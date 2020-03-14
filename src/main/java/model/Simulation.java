package model;

import controller.Day;
import controller.Post;
import model.entities.Player;
import model.entities.Random;
import model.entities.Weapon;
import model.entities.actionStrategies.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Simulation{

    private Random r;
    private Day d = new Day();
    private Post post = new Post(d);
    private boolean tokenSuicideOccured;
    private boolean firstPost;
    private List<Player> contestants;
    private List<Weapon> weaponry;
    private List<String> hourLog = new ArrayList<>();
    private List<Player> allContestants = new ArrayList<>();

    public Simulation(List<Player> contestants, List<Weapon> weaponry) {
        this.r = new Random();
        this.weaponry = weaponry;
        this.contestants = contestants;
        this.firstPost = true;
        this.tokenSuicideOccured = false;
    }

    public void hour() {

        // Prepare
        double scaleDay = Math.sin(Math.toRadians(d.getHour()*180/24));
        double scaleNight = Math.cos(Math.toRadians(d.getHour()*360/24)) + 1;

        // Fight Action
        boolean eventOccurred = false;
        String flag = "";
        Collections.shuffle(contestants);
        for(Player p : contestants){
            if(!eventOccurred){
                Player second = r.chooseRandomPerson(p, contestants);

                if( Math.random() < 0.25 * scaleDay/contestants.size()){
                    ActionStrategies encounter = new Encounter();
                    p.think(encounter, second, this);
                    eventOccurred = true;
                    flag = "death";
//                } else if(Math.random() < 0.25 * scaleDay/contestants.size()){
//                    ActionStrategies duel = new Duel();
//                    p.think(duel, second, this);
//                    eventOccurred = true;
//                    flag = "death";
                } else if( Math.random() < 0.01 * scaleDay){
                    ActionStrategies loot = new Loot();
                    p.think(loot, p, this);
                    eventOccurred = true;
                    flag = "item";
                } else if (Math.random() < 0.003 * scaleNight){
                    if(contestants.size() > 2 && !tokenSuicideOccured) {
                        tokenSuicideOccured = true;
                        ActionStrategies accidentalDeath = new AccidentalDeath();
                        p.think(accidentalDeath, p, this);
                        eventOccurred = true;
                        flag = "death";
                    }
                } else if (Math.random() < 0.001 * scaleDay){
                    ActionStrategies steal = new Steal();
                    p.think(steal, second, this);
                    eventOccurred = true;
                    flag = "item";
                } else if (Math.random() < 0.001){
                    ActionStrategies fallInLove = new FallInLove();
                    p.think( fallInLove, second, this);
                    eventOccurred = true;
                    flag = "love";
                }
            }
        }

        // Remove dead
        for(Player k: allContestants){
            if(!k.getAlive()){
                contestants.remove(k);
            }
        }
        facebookPost(flag);
        checkIfFinished();
        d.nextHour();
        hourLog.clear();
        for(Player p: contestants){
            p.setAvailable();
        }
    }

    public void facebookPost(String flag) {
        if(!hourLog.isEmpty()){
            String msg = "";
            if(firstPost){
                msg += "\nDay " + d.getDay();
                firstPost = false;
            }
            msg += "\n" + d.getTimeOfDay();
            for(String s : hourLog){
                msg += "\n" + (s);
            }
            if(flag == "death" && !(contestants.size() <= 1)){
                msg += "\nThere are " + contestants.size() + " tributes remaining...";
            }
            post.setFlag(flag);
            post.combinePictures();
            post.makePost(msg);
            post.clear();
        }
    }

    public void run(){

        firstPost = true;

        // arena.Day Loop
        while(d.getHour()<24){
            hour();
        }

        // Reset arena.Day
        d.resetHour();
        d.nextDay();
    }

    public void configure() {
        String msg = "\nWelcome to the first annual Hunger Games!\nYou have been chosen to represent your district.\nThis is a fight to the death, there will only be one survivor...\n\nThe tributes from each district are:";
        int count = 1;
        Collections.shuffle(contestants);
        for (Player p: contestants){
            while(!p.hasAnAlly()){
                Player partner = r.chooseRandomPerson(p, contestants);
                if(!partner.hasAnAlly()){
                    p.addAlly(partner);
                    msg += "\nDistrict "+ count+ ": " + p.getFullName() + " & " + p.getAllies().get(0).getFullName() + ".";
                    count ++;
                    partner.addAlly(p);
                }
            }
            allContestants.add(p);
        }
        post.makeTextPost(msg + "\n\nMay the odds be ever in your favour.");
    }

    public String stats(){
        List<Player> ordered = new ArrayList<>();
        int num = allContestants.size();
        String msg = "\n\nEnd Game Statistics:";
        for(int i = 0; i < num; i++){
            Player t = new Player("Dummy", "Dumb");
            t.decKillCount();
            for(Player p: allContestants){
                if(p.getKillCount() > t.getKillCount()){
                    t = p;
                }
            }
            ordered.add(t);
            allContestants.remove(t);
        }
        for(Player p: ordered){
            String status = "Dead";
            if(p.getAlive()){
                status = "Winner";
            }
            msg +="\n[" +status +"] " + p.getName()+"'s kills: "+ p.getKillCount();
        }
        return msg;
    }

    public void checkIfFinished(){

        // Finish
        if(contestants.size()<=1 || (contestants.size() == 2 && contestants.get(0).hasLover()) ){
            String msg = "";
            if(contestants.size() == 1){
                msg = "\nCongratulations " + contestants.get(0).getFullName() + "!\nYou are the victor of the first annual Hunger Games, winning with " + contestants.get(0).getKillCount();
                if(contestants.get(0).getKillCount() == 1){
                    msg += " kill.";
                } else {
                    msg += " kills.";
                }
            } else if (contestants.get(0).getLover().equals(contestants.get(1)) && contestants.get(1).getLover().equals(contestants.get(0))){
                msg = "\nThe two contestants remaining are from the same district.\nUnable to kill each other, " + contestants.get(0).getName() + " and " + contestants.get(1).getName() + " decide to lay down their weapons.\nConvinced that some divine intervention will save them, they both eat a poisonous berry.";
                msg += "\n\nThe first annual Hunger Game has finished, there were no survivors :(";
                contestants.get(0).setAlive(false);
                contestants.get(1).setAlive(false);
            }
            msg += stats();
            if(contestants.size() ==1){
                post.setFlag("death");
                post.addPlayers(contestants);
                post.combinePictures();
                post.makePost(msg);
            } else {
                post.makeTextPost(msg);
            }
            System.exit(0);
        }
    }

    public List<Weapon> getWeaponry(){
        return this.weaponry;
    }

    public List<String> getHourLog(){
        return this.hourLog;
    }

    public Post getPost(){
        return this.post;
    }

    public Day getDay(){
        return this.d;
    }

    public List<Player> getContestants() { return this.contestants; }
}
