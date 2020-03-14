package model.entities.actionStrategies;

import model.entities.Player;
import controller.Post;
import model.Simulation;
import model.entities.Weapon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Steal extends ActionStrategies {

    @Override
    public void action(Player p, Player two, Simulation s) {
        List<String> hourLog = s.getHourLog();
        Post post = s.getPost();

        boolean stolen = false;
        int i = 0;
        while (!stolen && i < 50) {
            if(two == null){
                System.out.println("null");
            }
            if(!p.equals(two)) {
                List<Weapon> choices = new ArrayList<>();
                for (Weapon g : two.getWeapons()) {
                    choices.add(g);
                }
                for(Weapon w : choices){
                    if(w.getValue() != 0 && !stolen) {
                        two.removeWeapon(w);
                        p.addWeapon(w);
                        post.addPlayers(new ArrayList<>(Arrays.asList(p,two)));
                        hourLog.add(p.getName() + " has stolen the " + w.getName() + " from " + two.getName() + ".");
                        stolen = true;
                    }
                }
            }
            i ++;
        }
    }
}
