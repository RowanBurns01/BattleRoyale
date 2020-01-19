import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Action {

    private Random r = new Random();
    private Day d;
    private List<Player> contestants;
    private List<Weapon> weaponry;
    private List<Player> unavailable;
    private List<Player> killed;
    private List<String> hourLog;
    private List<Player> listPlayers;

    public Action( Day d, List<String> hourLog, List<Player> contestants, List<Weapon> weaponry, List<Player> unavailable, List<Player> killed, List<Player> listPlayers){
        this.hourLog = hourLog;
        this.d = d;
        this.contestants = contestants;
        this.weaponry = weaponry;
        this.unavailable = unavailable;
        this.killed = killed;
        this.listPlayers = listPlayers;
    }

    public void suicide(Player p) {
        switch(r.generateNumber(2)){
            case 0:
                hourLog.add(p.getName() + " was unable to find water and died from dehydration.");
                break;
            case 1:
                hourLog.add(p.getName() + " has been swept away in a flood and drowned.");
                break;
            case 2:
                hourLog.add(p.getName() + " couldn't handle the pressure and took his own life.");
                break;
        }
        p.setAlive(false);
        listPlayers.add(p);
        killed.add(p);
    }

    public void steal(Player p){
        boolean stolen = false;
        int i = 0;
        while (!stolen && i < 50) {
            Player two = contestants.get(r.generateNumber(contestants.size() - 1));
            if(!p.equals(two)) {
                List<Weapon> choices = new ArrayList<>();
                for (Weapon g : two.getWeapons()) {
                    choices.add(g);
                }
                for(Weapon w : choices){
                    if(w.getValue() != 0 && !stolen) {
                        two.removeWeapon(w);
                        p.addWeapon(w);
                        listPlayers.add(p);
                        listPlayers.add(two);
                        hourLog.add(p.getName() + " has stolen the " + w.getName() + " from " + two.getName() + ".");
                        stolen = true;
                    }
                }
            }
            i ++;
        }
    }

    public void loot(Player p){
        if(!weaponry.isEmpty()) {
            int num = r.generateNumber(weaponry.size() - 1);
            Weapon w = weaponry.get(num);
            if(w.getValue() != 0) {
                weaponry.remove(w);
                p.addWeapon(w);
                listPlayers.add(p);
                hourLog.add(p.getName() + " has acquired a " + w.getName() + ".");
            }

        }
    }

    public void fight(Player p) {

        Player opponent = chooseRandomPerson(p, contestants);
        if (opponent != null) {
            List<Player> totalPlayers = new ArrayList<>(Arrays.asList(
                    p,
                    opponent
            ));

            if (p.hasLivingPartner() && !totalPlayers.contains(p.getPartner()) && 1 == r.generateNumber(1)) {
                totalPlayers.add(p.getPartner());
            }
            if (opponent.hasLivingPartner() && !totalPlayers.contains(opponent.getPartner()) && 1 == r.generateNumber(1)) {
                totalPlayers.add(opponent.getPartner());
            }

            if(totalPlayers.size() != 2){
                String message = "A fight has broken out between: ";
                for (Player g : totalPlayers) {
                    message += g.getName() + ", ";
                    listPlayers.add(g);
                }
                message += "ship";
                hourLog.add(message.replace(", ship","."));
                int totalKills = 0;
                for (int i = 0; i < totalPlayers.size(); i++) {

                    Player one = chooseRandomPerson(totalPlayers);

                    if (one != null) {

                        Player two = chooseRandomPerson(one, totalPlayers);
                        if(0 == r.generateNumber(8)){
                            hourLog.add(one.getName() + " has fled from the fight!");
                            two = null;
                            unavailable.add(one);
                        }

                        if (two != null && one.getPartner().getName() != two.getName()) {

                            Player winner = one;
                            Player loser = two;
                            int roleOne = winner.weaponBonus() + r.generateNumber(10);
                            int roleTwo = loser.weaponBonus() + r.generateNumber(10);
//                            hourLog.add(winner.getName()+" : "+roleOne);
//                            hourLog.add(loser.getName()+" : "+roleTwo);
                            if ( roleOne < roleTwo) {
                                winner = two;
                                loser = one;
                            }
                            if(loser.hasLivingPartner()){
                                if(loser.getLover() != null){
                                    hourLog.add("Just as " + loser.getName() + " was about to be killed by " + winner.getName() +", " + loser.getPartner().getName() + " sacrificed himself out of love.");
                                    loser = loser.getPartner();
                                }
                            }
                            if(winner.hasLivingPartner() && totalPlayers.contains(winner.getPartner()) && !unavailable.contains(winner.getPartner())){
                                hourLog.add(winner.getName() + " and " + winner.getPartner().getName() + " have killed " + loser.getName() + " with their " + winner.chooseWeapon().getName()+ ".");
                            } else {
                                hourLog.add(winner.getName() + " has killed " + loser.getName() + " with his " + winner.chooseWeapon().getName() + ".");
                            }

                            winner.incKillCount();
                            totalKills +=1;
                            loser.setAlive(false);
                            killed.add(loser);
                            unavailable.add(loser);
                            Weapon w = takeWeapon(winner, loser);
                            if(w!= null){
                                hourLog.add("From "+ loser.getName() +"'s dead body, " + winner.getName() + " has acquired a " + w.getName() + ".");
                            }
                        }
                    }
                }
                if(totalKills == 0){
                    hourLog.add("Everyone has escaped unharmed.");
                } else {
                    hourLog.add("The fight has finished.");
                }
            }
        }
    }

    public void singlefight(Player p) {

        Player winner = p;
        Player loser = chooseRandomPerson(p, contestants);
        if(loser != null){
            int roleOne = p.weaponBonus() + r.generateNumber(10);
            int roleTwo = loser.weaponBonus() + r.generateNumber(10);
//            hourLog.add(p.getName()+" : "+roleOne);
//            hourLog.add(loser.getName()+" : "+roleTwo);
            if ( roleOne < roleTwo) {
                winner = loser;
                loser = p;
            }

            String msg = "";
            if(loser.getLover() != null && !loser.getLover().equals(winner) && loser.getLover().getAlive()){
                msg += "Just as " + loser.getName() + " was about to be killed by " + winner.getName() +", " + loser.getLover().getName() + " sacrificed himself out of love.\n";
                Player og = loser;
                loser = og.getLover();
                og.setLover(null);
                listPlayers.add(og);
            } else if (loser.getPartner().equals(winner)) {
                msg += winner.getName() + " became paranoid that his partner "+ loser.getName() +" was going to betray him.\n";
            } else if(d.getHour() <3 || d.getHour() > 23) {
                msg += winner.getName() + " used the cover of night to sneak into " + loser.getName() + "'s campsite.\n";
            }

            hourLog.add(msg + winner.getName() + " has killed " + loser.getName() + " with his " + winner.chooseWeapon().getName() + ".");
            winner.incKillCount();
            loser.setAlive(false);
            killed.add(loser);
            unavailable.add(loser);
            Weapon w = takeWeapon(winner, loser);
            listPlayers.add(winner);
            listPlayers.add(loser);
            if(w!= null){
                hourLog.add("From "+ loser.getName() +"'s dead body, " + winner.getName() + " has acquired a " + w.getName() + ".");
            }
        }
    }

    public Weapon takeWeapon(Player winner, Player loser) {
        for (Weapon w : loser.getWeapons()) {
            if(w.getValue() != 0 && contestants.size() > 3) {
                winner.addWeapon(w);
                return w;
            }
        }
        return null;
    }

    public Player chooseRandomPerson(Player p, List<Player> list){
        int i = 0;
        while(i <10){
            Player person = list.get(r.generateNumber(list.size()-1));
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
        if(p.getLover() == null){
            Player lover = chooseRandomPerson(p, contestants);
            if(lover.getLover() == null) {
                hourLog.add(p.getName() + " and " + lover.getName() + " have fallen in love.");
                p.setLover(lover);
                lover.setLover(p);
                listPlayers.add(p);
                listPlayers.add(lover);
            }
        }
    }

}
