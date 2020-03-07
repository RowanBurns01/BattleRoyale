import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Simulation {

    private Action a;
    private Day d = new Day();
    private Post post = new Post(d);
    private List<String> hourLog = new ArrayList<>();
    private List<Player> allContestants = new ArrayList<>();
    private List<Player> contestants;
    private List<Player> unavailable = new ArrayList<>();
    private List<Player> killed = new ArrayList<>();
    private List<Player> listPlayers = new ArrayList<>();
    private double scaleDay;
    private double scaleNight;
    private boolean tokenSuicideOccured;
    private boolean firstPost;

    public Simulation(List<Player> contestants,  List<Weapon> weaponry) {
        this.contestants = contestants;
        this.a = new Action(d, hourLog, contestants, weaponry, unavailable, killed, listPlayers);
        this.tokenSuicideOccured = false;
        this.firstPost = true;
    }

    public void hour() {

        // Prepare
        listPlayers.clear();
        scaleDay = Math.sin(Math.toRadians(d.getHour()*180/24));
        scaleNight = Math.cos(Math.toRadians(d.getHour()*360/24)) + 1;

        // Fight Action

        boolean eventOccurred = false;
        String flag = "";
        for(Player p : contestants){
            if(!eventOccurred){
                if( Math.random() < 0.25 * scaleDay/contestants.size()){
                    a.fight(p);
                    eventOccurred = true;
                    flag = "death";
                } else if(Math.random() < 0.25 * scaleDay/contestants.size()){
                    a.singlefight(p);
                    eventOccurred = true;
                    flag = "death";
                } else if( Math.random() < 0.01 * scaleDay){
                    a.loot(p);
                    eventOccurred = true;
                    flag = "item";
                } else if (Math.random() < 0.003 * scaleNight){
                    if(contestants.size() > 2 && !tokenSuicideOccured) {
                        tokenSuicideOccured = true;
                        a.suicide(p);
                        eventOccurred = true;
                        flag = "death";
                    }
                } else if (Math.random() < 0.001 * scaleDay){
                    a.steal(p);
                    eventOccurred = true;
                    flag = "item";
                } else if (Math.random() < 0.001){
                    a.fallInLove(p);
                    eventOccurred = true;
                    flag = "love";
                }
            }
        }

        for(Player k: killed){
            contestants.remove(k);
        }

        facebookPost(flag);
        checkIfFinished();
        d.nextHour();
        killed.clear();
        hourLog.clear();
        unavailable.clear();
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
            post.addPlayers(listPlayers);
            post.combinePictures();
            post.makePost(msg);
            post.clear();
        }
    }


    public void run(){

        firstPost = true;

        // Day Loop
        while(d.getHour()<24){
            hour();
        }

        // Reset Day
        d.resetHour();
        d.nextDay();
    }

    public void configure() {
        String msg = "\nWelcome to the first annual Hunger Games!\nYou have been chosen to represent your district.\nThis is a fight to the death, there will only be one survivor...\n\nThe tributes from each district are:";
        int count = 1;
        Collections.shuffle(contestants);
        for (Player p: contestants){
            while(!p.hasLivingPartner()){
                Player partner = a.chooseRandomPerson(p, contestants);
                if(!partner.hasLivingPartner()){
                    p.setPartner(partner);
                    msg += "\nDistrict "+ count+ ": " + p.getFullName() + " & " + p.getPartner().getFullName() + ".";
                    count ++;
                    partner.setPartner(p);
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
        if(contestants.size()<=1 || (contestants.size() == 2 && contestants.get(0).hasLivingPartner()) ){
            String msg = "";
            if(contestants.size() == 1){
                msg = "\nCongratulations " + contestants.get(0).getFullName() + "!\nYou are the victor of the first annual Hunger Games, winning with " + contestants.get(0).getKillCount();
                if(contestants.get(0).getKillCount() == 1){
                    msg += " kill.";
                } else {
                    msg += " kills.";
                }
            } else if (contestants.get(0).getPartner().equals(contestants.get(1))){
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
}
