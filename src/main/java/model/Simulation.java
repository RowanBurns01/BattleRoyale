package model;

import controller.Day;
import controller.Post;
import controller.Table;
import model.entities.Player;
import model.entities.Weapon;
import model.entities.actionStrategies.*;
import model.entities.sortingApproaches.SortByDistrict;
import model.entities.sortingApproaches.SortByKills;
import model.utilities.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Simulation{


    private Random r;
    private Day d = new Day();
    private Post post = new Post(d);
    private Table table = new Table();
    private ActionStrategies gift;
    private ActionStrategies accidentalDeath;
    private boolean tokenLove;
    private boolean firstPost;
    private boolean debug;
    private List<Player> diedToday;
    private List<Player> contestants;
    private List<Weapon> weaponry;
    private List<String> hourLog = new ArrayList<>();
    private List<Player> allContestants = new ArrayList<>();

    public Simulation(List<Player> contestants, List<Weapon> weaponry) {
        this.r = new Random();
        this.weaponry = weaponry;
        this.contestants = contestants;
        this.firstPost = true;
        this.tokenLove = false;
        this.debug = post.getDebug();
        this.gift = new Gift(r.generateNumber(2));
        this.accidentalDeath = new AccidentalDeath((r.generateNumber(3)));
    }

    private void hour() {

        // Prepare
        double increasingScale = (double)d.getDay()/(double)contestants.size() + 0.05;
        double scaleDay = increasingScale* Math.sin(Math.toRadians(d.getHour()*180f/24f));
        double scaleNight = increasingScale* Math.cos(Math.toRadians(d.getHour()*360f/24f));

        // Fight Action
        boolean eventOccurred = false;
        String flag = "";
        Collections.shuffle(contestants);
        for (Player p : contestants) {
            if (!eventOccurred) {
                if (Math.random() < 0.18 * scaleDay) {
                    ActionStrategies encounter = new Encounter();
                    p.think(encounter, this);
                    eventOccurred = true;
                    flag = "death";
                } else if (Math.random() < 0.01 * scaleDay && d.getDay() > 1 && contestants.size() > 2) {
                    p.think(accidentalDeath, this);
                    eventOccurred = true;
                    flag = "death";
                } else if (Math.random() < 0.2 * scaleDay && !tokenLove && d.getDay() > 1) {
                    tokenLove = true;
                    ActionStrategies fallInLove = new FallInLove();
                    p.think(fallInLove, this);
                    eventOccurred = true;
                    flag = "love";
                } else if (Math.random() < 0.05 * scaleDay && d.getDay() > 1) {
                    p.think(gift, this);
                    eventOccurred = true;
                    flag = "gift";
                } else if (Math.random() < 0.05 * scaleNight && d.getDay() > 1) {
                    ActionStrategies assassinate = new Assassinate();
                    p.think(assassinate, this);
                    eventOccurred = true;
                    flag = "death";
                }
            }
        }
        finishHour(flag);
        d.nextHour();
    }

    private void hourZero() {

        double scaleFirstHour = 2;

        // Fight Action

        while(d.getMinute() < 60){
            boolean eventOccurred = false;
            String flag = "";
            scaleFirstHour *= 0.85;
            Collections.shuffle(contestants);
            for(Player p : contestants){
                if(!eventOccurred){
                    if( Math.random() < 0.03 * scaleFirstHour){
                        ActionStrategies encounter = new Encounter();
                        p.think(encounter, this);
                        eventOccurred = true;
                        flag = "death";
                    } else if( Math.random() < 0.05* scaleFirstHour && !weaponry.isEmpty()) {
                        ActionStrategies loot = new Loot();
                        p.think(loot, this);
                        eventOccurred = true;
                        flag = "item";
                    } else if( Math.random() < 0.02 * scaleFirstHour){
                        ActionStrategies allyUp = new FormAllegiance();
                        p.think(allyUp,this);
                        eventOccurred = true;
                        flag = "allyUp";
                    }
                }
            }
            finishHour(flag);
            d.nextMinute();
        }
        d.resetMinute();
        d.nextHour();
    }

    private void finishHour(String flag){
        // Remove dead
        for(Player k: allContestants){
            if(!k.getAlive()){
                if(contestants.remove(k)) {
                    diedToday.add(k);
                }
            }
        }
        facebookPost(flag);
        checkIfFinished();

        hourLog.clear();
        for(Player p: contestants){
            p.setAvailable();
        }
    }

    private void facebookPost(String flag) {
        if (!hourLog.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            if (firstPost) {
                if (d.getDay() == 1) {
                    msg.append("\nLet the games begin.");
                }
                msg.append("\nDay ").append(d.getDay());
                firstPost = false;
            }
            msg.append("\n").append(d.getTimeOfDay());
            for (String s : hourLog) {
                msg.append("\n").append(s);
            }
            post.setFlag(flag);
            post.combinePictures();
            post.makePost(msg.toString());
            post.clear();
        }
    }

    public void run(){

        // Start Day
        diedToday = new ArrayList<>();
        firstPost = true;
        if(d.getDay() == 1){
            hourZero();
        }

        // Loop
        while(d.getHour()<24){
            hour();
        }

        // End Day
        d.resetHour();
        d.nextDay();
        dailySummary(diedToday);
        run();
    }

    public void configure() {
        StringBuilder msg = new StringBuilder("\nWelcome to Hunger Games II, Sam Strikes Back.\nYou have been chosen to represent your district in a fight to the death.\nThe battle royale will commence tomorrow at 2pm NZT (Noon AEDT), may the odds be ever in your favour.\n\nThe tributes from each district are:");
        int count = 1;
        Collections.shuffle(contestants);
        for (Player p: contestants){
            while(p.hasNoDistrictPartner()){
                Player partner = null;
                for(Player yourPartner: contestants){
                    if(yourPartner.getFullName().equals(p.getAssignedPartner())) {
                        partner = yourPartner;
                    }
                }
                assert partner != null;
                if(partner.hasNoDistrictPartner()){
                    String allies = "";
                    if(1 == r.generateNumber(1)){
                        p.addAlly(partner);
                        partner.addAlly(p);
                        allies = " [Allies]";
                    }
                    p.setDistrictPartner(partner);
                    p.setDistrict(count);
                    partner.setDistrictPartner(p);
                    partner.setDistrict(count);
                    msg.append("\nDistrict ").append(count).append(": ").append(p.getFullName()).append(" & ").append(partner.getFullName()).append(".").append(allies);
                    count ++;

                }
            }
            allContestants.add(p);
        }
        post.makeTextPost(msg.toString());
    }

    private void dailySummary(List<Player> diedToday){
        if(!diedToday.isEmpty()){
            StringBuilder msg = new StringBuilder("\nMidnight\n");
            if(diedToday.size() == 1){
                msg.append("Only a single tribute's name has been projected into the night sky:\n");
            } else {
                msg.append("The names of the fallen tributes have been projected into the sky:\n");
            }
            diedToday.sort(new SortByDistrict());
            for(int i =0; i< diedToday.size();i++){
                if(i < diedToday.size()-1){
                    if(diedToday.get(i).getDistrict() == diedToday.get(i+1).getDistrict()){
                        msg.append(String.format("District %s: %s & %s \n", diedToday.get(i).getDistrict(), diedToday.get(i).getFullName(), diedToday.get(i + 1).getFullName()));
                        i++;
                    } else {
                        msg.append(String.format("District %s: %s \n", diedToday.get(i).getDistrict(), diedToday.get(i).getFullName()));
                    }
                } else {
                    msg.append(String.format("District %s: %s \n", diedToday.get(i).getDistrict(), diedToday.get(i).getFullName()));
                }
            }
            if(contestants.size()<8){
                msg.append("Only ");
            }
            msg.append(contestants.size()).append(" tributes remain.");
            msg.append(String.format("\n\nDay %s Leaderboard:", d.getDay() - 1));

            // Post Recap and Leaderboard
            makeTable(msg.toString(),5);

            hourLog.clear();
            d.nextHour();
        }
    }

    private void makeTable(String preface, int rows){
        table.setTable(rows);
        allContestants.sort(new SortByKills());
        int i = 0;
        for(Player p: allContestants) {
            if (i == rows) {
                break;
            }
            table.addContestant(p,i);
            i++;
        }
        table.makeTable();
        post.publishBufferedImage(table.getImage());
        post.makePost(preface);
    }

    private void checkIfFinished(){

        // Finish
        if(contestants.size()<=1 || (contestants.size() == 2 && contestants.get(0).hasLover()) ){
            String msg = "";
            if(contestants.size() == 1){
                msg = "\nCongratulations " + contestants.get(0).getFullName() + "!\nYou are the victor of the second Hunger Games, winning with " + contestants.get(0).getKillCount();
                if(contestants.get(0).getKillCount() == 1){
                    msg += " kill.";
                } else {
                    msg += " kills.";
                }
            } else if (contestants.get(0).getLover().equals(contestants.get(1)) && contestants.get(1).getLover().equals(contestants.get(0))){
                msg = "\nThe two contestants remaining are in love.\nUnable to kill each other, " + contestants.get(0).getName() + " and " + contestants.get(1).getName() + " decide to lay down their weapons.\nConvinced that some divine intervention will save them, they both eat a poisonous berry.";
                msg += "\n\nThe second Hunger Games has finished, there were no survivors.";
                contestants.get(0).setAlive(false);
                contestants.get(1).setAlive(false);
            }
            d.nextMinute();
            if(contestants.size() ==1){
                post.setFlag("death");
                post.addPlayers(contestants);
                post.combinePictures();
                post.makePost(msg);
            } else {
                post.makeTextPost(msg);
            }


            // Post Leaderboard
            for(int i = 0; i < 4; i++){
                d.nextMinute();
            }
            makeTable("\nEnd Game Leaderboard:", allContestants.size());
            System.exit(0);
        }
    }

    public boolean getPublish(){
        return !debug;
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

    public List<Player> getContestants() { return this.contestants; }
}
