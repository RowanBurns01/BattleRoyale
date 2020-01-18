import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Simulation {

    private Action a;
    private Day d = new Day();
    private Post post = new Post();
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

        boolean fightOccured = false;
        for(Player p : contestants){
            if(0.09*scaleDay/contestants.size() > Math.random() && !fightOccured){
                a.singlefight(p);
                fightOccured = true;
            } else if(0.03*scaleDay/contestants.size() > Math.random() && !fightOccured){
                a.fight(p);
                fightOccured = true;
            }
        }

        // Alternate Action
        for(Player p : contestants){
            if(!unavailable.contains(p) & !fightOccured){
                double num = Math.random();
                if( num < 0.006*scaleDay){
                    a.loot(p);
                }
                num = Math.random();
                if (num < 0.003*scaleNight){
                    if(contestants.size() > 2 && !tokenSuicideOccured) {
                        tokenSuicideOccured = true;
                        a.suicide(p);
                    }
                }
                num = Math.random();
                if (num < 0.003*scaleDay){
                    a.steal(p);
                }
                num = Math.random();
                if (num < 0.004){
                    a.fallInLove(p);
                }
            }
        }

        String flag = "";
        for(Player k: killed){
            contestants.remove(k);
            flag = "death";
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
        String msg = "\nWelcome to the 1st Annual Hunger Games!\nYou have been chosen to represent your district in a fight to death.\nThere can only be one survivor...\n\nThe tributes are as follows:";
        int count = 1;
        for (Player p: contestants){
            while(!p.hasLivingPartner()){
                Player partner = a.chooseRandomPerson(p, contestants);
                if(!partner.hasLivingPartner()){
                    p.setPartner(partner);
                    msg += "\nDistrict "+ count+ " are: " + p.getFullName() + " & " + p.getPartner().getFullName() + ".";
                    count ++;
                    partner.setPartner(p);
                }
            }
            allContestants.add(p);
        }
        post.makeTextPost(msg);
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
            msg +="\n[" +p.getAlive() +"] " + p.getName()+"'s kills: "+ p.getKillCount();
        }
        return msg;
    }

    public void checkIfFinished(){

        // Finish
        if(contestants.size()<=1 || (contestants.size() == 2 && contestants.get(0).hasLivingPartner()) ){
            Post post = new Post();
            String msg = "";
            if(contestants.size() == 1){
                post.addPlayers(contestants);
                msg = "\nCongratulations " + contestants.get(0).getFullName() + "! You are the victor of the First Annual Hunger Games, winning with " + contestants.get(0).getKillCount() + " kills.";
            } else if (contestants.get(0).getPartner().equals(contestants.get(1))){
                msg = "\nThe two contestants remaining are from the same district.\n" + contestants.get(0).getName() + " and " + contestants.get(1).getName() + " decide to lay down their weapons.\nBrothers in arms, they both eat a single poisonous berry.";
                msg += "\n\nThe First Annual Hunger Game is finished, there were no survivors :(";
                contestants.get(0).setAlive(false);
                contestants.get(1).setAlive(false);
            }
            msg += stats();
            if(contestants.size() ==1){
                post.makePost(msg);
            } else {
                post.makeTextPost(msg);
            }
            System.exit(0);
        }
    }
}
