package model.entities.actionStrategies;

import controller.Day;
import controller.Post;
import model.entities.Player;
import model.entities.Random;
import model.Simulation;

import java.util.List;

public class AccidentalDeath extends ActionStrategies{

    @Override
    public void action(Player p, Simulation s) {
        Simulation simulation = s;
        List<String> hourLog = s.getHourLog();
        Day d = s.getDay();
        Post post = s.getPost();
        Random r = new Random();

        switch(r.generateNumber(1)){
            case 0:
                hourLog.add(p.getName() + " was unable to find water and has died from dehydration.");
                break;
            case 1:
                hourLog.add(p.getName() + " has been swept away in a flood and has drowned.");
                break;
        }
        p.removeAllAllies();
        p.setAlive(false);
        post.addPlayer(p);
    }
}
