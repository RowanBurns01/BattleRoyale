package controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import com.restfb.*;
import com.restfb.types.GraphResponse;
import model.entities.Player;

public class Post {

    /* To post on Facebook:
    Step 1: Update pageAccessToken using /me/accounts
    Step 2: Change debug to false
    Step 3: Set app to Live or In development in Facebook For Developers

    To print to System.out:
    Step 1: Change debug to true */

    private boolean debug = true;
    private FacebookClient facebookClient;
    private List<Player> players = new ArrayList<>();
    private String flag;
    private Day d;
    private byte[] imageInByte;
    private int count = 1;

    public Post(Day d){
        this.d = d;
        String pageAccessToken = "EAAi8qZBaTG6kBALcWiChPms6maQQMP6ZB58d4XxeYZCqgSGQOyqrhfRiq0TTNBM1pqwVvnpaNY4m08KH7JuB3zyQgOZAzg2huyQ1wmAgluCZCoj9U2lEvpVZAVHqZBpiXRkmF3wcArQujFOtGgDcAJ2ZC5Tn7ZBAwLjaMdvwcrbxqBimhOr00MWOQKBDfpRUZAln7dEjCRY9sH7QZDZD";
        this.facebookClient = new DefaultFacebookClient(pageAccessToken, Version.LATEST);
    }

    public void makePost(String msg){
        System.out.print("\n" +String.format("Post #%s\n",count)  + d.getDate().toString());
        postInc();
        if(!debug){
            facebookClient.publish("me/photos", GraphResponse.class,
                BinaryAttachment.with("HourlyPost",imageInByte),
                Parameter.with("message", msg),
                Parameter.with("published", false),
                Parameter.with("scheduled_publish_time", d.getDate().getTime() /1000));
        } else {
            System.out.println(msg);
        }
    }

    public boolean getDebug(){
        return debug;
    }

    public void makeTextPost(String msg){
        System.out.print("\n" + d.getDate().toString());
        if(!debug){
            facebookClient.publish("me/feed", GraphResponse.class,
                Parameter.with("message", msg),
                Parameter.with("published", false),
                Parameter.with("scheduled_publish_time", d.getDate()));
        } else {
            System.out.println(msg);
        }
    }

    private void postInc(){
        count ++;
    }
    
    public void clear(){
        if(players != null){
            players.clear();
        }
    }

    public void addPlayers(List<Player> players){
        this.players.addAll(players);
    }

    public void addPlayer(Player p) { this.players.add(p); }

    public void setFlag(String flag){ this.flag = flag; }

