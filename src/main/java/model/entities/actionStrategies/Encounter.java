package model.entities.actionStrategies;

import controller.Post;
import model.Simulation;
import model.entities.Player;
import model.entities.Random;
import model.entities.Weapon;

import java.util.*;

public class Encounter extends ActionStrategies {

    Random r = new Random();
    Simulation simulation;
    List<String> hourLog;
    List<Player> contestants;
    Post post;

    @Override
    public void action(Player a, Player b, Simulation s) {

        simulation = s;
        hourLog = simulation.getHourLog();
        contestants = simulation.getContestants();
        post = simulation.getPost();

        // setup
        // side a vs side b
        // Get allies of both sides

        List<Player> teamOne = a.getAllies();
        teamOne.add(a);

        b = r.chooseRandomPerson(teamOne, contestants);
        List<Player> teamTwo = b.getAllies();
        teamTwo.add(b);

        List<Player> fighters = new ArrayList<>();
        fighters.addAll(teamOne);
        fighters.addAll(teamTwo);

        // while loop of either a fight or flee
        // the team with less people can potentially flee at this point, consideration speed and number of others
        // otherwise theres a fight
        start(teamOne, teamTwo);
        int i = 0;
        while(!teamOne.isEmpty() && !teamTwo.isEmpty()){
            Player death = null;
            // FIGHT!

            // can't work out how to use the comparator to sort by dexterity
            Collections.shuffle(fighters);

            for(Player attacker : fighters){
                if(death == null){
                    Player receiver;
                    if(teamOne.contains(attacker)){
                        receiver = r.chooseRandomPerson(teamTwo);
                    } else {
                        receiver = r.chooseRandomPerson(teamOne);
                    }
                    boolean kill = attacker.attack(receiver);
                    if(kill){
                        death(attacker,receiver);
                        death = receiver;
                    }
                }
            }
            teamOne.remove(death);
            teamTwo.remove(death);
            fighters.remove(death);
        }
        for(Player p: fighters){
            hourLog.add(p.getName() + " HP: " + p.getHealth());
        }
        hourLog.add("The fight has finished.");

    }

    public void death(Player attacker, Player receiver){
        receiver.setAlive(false);
        hourLog.add(attacker.getName() + " has killed " + receiver.getName() + " with his " + attacker.getLastUsedWeapon().getName() + ".");
        Weapon dropped = receiver.getOneWeapon();
        if(dropped.getValue() != 0){
            attacker.addWeapon(dropped);
            hourLog.add("From "+ receiver.getName() +"'s body, " + attacker.getName() + " has acquired a " + dropped.getName() + ".");
        }
        if(attacker.incKillCount()){
            int level = attacker.getLevel();
            int health = r.generateNumber(level*30);
            int defence = r.generateNumber(level*10);
            int attack = r.generateNumber( level*10);
            hourLog.add(attacker.getName() + " has leveled up, his new stats are:" +
                    "\nHealth: " + attacker.getTotalHealth() + " + " + health +
                    "\nAttack: " + attacker.getAttack() + " + " + attack +
                    "\nDefence: " + attacker.getDefence() + " + " + defence);
            attacker.levelUp(health,defence,attack);
        }
    }

    public void start(List<Player> teamOne, List<Player> teamTwo){
        String message = "A fight has broken out between: ";
        for(Player fighter : teamOne) {
            message += fighter.getName() + ", ";
            post.addPlayer(fighter);
        }
        message += "and ";

        for(Player fighter : teamTwo) {
            message += fighter.getName() + ", ";
            post.addPlayer(fighter);
        }
        hourLog.add(message.replace(", .", "."));
    }

}
