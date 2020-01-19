import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.restfb.*;
import com.restfb.types.GraphResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import static java.lang.System.currentTimeMillis;

public class Post {

    private FacebookClient facebookClient;
    private List<Player> players;
    private String pageAccessToken = "EAAi8qZBaTG6kBAPI9Xmtuj5t5O3ZAZCGKq6n5TOjvma5T95SXaFmBWIViSZAcfNM25kDJyZBlqc0aPlvpuv4LRJq65MOaEv2nENaTeYDqiYj1YwriBePWdoboUQ69eWC1W3mpxPZAyP1HkJiLVN26BGG8OgwCTkhYZAPEn4i9AwXVCiaBZBaSCS5OctMczSNW7QZD";
    private BufferedImage finalImage;
    private String flag;
    private Day d;
    private byte[] imageInByte;

    public Post(Day d){
        this.d = d;
        this.facebookClient = new DefaultFacebookClient(pageAccessToken, Version.LATEST);
    }

    public void makePost(String msg){
        System.out.println("\n"  + d.getDate().toString() + msg);

        GraphResponse publishPhotoResponse = facebookClient.publish("me/photos", GraphResponse.class,
                BinaryAttachment.with("HourlyPost", imageInByte),
                Parameter.with("message", msg));
//                Parameter.with("published", false),
////                Parameter.with("scheduled_publish_time", d.getDate()));
    }

    public void makeTextPost(String msg){
        System.out.println("\n" + d.getDate().toString() + msg);

        GraphResponse publishMessageResponse = facebookClient.publish("me/feed", GraphResponse.class,
                Parameter.with("message", msg),
                Parameter.with("published", false),
                Parameter.with("scheduled_publish_time", d.getDate()));
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
            for(int i = 1; i < players.size(); i++){
                BufferedImage img2 = ImageIO.read(new File("C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\resources\\" + players.get(i).getImagePath()));
                finalImage = joinBufferedImage(finalImage, img2);
            }
            if(flag == "item"){
                BufferedImage img2 = ImageIO.read(new File("C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\resources\\" + players.get(0).getWeapons().get(players.get(0).getWeapons().size()-1).getImagePath()));
                finalImage = joinBufferedImage(finalImage, img2);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( finalImage, "png", baos );
            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();

        } catch (IOException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public static void resize(String inputImagePath,
                              String outputImagePath, int scaledWidth, int scaledHeight)
            throws IOException {
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
        String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);
        ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }

    public BufferedImage joinBufferedImage(BufferedImage img1,
                                                  BufferedImage img2) {
        try {
            // Leaving here if needed again
            if(img1.getWidth() > img2.getWidth()){
                if(flag == "item"){
                    resize("C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\resources\\" + players.get(0).getWeapons().get(players.get(0).getWeapons().size()-1).getImagePath(),"C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\resources\\" + players.get(0).getWeapons().get(players.get(0).getWeapons().size()-1).getImagePath(), img2.getWidth(), img2.getHeight());
                } else {
                    resize("C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\resources\\" + players.get(0).getImagePath(),"C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\resources\\" + players.get(0).getImagePath(), img2.getWidth(), img2.getHeight());
                }
            } else {
                if(flag == "item"){
                    resize("C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\resources\\" + players.get(0).getWeapons().get(players.get(0).getWeapons().size()-1).getImagePath(),"C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\resources\\" + players.get(0).getWeapons().get(players.get(0).getWeapons().size()-1).getImagePath(), img1.getWidth(), img1.getHeight());
                } else {
                    resize("C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\resources\\" + players.get(1).getImagePath(),"C:\\Users\\rljb\\Desktop\\Organised Folders\\GitHub\\Royale\\src\\main\\resources\\" + players.get(1).getImagePath(), img1.getWidth(), img1.getHeight());
                }
            }
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
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
