package model.entities.actionStrategies;

import controller.Post;
import model.entities.Player;
import model.Simulation;
import java.util.List;

public class AccidentalDeath extends ActionStrategies{

    private int counter;

    public AccidentalDeath(int counter){
        this.counter = counter;
    }

    @Override
    public void action(Player p, Simulation s) {
        List<String> hourLog = s.getHourLog();
        Post post = s.getPost();
        if(counter == 4){
            counter = 0;
        }
        switch(counter){
            case 0:
                hourLog.add(p.getName() + " has been set ablaze by a fireball and has burnt alive.");
                break;
            case 1:
                hourLog.add(p.getName() + " has been swept away in a flood and has drowned.");
                break;
            case 2:
                hourLog.add(p.getName() + " has been bitten by a monkey and has passed away.");
                break;
            case 3:
                hourLog.add(p.getName() + " has disappeared off the grid and is assumed dead.");
                break;
        }
        counter ++;
        if(p.hasLover()){
            hourLog.add(p.getLover().getName() + " is devastated to see him go.");
        }
//        if(s.getDebug()){
            System.out.print("\naccident");
//        }
        p.setHP(Integer.MAX_VALUE);
        p.removeAllAllies();
        p.setAlive(false);
        post.addPlayer(p);
    }
}
