package controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.restfb.*;
import model.entities.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Post {

    private FacebookClient facebookClient;
    private List<Player> players = new ArrayList<>();
    private String pageAccessToken = "EAAi8qZBaTG6kBAHBrhwLG1nJU2dZB9YlukQBZCgQJGajNNJtFZBUeKVZBg8fwRsebp1QTTZBRGwvS1nKrhLYvpgH51ZA2rxsES887po9qqkTZBQVSOgAC5PELZCDdSuEpwx7Bny5DnDCs6ARZAZAhybWZCxshx1H8qfy15PxfbJZBZAcBzCHR0edyYxxraoL0Tq5k13epqzjUKxNO5BwZDZD";
    private BufferedImage finalImage;
    private String flag;
    private Day d;
    private byte[] imageInByte;
    private int count = 1;

    public Post(Day d){
        this.d = d;
        this.facebookClient = new DefaultFacebookClient(pageAccessToken, Version.LATEST);
    }

    public void makePost(String msg){

//        System.out.print("\n" +String.format("Post #%s\n",count)  + d.getDate().toString());
        System.out.println(msg);
        postInc();
//        GraphResponse publishPhotoResponse = facebookClient.publish("me/photos", GraphResponse.class,
//                BinaryAttachment.with("HourlyPost",imageInByte),
//                Parameter.with("message", msg),
//                Parameter.with("published", false),
//                Parameter.with("scheduled_publish_time", d.getDate().getTime() /1000));
    }

    public void makeTextPost(String msg){
//        System.out.print("\n" + d.getDate().toString());
        System.out.println(msg);
//        GraphResponse publishMessageResponse = facebookClient.publish("me/feed", GraphResponse.class,
//                Parameter.with("message", msg),
//                Parameter.with("published", false),
//                Parameter.with("scheduled_publish_time", d.getDate()));
    }

    public void postInc(){
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
            finalImage = ImageIO.read(new File("C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\resources\\Tributes\\" + players.get(0).getImagePath()));
            if(!players.get(0).getAlive()){
                finalImage = overlayCross(finalImage);
            } else if (flag == "death"){
                finalImage = overlayHealth(finalImage,players.get(0));
            }
            for(int i = 1; i < players.size(); i++){
                BufferedImage img2 = ImageIO.read(new File("C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\resources\\Tributes\\" + players.get(i).getImagePath()));
                if(!players.get(i).getAlive()){
                    img2 = overlayCross(img2);
                } else if (flag == "death") {
                    img2 = overlayHealth(img2,players.get(i));
                }
                finalImage = joinBufferedImage(finalImage, img2);
            }
            if(flag == "item"){
                BufferedImage img2 = ImageIO.read(new File("C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\resources\\Items\\" + players.get(0).getWeapons().get(players.get(0).getWeapons().size()-1).getImagePath()));
                finalImage = joinBufferedImage(finalImage, img2);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // This writes to baos
//            ImageIO.write( finalImage, "png", baos );

            // This writes photos to output
            ImageIO.write( finalImage, "png", new File( "C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\output\\post"+count+".png"));

            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();

        } catch (IOException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage overlayCross(BufferedImage img){

        try {
            BufferedImage cross = ImageIO.read(new File("C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\resources\\RedCross.png"));

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
            g.drawImage(cross, 0, 0, null);
            g.dispose();
            return combined;


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BufferedImage overlayHealth(BufferedImage img,Player p){

        int w = img.getWidth();

        BufferedImage combined = new BufferedImage(w, w, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combined.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.setColor(Color.white);
        int x = 40;
        g.fillRect(x,430,500-2*x,x);
        g.setColor(Color.darkGray);
        g.fillRect(x+5,435,500-2*(x+5),x-10);

        double percentage = (double)p.getHealth()/(double)p.getTotalHealth();
        long width = Math.round( percentage*410.0);

        g.setColor(Color.white);
        Font font = new Font("Serif", Font.BOLD, 24);
        g.setFont(font);
        g.drawString(String.format("District: %s",p.getDistrict()),x+10,395);
        g.drawString(String.format("HP: %s/%s",p.getHealth(),p.getTotalHealth()),x+10,420);

        if(percentage> 0.5){
            g.setColor(new Color(154,255,167));
        }else if(percentage>0.2){
            g.setColor(new Color(255, 198, 52));
        } else {
            g.setColor(new Color(255, 47, 49));
        }
        g.fillRect(x+5,435,(int)width,x-10);

        return combined;

    }

    public BufferedImage joinBufferedImage(BufferedImage img1,
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
