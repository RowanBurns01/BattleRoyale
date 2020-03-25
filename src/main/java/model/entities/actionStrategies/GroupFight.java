package model.entities.actionStrategies;

import controller.Post;
import model.Simulation;
import model.entities.Player;
import model.entities.Random;
import model.entities.Weapon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//public class GroupFight extends ActionStrategies {

    // NO LONGER IN USE

//    Random r = new Random();
//    Simulation simulation;
//    List<String> hourLog;
//    List<Player> contestants;
//    Post post;
//
//    @Override
//    public void action(Player a,  Simulation s) {
//        simulation = s;
//        hourLog = simulation.getHourLog();
//        contestants = simulation.getContestants();
//        post = simulation.getPost();
//
//        // Add all fighters
//        Player b = r.chooseRandomPerson(a, contestants);
//        List<Player> fighters = new ArrayList<>(Arrays.asList(a));
//        List<Player> teamOne = a.getAllies();
//        List<Player> teamTwo = b.getAllies();
//        teamOne.add(a);
//        teamTwo.add(b);
//        fighters.addAll(teamOne);
//        fighters.addAll(teamTwo);
//
//        // Start arena.Fight
//        String message = "A fight has broken out between: ";
//        for(Player fighter : fighters) {
//            message += fighter.getName() + ", ";
//            post.addPlayer(fighter);
//        }
//        hourLog.add(message.replace(", ", "."));
//        int numberOfDeaths = 0;
//
//        // Fights
//        int i = 0;
//        while(i < fighters.size()){
//            i++;
//
//            List<Player> toDie = skirmish(teamOne,teamTwo);
//            List<Player> sacrificed = new ArrayList<>();
//            for(Player p: toDie){
//                if(p.hasLover()) {
//                    hourLog.add("Just as " + p.getName() + " was about to be killed by " + p.getKiller().getName() + ", " + p.getLover().getName() + " sacrificed himself out of love.");
//                    sacrificed.add(p.getLover());
//                }
//            }
//
//            // bug potential here
//            for(Player p: sacrificed){
//                toDie.add(p);
//                toDie.remove(p.getLover());
//            }
//
//            for(Player p: toDie){
//                System.out.println(p.getKiller());
//                Weapon w = p.getKiller().getOneWeapon();
//                hourLog.add(p.getKiller().getName() + " has killed " + p.getName() + " with his " + w.getName() + ".");
//                p.getKiller().incKillCount();
//                numberOfDeaths +=1;
//                p.setAlive(false);
//                p.setUnavailable();
//                Weapon l = p.getOneWeapon();
//                if(l != null){
//                    p.getKiller().addWeapon(l);
//                    hourLog.add("From "+ p.getName() +"'s body, " + p.getKiller().getName() + " has acquired a " + l.getName() + ".");
//                }
//            }
//
//            if(numberOfDeaths == 0){
//                hourLog.add("Everyone has escaped unharmed.");
//            } else {
//                hourLog.add("The fight has finished.");
//            }
//        }
//    }
//
//    public int calculateScore(List<Player> group) {
//        int total = 0;
//        for(Player p : group) {
//            total += p.getOneWeapon().getValue();
//            total += r.generateNumber(10);
//        }
//        return total;
//    }
//
//    public List<Player> skirmish(List<Player> teamOne, List<Player> teamTwo) {
//
//        int teamOneTotal = calculateScore(teamOne);
//        int teamTwoTotal = calculateScore(teamTwo);
//        int total = teamOneTotal + teamTwoTotal;
//
//        List<Player> toDie = new ArrayList<>();
//
//        int roll = r.generateNumber(total);
//        if(roll < teamOneTotal - 3) {
//            Player oldMate = r.chooseRandomPerson(teamTwo);
//            toDie.add(oldMate);
//            oldMate.setKiller(r.chooseRandomPerson(teamOne));
//            oldMate.setUnavailable();
//        } else if (roll < teamOneTotal){
//            Player oldMate = r.chooseRandomPerson(teamTwo);
//            hourLog.add(oldMate.getName() + " has fled from the skirmish and ended their allegiance!");
//            oldMate.setUnavailable();
//        } else {
//            Player oldMate = r.chooseRandomPerson(teamOne);
//            toDie.add(oldMate);
//            oldMate.setKiller(r.chooseRandomPerson(teamTwo));
//            oldMate.setUnavailable();
//        }
//        return toDie;
//    }
//}

