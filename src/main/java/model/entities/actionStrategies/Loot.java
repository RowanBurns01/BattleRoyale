package model.entities.actionStrategies;

import controller.Post;
import model.entities.Player;
import model.entities.Random;
import model.entities.Weapon;
import model.Simulation;

import java.util.List;

public class Loot extends ActionStrategies {

    Random r = new Random();

    @Override
    public void action(Player p, Simulation s) {
        List<Weapon> weaponry = s.getWeaponry();
        Post post = s.getPost();
        List<String> hourLog = s.getHourLog();

        if(!weaponry.isEmpty() && p.getWeapons().isEmpty()) {
            int num = r.generateNumber(weaponry.size()-1);
            Weapon w = weaponry.get(num);
            if(w.getValue() != 0) {
                weaponry.remove(w);
                p.addWeapon(w);
                post.addPlayer(p);
                hourLog.add(p.getName() + " has acquired a " + w.getName() + ".");
            }

        }
    }
}