    public void combinePictures() {
        try {
            BufferedImage finalImage = ImageIO.read(new File("C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\resources\\tributes\\" + players.get(0).getImagePath()));
            finalImage = overlayHUD(finalImage, players.get(0));
            if (!players.get(0).getAlive()) {
                finalImage = overlayDead(finalImage);
            }
            assert finalImage != null;
            finalImage = addColouredBorder(finalImage);
            for (int i = 1; i < players.size(); i++) {
                BufferedImage img2 = ImageIO.read(new File("C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\resources\\tributes\\" + players.get(i).getImagePath()));
                img2 = overlayHUD(img2, players.get(i));
                if (!players.get(i).getAlive()) {
                    img2 = overlayDead(img2);
                }
                assert img2 != null;
                img2 = addColouredBorder(img2);
                finalImage = joinBufferedImage(finalImage, img2);
            }
            if (flag.equals("item")) {
                BufferedImage img2 = ImageIO.read(new File("C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\resources\\items\\" + players.get(0).getWeapons().get(players.get(0).getWeapons().size() - 1).getImagePath()));
                img2 = addColouredBorder(img2);
                finalImage = joinBufferedImage(finalImage, img2);
            }
            if (flag.equals("gift")) {
                BufferedImage img2 = ImageIO.read(new File("C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\resources\\items\\HealthPack.jpg"));
                img2 = addColouredBorder(img2);
                finalImage = joinBufferedImage(finalImage, img2);
            }

            publishBufferedImage(finalImage);

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void publishBufferedImage(BufferedImage finalImage){
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            if(!debug){
                // This writes to baos
                ImageIO.write( finalImage, "png", baos );
            } else {
                // This writes photos to output
                ImageIO.write( finalImage, "png", new File( "C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\resources\\outputs\\post"+count+".png"));
            }

            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
        } catch (IOException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage overlayDead(BufferedImage img){
        try {
            BufferedImage cross = ImageIO.read(new File("C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\resources\\utilities\\RedCross.png"));
            BufferedImage wasted = ImageIO.read(new File("C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\resources\\utilities\\Wasted.png"));

            int w = Math.max(img.getWidth(), cross.getWidth());
            int h = Math.max(img.getHeight(), cross.getHeight());

            BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = combined.createGraphics();
            g.setComposite(AlphaComposite.Clear);
            g.fillRect(0,0, w, h);
            g.setComposite(AlphaComposite.SrcOver);
            g.drawImage(img, 0, 0, null);
            float alpha = 0.50f;
            g.setComposite(AlphaComposite.SrcOver.derive(alpha));
            alpha = 0.70f;
            g.setComposite(AlphaComposite.SrcOver.derive(alpha));
            g.setColor(Color.gray);
            g.fillRect(0,0,w,h);
            alpha = 0.85f;
            g.setComposite(AlphaComposite.SrcOver.derive(alpha));
            g.drawImage(wasted,0,0,null);
            g.dispose();
            return combined;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private BufferedImage addColouredBorder(BufferedImage img){
        int w = img.getWidth();
        BufferedImage combined = new BufferedImage(w, w, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combined.createGraphics();
        g.drawImage(img, 0, 0, null);

        float thickness = 5.0f;
        Stroke oldStroke = g.getStroke();
        g.setStroke(new BasicStroke(thickness));
        g.setColor(new Color(24,54,16));
        g.drawRect(0,0,w,w);
        g.setStroke(oldStroke);

        return combined;
    }

    private BufferedImage overlayHUD(BufferedImage img, Player p){

        int w = img.getWidth();

        BufferedImage combined = new BufferedImage(w, w, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combined.createGraphics();
        g.drawImage(img, 0, 0, null);

        // Starting Coordinates
        int x = 30;
        int y = 400;
        int arc = 25;
        int smallArc = 5;

        // Base Rectangles
        g.setColor(new Color(86,113,84));
        g.fillRoundRect(x,y,250,72,arc,arc);
        g.setColor(new Color(24,54,16));
        g.fillRoundRect(x+1,y+1,250-2,72-2,arc,arc);
        g.setColor(new Color(255,255,235));
        g.fillRoundRect(x+3,y+3,250-6,72-6,arc,arc);

        // Health Bar
        g.setColor(new Color(24,54,16));
        g.fillRoundRect(x+22,y+37,212,22,smallArc,smallArc);
        g.setColor(Color.white);
        g.fillRoundRect(x+59,y+39,173,18,smallArc,smallArc);
        g.setColor(Color.darkGray);
        g.fillRect(x+61,y+41,169,14);

        double percentage = (double)p.getHealth()/(double)p.getTotalHealth();
        long width = Math.round( percentage*169);

        if(percentage> 0.5){
            g.setColor(new Color(105,239,154));
        }else if(percentage>0.2){
            g.setColor(new Color(218, 183, 36));
        } else {
            g.setColor(new Color(255, 47, 49));
        }
        g.fillRect(x+61,y+41,(int)width,14);

        // Text
        g.setColor(Color.BLACK);
        Font font = new Font("Calibri", Font.PLAIN, 22);
        g.setFont(font);
        g.drawString(String.format("%s",p.getName()),x+25,y+28);

        String buffer = "";
        if(p.getDistrict()<10){
            buffer = "  ";
        }
        g.drawString(String.format("%sLv%s",buffer,p.getLevel()),x+100,y+28);
        g.drawString(String.format("%sDistrict %s",buffer, p.getDistrict()),x+140,y+28);
        g.setColor(new Color(246,193,62));
        font = new Font("Calibri", Font.BOLD, 22);
        g.setFont(font);
        g.drawString("HP",x+27,y+55);




        return combined;

    }

    private BufferedImage joinBufferedImage(BufferedImage img1,
                                            BufferedImage img2) {
        int offset = 0;
        int width = img1.getWidth() + img2.getWidth() + offset;
        int height = Math.max(img1.getHeight(), img2.getHeight()) + offset;
        BufferedImage newImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();
        Color oldColor = g2.getColor();
        g2.setPaint(Color.BLACK);
        g2.fillRect(0, 0, width, height);
        g2.setColor(oldColor);
        g2.drawImage(img1, null, 0, 0);
        g2.drawImage(img2, null, img1.getWidth() + offset, 0);
        g2.dispose();
        return newImage;
    }
}
