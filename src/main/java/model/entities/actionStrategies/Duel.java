package model.entities.actionStrategies;

import controller.Day;
import controller.Post;
import model.Simulation;
import model.entities.Player;
import model.entities.Random;
import model.entities.Weapon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Duel extends ActionStrategies {

    // NO LONGER IN USE

    @Override
    public void action(Player p, Simulation s) {
//        List<String> hourLog = s.getHourLog();
//        Day d = s.getDay();
//        Post post = s.getPost();
//        Random r = new Random();
//
//        Player winner = p;
//        if(loser != null){
//            int roleOne = p.getOneWeapon().getValue() + r.generateNumber(10);
//            int roleTwo = loser.getOneWeapon().getValue() + r.generateNumber(10);
//            if ( roleOne < roleTwo) {
//                winner = loser;
//                loser = p;
//            }
//
//            String msg = "";
//            if(loser.getLover() != null && !loser.getLover().equals(winner) && loser.getLover().getAlive()){
//                msg += "Just as " + loser.getName() + " was about to be killed by " + winner.getName() +", " + loser.getLover().getName() + " sacrificed himself out of love.\n";
//                Player og = loser;
//                loser = og.getLover();
//                og.setLover(null);
//                post.addPlayer(og);
//            } else if (loser.hasLover()) {
//                if(loser.getLover().equals(winner)) {
//                    msg += winner.getName() + " became paranoid that his lover " + loser.getName() + " was going to betray him.\n";
//                } else if(d.getHour() <3 || d.getHour() > 23) {
//                    msg += winner.getName() + " used the cover of night to sneak into " + loser.getName() + "'s campsite.\n";
//                }
//            } else if(d.getHour() <3 || d.getHour() > 23) {
//                msg += winner.getName() + " used the cover of night to sneak into " + loser.getName() + "'s campsite.\n";
//            }
//            Weapon w = winner.getOneWeapon();
//            hourLog.add(msg + winner.getName() + " has killed " + loser.getName() + " with his " + w.getName() + ".");
//            winner.incKillCount();
//            loser.setAlive(false);
//            loser.setUnavailable();
//            Weapon l = loser.getOneWeapon();
//            if(l != null){
//                winner.addWeapon(l);
//                hourLog.add("From "+ loser.getName() +"'s body, " + winner.getName() + " has acquired a " + l.getName() + ".");
//            }
//            post.addPlayers(new ArrayList<>(Arrays.asList(winner, loser)));
//        }
    }
}
