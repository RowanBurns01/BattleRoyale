import java.util.ArrayList;
import java.util.List;

public class Simulation {

    private Day d;
    private Action a;
    private List<String> hourLog;
    private List<Player> contestants;
    private List<Player> unavailable = new ArrayList<>();
    private List<Player> killed = new ArrayList<>();
    private Phrases p;

    public Simulation(Day d, List<Player> contestants,  List<Weapon> weaponry) {
        this.d = d;
        this.contestants = contestants;
        this.hourLog = new ArrayList<>();
        this.a = new Action(d, hourLog, contestants, weaponry, unavailable, killed);
        this.p = new Phrases();
    }

    public void hour() {
        int hourOutta180 = d.getHour()*180/24;
        double scaleBy = Math.sin(Math.toRadians(hourOutta180));

        int hourOutta360 = d.getHour()*360/24;
        double scaleBySuicide = Math.cos(Math.toRadians(hourOutta360));

        // Fight Action
        boolean noFight = false;
        for(Player p : contestants){
            if(0.1*scaleBy/contestants.size() > Math.random() && !noFight){
                a.fight(p);
                noFight = true;
            }
        }
        // Alternate Action
        for(Player p : contestants){
            if(!unavailable.contains(p)){
                double num = Math.random();
                if( num < 0.004*scaleBy){
                    a.loot(p);
                } else if (num < 0.005*scaleBySuicide){
                    if(contestants.size() > 2) {
                        a.suicide(p);
                    }
                } else if (num < 0.0055*scaleBy){
                    a.steal(p);
                }
            }
        }
        d.nextHour();
    }

    public void singleDay(){
        p.dayText(d);
        d.nextDay();

        while(d.getHour()<24){
            hourLog.clear();
            unavailable.clear();

            hour();

            for(Player k: killed){
                contestants.remove(k);
            }



            if(!hourLog.isEmpty()){
                d.getTimeOfDay();
                for(String s : hourLog){
                    System.out.println(s);
                }
            }
            checkIfFinished();
        }
        if(contestants.size() == 2 && contestants.get(0).hasLivingPartner()){
            if(contestants.get(0).getPartner().equals(contestants.get(1))){
                System.out.println("Oh no it seems that " + contestants.get(0).getName() + " and " + contestants.get(1).getName() + " have decided to lay down their weapons.\nHomies in arms, they both eat a poisonous berry...");
                contestants.remove(0);
                contestants.remove(0);

                checkIfFinished();
            }
        }
        d.resetHour();
        p.remainingText(contestants.size(), killed.size());
        killed.clear();
    }

    public void partnerUp() {
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
        }
    }

    public void checkIfFinished(){
        if(contestants.size() == 1){
            System.out.println("\nGame Finished, " + contestants.get(0).getName() + " is victorious with " + contestants.get(0).getKillCount() + " kills.");
            System.exit(0);
        } else if (contestants.size() == 0) {
            System.out.println("\nGame Finished, there were no surviors :'(");
            System.exit(0);
        }
    }
}
