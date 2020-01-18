import java.util.List;
import com.restfb.*;
import com.restfb.types.GraphResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Post {
    private FacebookClient facebookClient;
    private List<Player> players;
    private String pageAccessToken = "EAAi8qZBaTG6kBAIRiTnv8SfSL88rQYRYtyfNl80Uu6RyPXZBc0rBe0sc0CFGRQh5ltlTd2GZButtoEpxKRQSUcKyC1RtW31hVslQU0nHcRYB8sDaitga1yaDOPvFN5u49HBUR0kGfvoeqLZCuSW3GFyHn6UcGjR8kmAKTuhhVxjTKCYIxTX3gxgy27X01o4ZD";
    private BufferedImage finalImage;

    public Post(){
        this.facebookClient = new DefaultFacebookClient(pageAccessToken, Version.LATEST);
    }

    public void makePost(String msg){
        System.out.println(msg);
//        System.out.println("picture");
//        GraphResponse publishPhotoResponse = facebookClient.publish("me/photos", GraphResponse.class,
//                BinaryAttachment.with("Hourly Post", Post.class.getResourceAsStream("/Percy The Dog.jpg")),
//                Parameter.with("message", msg));

    }

    public void clear(){
        if(players != null){
            players.clear();
        }
    }

    public void makeTextPost(String msg){
        System.out.println(msg);
//        GraphResponse publishMessageResponse = facebookClient.publish("me/feed", GraphResponse.class,
//                Parameter.with("message", msg));
    }

    public void addPlayers(List<Player> players){
        this.players = players;
    }

    public void combinePictures(){
        try {
            finalImage = ImageIO.read(new File("C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\resources\\" + players.get(0).getImagePath()));
            for(int i = 1; i < players.size(); i++){
                BufferedImage img2 = ImageIO.read(new File("C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\resources\\"+ players.get(0).getImagePath()));
                finalImage = joinBufferedImage(finalImage,img2);
            }
//            boolean success = ImageIO.write(finalImage, "jpg", new File("Hourly Post.jpg"));
//            System.out.println("saved success? "+success);

        } catch (IOException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage joinBufferedImage(BufferedImage img1,
                                                  BufferedImage img2) {
        int offset = 2;
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
