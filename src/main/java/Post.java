import java.io.*;
import java.util.List;
import com.restfb.*;
import com.restfb.types.GraphResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Post {

    private FacebookClient facebookClient;
    private List<Player> players;
    private String pageAccessToken = "EAAi8qZBaTG6kBAHBrhwLG1nJU2dZB9YlukQBZCgQJGajNNJtFZBUeKVZBg8fwRsebp1QTTZBRGwvS1nKrhLYvpgH51ZA2rxsES887po9qqkTZBQVSOgAC5PELZCDdSuEpwx7Bny5DnDCs6ARZAZAhybWZCxshx1H8qfy15PxfbJZBZAcBzCHR0edyYxxraoL0Tq5k13epqzjUKxNO5BwZDZD";
    private BufferedImage finalImage;
    private String flag;
    private Day d;
    private byte[] imageInByte;
    private int count = 0;

    public Post(Day d){
        this.d = d;
        this.facebookClient = new DefaultFacebookClient(pageAccessToken, Version.LATEST);
    }

    public void makePost(String msg){
        System.out.println("\n"  + d.getDate().toString() + msg);
        postCount();
        GraphResponse publishPhotoResponse = facebookClient.publish("me/photos", GraphResponse.class,
                BinaryAttachment.with("HourlyPost",imageInByte),
                Parameter.with("message", msg),
                Parameter.with("published", false),
                Parameter.with("scheduled_publish_time", d.getDate().getTime() /1000));
    }

    public void makeTextPost(String msg){
        System.out.println("\n" + d.getDate().toString() + msg);
        postCount();
        GraphResponse publishMessageResponse = facebookClient.publish("me/feed", GraphResponse.class,
                Parameter.with("message", msg),
                Parameter.with("published", false),
                Parameter.with("scheduled_publish_time", d.getDate()));
    }

    public void postCount(){
        count ++;
        System.out.println(count);
    }
    
    public void clear(){
        if(players != null){
            players.clear();
        }
    }

    public void addPlayers(List<Player> players){
        this.players = players;
    }

    public void setFlag(String flag){ this.flag = flag; }

    public void combinePictures() {
        try {
            finalImage = ImageIO.read(new File("C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\resources\\" + players.get(0).getImagePath()));
            if(!players.get(0).getAlive()){
                finalImage = overlayCross(finalImage);
            }
            for(int i = 1; i < players.size(); i++){
                BufferedImage img2 = ImageIO.read(new File("C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\resources\\" + players.get(i).getImagePath()));
                if(!players.get(i).getAlive()){
                    img2 = overlayCross(img2);
                }
                finalImage = joinBufferedImage(finalImage, img2);
            }
            if(flag == "item"){
                BufferedImage img2 = ImageIO.read(new File("C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\resources\\" + players.get(0).getWeapons().get(players.get(0).getWeapons().size()-1).getImagePath()));
                finalImage = joinBufferedImage(finalImage, img2);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // This writes to baos
            ImageIO.write( finalImage, "png", baos );

            // This writes photos to resources
//            ImageIO.write( finalImage, "png", new File( "C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\resources\\post"+count+".png"));

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

//            // paint both images, preserving the alpha channels
//            Graphics g = combined.getGraphics();
//            g.drawImage(img, 0, 0, null);
//            g.drawImage(cross, 0, 0, null);
//
//
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
