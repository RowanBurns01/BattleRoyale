package model.entities.actionStrategies;

import controller.Post;
import model.entities.Player;
import model.Simulation;

import java.util.List;

public class Gift extends ActionStrategies {

    private int phraseNum;

    public Gift(int num){
        this.phraseNum = num;
    }

    @Override
    public void action(Player p, Simulation s) {
        Post post = s.getPost();
        List<String> hourLog = s.getHourLog();

        if(p.getHealth() < p.getTotalHealth()/2) {
            p.setHP(-50);
            String sponsor = "";
            if(phraseNum == 3){
                phraseNum = 0;
            }
            switch(phraseNum){
                case 0:
                    sponsor = "Kanye West. Thank you Kanye, very cool.";
                    break;
                case 1:
                    sponsor = "Christina Applegate. Quick shoutout to Christina Applegate!";
                    break;
                case 2:
                    sponsor = "Rick Sanchez. And then he turned himself into a pickle, funniest shit I've ever seen.";
                    break;
            }
            phraseNum ++;
            if(s.getPublish()){
                System.out.print("\ngift");
            }
            hourLog.add(String.format("%s has been sponsored a gift from %s\n%s wounds have been healed.",p.getName(),sponsor,p.getName()));
            post.addPlayer(p);
        }
    }
}
