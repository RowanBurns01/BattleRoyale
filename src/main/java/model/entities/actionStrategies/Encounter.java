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
    public void action(Player a, Simulation s) {

        simulation = s;
        hourLog = simulation.getHourLog();
        contestants = simulation.getContestants();
        post = simulation.getPost();
        boolean backStab = false;
        // setup
        // side a vs side b
        // Get allies of both sides

        List<Player> teamOne = new ArrayList<>(a.getAllies());
        List<Player> teamTwo;
        teamOne.add(a);
        Player b = r.chooseRandomPerson(teamOne, contestants);
        int odds = r.generateNumber(5-a.getAllies().size());
        if(b == null || (a.hasAnAlly() && odds==0)){
            teamTwo = new ArrayList<>(backStab(a));
            teamOne.removeAll(teamTwo);
            backStab = true;
        } else {
            teamTwo = new ArrayList<>(b.getAllies());
            teamTwo.add(b);
        }

        List<Player> fighters = new ArrayList<>();
        fighters.addAll(teamOne);
        fighters.addAll(teamTwo);

        // while loop of either a fight or flee
        // the team with less people can potentially flee at this point, consideration speed and number of others
        // otherwise theres a fight
        start(teamOne, teamTwo, backStab);
        int round = 1;
        while(!teamOne.isEmpty() && !teamTwo.isEmpty()){
            Player death = null;

            //Flee Attempt
            if(round > 4){
                if(teamOne.size() > teamTwo.size()){
                    if(teamTwo.removeAll(flee(teamTwo, teamOne))){
                        continue;
                    }
                } else if(teamTwo.size() > teamOne.size()) {
                    if(teamOne.removeAll(flee(teamOne, teamTwo))){
                        continue;
                    }
                }
            }


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
//                    hourLog.add(String.format("%s has attacked %s",attacker.getName(),receiver.getName()));
                    if(kill){
                        death(attacker,receiver);
                        death = receiver;
                    }
                }
            }

            teamOne.remove(death);
            teamTwo.remove(death);
            fighters.remove(death);
            round++;
        }
//        for(Player p: fighters){
//            hourLog.add(p.getName() + " HP: " + p.getHealth());
//        }
        hourLog.add("The fight has finished.");

    }

    public List<Player> flee(List<Player> smallerTeam, List<Player> largerTeam){

        List<Player> fleeing = new ArrayList<>();
        int teamSize = largerTeam.size();
        int momentum = 1;
        int fleeingOdds = largerTeam.size() + momentum;

        for(Player p: smallerTeam){
            if(r.generateNumber(fleeingOdds) >=teamSize){
                fleeing.add(p);
                momentum += 2;
            }
        }

        if(!fleeing.isEmpty()){
            String msg = "";
            if(fleeing.size() == 1){
                msg += String.format("%s has",fleeing.get(0).getName());
            } else {
                for(int k = 0; k < fleeing.size()-1; k++) {
                    msg += fleeing.get(k).getName() + ", ";
                    post.addPlayer(fleeing.get(k));
                }
                msg += ".";
                msg = msg.replace(", ."," and ");
                msg = String.format(msg +"%s have",fleeing.get(fleeing.size()-1).getName());
            }
            hourLog.add(msg + " fled from the battle!");
        }
        return fleeing;
    }

    public void death(Player attacker, Player receiver){
        receiver.removeAllAllies();
        receiver.setAlive(false);
        hourLog.add(attacker.getName() + " has killed " + receiver.getName() + " with his " + attacker.getLastUsedWeapon().getName() + ".");
        Weapon dropped = receiver.getOneWeapon();
        if(dropped.getValue() != 0){
            attacker.addWeapon(dropped);
            hourLog.add("From "+ receiver.getName() +"'s body, " + attacker.getName() + " has acquired a " + dropped.getName() + ".");
        }
        if(attacker.incKillCount()){
            int level = attacker.getLevel();
            int health = r.generateNumber(attacker.getTotalHealth());
            int defence = r.generateNumber(level*10);
            int attack = r.generateNumber( level*10);
            int dexterity = r.generateNumber( level * 5);
            String s = String.format("%s has leveled up to level %s:\n Health : %s\n Attack : %s\n Defence : %s\n Dexterity : %s",attacker.getName(), Integer.toString(attacker.getLevel()+1),attacker.getHealth() + " + " + health, attacker.getAttack() + " + " + attack, attacker.getDefence() + " + " + defence, attacker.getDexterity() + " + " + dexterity);
            hourLog.add(s);
            attacker.levelUp(health,defence,attack, dexterity);
        }
    }

    public void start(List<Player> a, List<Player> b, boolean backStab){
        String message = "";
        List<Player> teamOne;
        List<Player> teamTwo;
        if(a.size()> b.size()){
            teamOne = a;
            teamTwo = b;
        } else {
            teamOne = b;
            teamTwo = a;
        }

        if(teamOne.size() == 1){
            message += teamOne.get(0).getName();
            if(backStab){
                message += " has broken the allegiance and turned on ";
            } else {
                message += " has attacked ";
            }
        } else {
            for(int i = 0; i< teamOne.size()-1 ; i++) {
                message += teamOne.get(i).getName() + ", ";
            }
            message += "and";
            message = message.replace(", and", " and");
            if(backStab){
                message += String.format(" %s have broken their allegiance and turned on ",teamOne.get(teamOne.size()-1).getName());
            } else {
                message += String.format(" %s have attacked ",teamOne.get(teamOne.size()-1).getName());
            }

        }


        if(teamTwo.size() == 1){
            message += teamTwo.get(0).getName() + ".";
        } else {
            for(int i = 0; i< teamTwo.size()-1 ; i++) {
                message += teamTwo.get(i).getName() + ", ";
            }
            message += "and";
            message = message.replace(", and", " and");
            message += String.format(" %s.",teamTwo.get(teamTwo.size()-1).getName());
        }

        hourLog.add(message);
        post.addPlayers(teamOne);
        post.addPlayers(teamTwo);
    }

    public List<Player> backStab(Player p){
        List<Player> groupOne = new ArrayList<>();
        List<Player> groupTwo = new ArrayList<>();

        if(!p.getAllies().isEmpty()){

            List<Player> originalGroup = new ArrayList<>(p.getAllies());
            //SPlit allegiance


            groupOne.add(p);
            Collections.shuffle(originalGroup);
            groupTwo.add(originalGroup.get(0));

            for(int i = 1; i < originalGroup.size(); i++){
                originalGroup.get(i).removeAllAllies();
                if(r.generateNumber(1)== 1){
                    groupOne.add(originalGroup.get(i));
                } else {
                    groupTwo.add(originalGroup.get(i));
                }
            }

            for(Player p1: groupOne){
                for(Player p2: groupOne){
                    if(!p1.equals(p2)){
                        p1.addAlly(p2);
                    }
                }
            }

            for(Player p1: groupTwo){
                for(Player p2: groupTwo){
                    if(!p1.equals(p2)){
                        p1.addAlly(p2);
                    }
                }
            }
        }
        return groupTwo;
    }

}
