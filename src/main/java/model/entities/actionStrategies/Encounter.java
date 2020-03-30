package model.entities.actionStrategies;

import controller.Post;
import model.Simulation;
import model.entities.Player;
import model.utilities.Random;
import model.entities.sortingApproaches.SortBySpeed;
import model.entities.Weapon;

import java.util.*;

public class Encounter extends ActionStrategies {

    private Random r = new Random();
    private List<String> hourLog;
    private List<Player> contestants;
    private Post post;

    @Override
    public void action(Player a, Simulation s) {

        hourLog = s.getHourLog();
        contestants = s.getContestants();
        post = s.getPost();
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

            fighters.sort(new SortBySpeed());

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
                        if(receiver.hasLover()){
                            hourLog.add("Just as " + receiver.getName() + " was about to be killed by " + attacker.getName() +", " + receiver.getLover().getName() + " sacrificed himself out of love.");
                            receiver.setHP(Integer.MAX_VALUE);
                            receiver.setHP(-receiver.getLover().getHealth());
                            receiver = receiver.getLover();
                            receiver.setHP(Integer.MAX_VALUE);
                        }
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

    private List<Player> flee(List<Player> smallerTeam, List<Player> largerTeam){

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
            StringBuilder msg = new StringBuilder();
            if(fleeing.size() == 1){
                msg.append(String.format("%s has", fleeing.get(0).getName()));
            } else {
                for(int k = 0; k < fleeing.size()-1; k++) {
                    msg.append(fleeing.get(k).getName()).append(", ");
                    post.addPlayer(fleeing.get(k));
                }
                msg.append(".");
                msg = new StringBuilder(msg.toString().replace(", .", " and "));
                msg = new StringBuilder(String.format(msg + "%s have", fleeing.get(fleeing.size() - 1).getName()));
            }
            hourLog.add(msg + " fled from the battle!");
        }
        return fleeing;
    }

    private void death(Player attacker, Player receiver){
        receiver.removeAllAllies();
        receiver.setAlive(false);
        String msg = attacker.getName() + " has killed " + receiver.getName() + " with his " + attacker.getLastUsedWeapon().getName() + ".";
        Weapon dropped = receiver.getOneWeapon();
        if(dropped.getValue() != 0 && contestants.size() != 2){
            attacker.addWeapon(dropped);
            if(dropped.getName().equals("throwing knives")){ //hard coding
                msg += " " + attacker.getName() + " has taken  the " + dropped.getName() + " from "+ receiver.getName() +"'s body.";
            } else {
                msg += " " + attacker.getName() + " has taken the " + dropped.getName() + " from "+ receiver.getName() +"'s body.";
            }
        }
        hourLog.add(msg);
        if(attacker.incKillCount()){
            hourLog.add(attacker.levelUp());
        }
    }

    private void start(List<Player> a, List<Player> b, boolean backStab){
        StringBuilder message = new StringBuilder();
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
            message.append(teamOne.get(0).getName());
            if(backStab){
                message.append(" has decided to turn on ");
            } else {
                message.append(" has attacked ");
            }
        } else {
            for(int i = 0; i< teamOne.size()-1 ; i++) {
                message.append(teamOne.get(i).getName()).append(", ");
            }
            message.append("and");
            message = new StringBuilder(message.toString().replace(", and", " and"));
            if(backStab){
                message.append(String.format(" %s have decided to turn on ", teamOne.get(teamOne.size() - 1).getName()));
            } else {
                message.append(String.format(" %s have attacked ", teamOne.get(teamOne.size() - 1).getName()));
            }

        }

        if(teamTwo.isEmpty()){
            System.out.println("Error");
        }
        FormAllegiance.playersInvolvedString(hourLog, post, teamTwo, teamOne, message.toString());
        post.addPlayers(teamTwo);
    }

    private List<Player> backStab(Player p){
        List<Player> groupOne = new ArrayList<>();
        List<Player> groupTwo = new ArrayList<>();
        boolean twoLovers = false;
        if(!p.getAllies().isEmpty()){
            if(p.hasLover()) {
                if((p.getAllies().size() ==1 && p.getAllies().get(0).equals(p.getLover()))) {
                    twoLovers = true;
                }
            }
            if(!twoLovers){
                List<Player> originalGroup = new ArrayList<>(p.getAllies());

                groupOne.add(p);
                if(p.hasLover()){
                    groupOne.add(p.getLover());
                }
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
        }
        return groupTwo;
    }

}
