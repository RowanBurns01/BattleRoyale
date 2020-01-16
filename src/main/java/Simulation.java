import java.util.ArrayList;
import java.util.List;

public class Simulation {

    private Day d;
    private Action a;
    private List<String> hourLog = new ArrayList<>();
    private List<Player> allContestants = new ArrayList<>();
    private List<Player> contestants;
    private List<Player> unavailable = new ArrayList<>();
    private List<Player> killed = new ArrayList<>();
    private Phrases p = new Phrases();
    private double scaleDay;
    private double scaleNight;
    private boolean tokenSuicideOccured;

    public Simulation(Day d, List<Player> contestants,  List<Weapon> weaponry) {
        this.d = d;
        this.contestants = contestants;
        this.a = new Action(d, hourLog, contestants, weaponry, unavailable, killed);
        this.tokenSuicideOccured = false;
    }

    public void hour() {

        // Prepare
        scaleDay = Math.sin(Math.toRadians(d.getHour()*180/24));
        scaleNight = Math.cos(Math.toRadians(d.getHour()*360/24)) + 1;

        // Fight Action

        boolean fightOccured = false;
        for(Player p : contestants){
            if(d.getDay()==1 && d.getHour()== 7 && !fightOccured){
                a.fight(p);
                fightOccured = true;
            }
            if(0.09*scaleDay/contestants.size() > Math.random() && !fightOccured){
                a.fight(p);
                fightOccured = true;
            }
        }

        // Alternate Action
        for(Player p : contestants){
            if(!unavailable.contains(p) & !fightOccured){
                double num = Math.random();
                if( num < 0.004*scaleDay){
                    a.loot(p);
                } else if (num < 0.0045*scaleNight){
                    if(contestants.size() > 2 && !tokenSuicideOccured) {
                        tokenSuicideOccured = true;
                        a.suicide(p);
                    }
                } else if (num < 0.005*scaleDay){
                    a.steal(p);
                } else if (num < 0.005){
                    a.fallInLove(p);
                }
            }
        }

        // Hourly Reset
        for(Player k: killed){
            contestants.remove(k);
        }
        if(!hourLog.isEmpty()){
            d.getTimeOfDay();
            for(String s : hourLog){
                System.out.println(s);
            }
            a.randomWeather();
        }
        checkIfFinished();
        d.nextHour();
        hourLog.clear();
        unavailable.clear();
    }

    public void run(){

        // New Day
        p.dayText(d);

        // Day Loop
        while(d.getHour()<24){
            hour();
        }

        // Reset Day
        d.resetHour();
        remainingText(contestants.size(), killed.size());
        killed.clear();
        d.nextDay();
    }

    public void configure() {
        p.welcomeText();
        int count = 1;
        for (Player p: contestants){
            while(!p.hasLivingPartner()){
                Player partner = a.chooseRandomPerson(p, contestants);
                if(!partner.hasLivingPartner()){
                    p.setPartner(partner);
                    System.out.println("District "+ count+ " are: " + p.getFullName() + ", " + p.getPartner().getFullName());
                    count ++;
                    partner.setPartner(p);
                }
            }
            allContestants.add(p);
        }
    }

    public void remainingText(int num, int num2) {
        if (num2 == 1) {
            hourLog.add("A single cannon shot fires, " + num + " tributes remain.");
        }else if( num2== 0){
            hourLog.add("There are no cannon shots tonight, "+ num + " tributes remain.");
        } else {
            hourLog.add("There are " + num2 + " cannon shots in the night, "+ num + " tributes remain.");
        }
    }

    public void stats(){
        List<Player> ordered = new ArrayList<>();
        int num = allContestants.size();
        System.out.println("\nEnd Game Statistics:");
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
            System.out.println(p.getName()+"'s kills: "+ p.getKillCount());
        }
    }

    public void checkIfFinished(){

        //Double Suicide
        if(contestants.size() == 2 && contestants.get(0).hasLivingPartner()){
            if(contestants.get(0).getPartner().equals(contestants.get(1))){
                System.out.println("\nOnly two contestants remain...\n" + contestants.get(0).getName() + " and " + contestants.get(1).getName() + " have decided to lay down their weapons.\nHomies in arms, they each consume a single poisonous berry.");
                contestants.remove(0);
                contestants.remove(0);
            }
        }

        // One Victor
        if(contestants.size() == 1){
            System.out.println("\nCongratulations " + contestants.get(0).getFullName() + "! You are the victor of the First Annual Hunger Games, winning with " + contestants.get(0).getKillCount() + " kills.");
            stats();
            System.exit(0);
        } else if (contestants.size() == 0) {
            System.out.println("\nThe First Annual Hunger Game is finished, there were no surviors :'(");
            stats();
            System.exit(0);
        }
    }
}
