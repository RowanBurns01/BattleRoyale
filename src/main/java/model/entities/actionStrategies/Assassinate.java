package model.entities.actionStrategies;

import model.entities.Player;
import controller.Post;
import model.Simulation;
import model.utilities.Random;
import model.entities.Weapon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Assassinate extends ActionStrategies {

    @Override
    public void action(Player p, Simulation s) {
        List<String> hourLog = s.getHourLog();
        Post post = s.getPost();
        Random r = new Random();
        Player two = r.chooseRandomPerson(p,s.getContestants());
        boolean stolen = false;
        int i = 0;
        while (!stolen && i < 50) {
            if(!p.equals(two)) {
                List<Weapon> choices = new ArrayList<>(two.getWeapons());
                for(Weapon w : choices){
                    if(w.getValue() != 0 && !stolen) {
                        two.removeWeapon(w);
                        p.addWeapon(w);
                        post.addPlayers(new ArrayList<>(Arrays.asList(p,two)));
                        String msg = p.getName() + " has used the cover of night to sneak into " +two.getName() + "'s campsite.\n";
                        msg += two.getName() + " has been murdered with his own " + w.getName() +".";
                        if(s.getPublish()){
                            System.out.print("\nassassination");
                        }
                        hourLog.add(msg);
                        if(p.incKillCount()){
                            hourLog.add(p.levelUp());
                        }
                        two.setHP(Integer.MAX_VALUE);
                        two.removeAllAllies();
                        two.setAlive(false);
                        stolen = true;
                    }
                }
            }
            i ++;
        }
    }
}
