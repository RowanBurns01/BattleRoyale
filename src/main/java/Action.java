import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Action {

    private Random r;
    private Day d;
    private List<Player> contestants;
    private List<Weapon> weaponry;
    private List<Player> unavailable;
    private List<Player> killed;
    private List<String> hourLog;
    private Weather w;

    public Action( Day d, List<String> hourLog, List<Player> contestants, List<Weapon> weaponry, List<Player> unavailable, List<Player> killed){
        this.hourLog = hourLog;
        this.r = new Random();
        this.d = d;
        this.w = Weather.FINE;
        this.contestants = contestants;
        this.weaponry = weaponry;
        this.unavailable = unavailable;
        this.killed = killed;
    }

    public void suicide(Player p) {
        switch(w){
            case HOT:
                hourLog.add("Due to the heat, " + p.getName() + " died from dehydration.");
                break;
            case COLD:
                hourLog.add("Unable to escape the rain, " + p.getName() + " drowned himself in the river.");
                break;
            case FINE:
                hourLog.add(p.getName() + " couldn't take it anymore rip.");
                break;
        }
        p.setAlive(false);
        killed.add(p);
    }

    public void steal(Player p){
        if(weaponry.size() < 3) {
            Weapon w = null;
            while (w == null) {
                Player two = contestants.get(r.generateNumber(contestants.size() - 1));
                if(!p.equals(two)) {
                    for (Weapon g : two.getWeapons()) {
                        w = g;
                    }
                    two.removeWeapon(w);
                    if (w != null) {
                        hourLog.add(p.getName() + " sneaks into " + two.getName() + "'s campsite and looks through his gear.");
                    }
                }
            }

            p.addWeapon(w);
            hourLog.add(p.getName() + " has acquired the " + w.getName() + ".");
        }
    }


    public void loot(Player p){
        if(!weaponry.isEmpty()) {
            int num = r.generateNumber(weaponry.size() - 1);
            Weapon w = weaponry.get(num);
            weaponry.remove(w);
            p.addWeapon(w);
            hourLog.add(p.getName() + " has acquired the " + w.getName() + ".");
        }
    }

    public void fight(Player p) {

        Player opponent = chooseRandomPerson(p, contestants);

        if (opponent != null) {
            List<Player> totalPlayers = new ArrayList<>(Arrays.asList(
                    p,
                    opponent
            ));

            // Add friends
            if (p.hasLivingPartner() && !totalPlayers.contains(p.getPartner()) && 1 == r.generateNumber(1)) {
                totalPlayers.add(p.getPartner());
            }
            if (opponent.hasLivingPartner() && !totalPlayers.contains(opponent.getPartner()) && 1 == r.generateNumber(1)) {
                totalPlayers.add(opponent.getPartner());
            }
            String message = "A fight breaks out between: ";
            for (Player g : totalPlayers) {
                message += g.getName() + ", ";
            }
            message += "ship";
            hourLog.add(message.replace(", ship","."));
            int totalKills = 0;
            for (int i = 0; i < totalPlayers.size(); i++) {

                Player one = chooseRandomPerson(totalPlayers);

                if (one != null) {
                    Player two = chooseRandomPerson(one, totalPlayers);

                    if (two != null && one.getPartner().getName() != two.getName()) {

                        Player winner = one;
                        Player loser = two;


                        if (one.weaponBonus() + r.generateNumber(10) < two.weaponBonus() + r.generateNumber(10)) {
                            winner = two;
                            loser = one;
                        }
                        if(loser.hasLivingPartner()){
                            if(loser.getInLove()){
                                hourLog.add("Just as " + loser.getName() + " was about to be killed by " + winner.getName() +", " + loser.getPartner().getName() + " sacrificed himself out of love.");
                                loser = loser.getPartner();
                            }
                        }
                        Weapon knuckles = new Weapon("knuckles", 3);
                        if(winner.getWeapons().isEmpty()){
                            winner.addWeapon(knuckles);
                        }
                        if(winner.hasLivingPartner() && totalPlayers.contains(winner.getPartner())){
                            hourLog.add(winner.getName() + " and " + winner.getPartner().getName() + " have killed " + loser.getName() + " with their " + winner.getWeapons().get(0).getName()+ ".");
                        } else {
                            hourLog.add(winner.getName() + " has killed " + loser.getName() + " with his " + winner.getWeapons().get(0).getName()+ ".");
                        }
                        winner.removeWeapon(knuckles);
                        winner.incKillCount();
                        totalKills +=1;
                        loser.setAlive(false);
                        killed.add(loser);
                        unavailable.add(loser);
                        for (Weapon w : loser.getWeapons()) {
                            winner.addWeapon(w);
                            hourLog.add(winner.getName() + " has acquired the " + w.getName() + ".");
                        }
                    }
                }
            }
            if(totalKills == 0){
                hourLog.add("Everyone escapes unharmed.");
            }
        }
    }

    public Player chooseRandomPerson(Player p, List<Player> list){
        Player person = list.get(r.generateNumber(list.size()-1));
        if(p.getPartner() != null) {
            if (!person.equals(p) && !p.getPartner().equals(person) && !unavailable.contains(person)) {
                return person;
            }
        }
        int i = 0;
        while(i <10){
            person = list.get(r.generateNumber(list.size()-1));
            if(!person.equals(p) && !unavailable.contains(person)) {
                return person;
            }
            i++;
        }
        return null;
    }

    public Player chooseRandomPerson(List<Player> list){
        Player person = list.get(r.generateNumber(list.size()-1));
        int i = 0;
        while(i <10){
            if(!unavailable.contains(person)) {
                return person;
            }
            i++;
        }
        return null;
    }

    public void fallInLove(Player p){
        if(p.hasLivingPartner() && !p.getInLove()){
            hourLog.add(p.getName() + " has fallen in love with "+ p.getPartner().getName() + ".");
            p.setInLove();
            p.getPartner().setInLove();
        }
    }

    public void randomWeather(){
        int num = r.generateNumber(5);
        if( num == 0) {
            if(w != Weather.HOT) {
                w = Weather.HOT;
                System.out.println("The sun is beating down.");
            } else {
                System.out.println("The sun continues to beat down.");
            }
        } else if( num == 1) {
            if(w != Weather.COLD){
                w = Weather.COLD;
                System.out.println("It has started to rain.");
            } else {
                System.out.println("It is still raining.");
            }
        }
    }
}
