import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Simulation {

    private Random r;
    private Day d;
    private List<Player> contestants;
    private List<Weapon> weaponry;
    private List<Player> unavailable = new ArrayList<>();
    private List<Player> killed = new ArrayList<>();


    public Simulation(Random r, Day d, List<Player> contestants,  List<Weapon> weaponry) {
        this.r = r;
        this.d = d;
        this.contestants = contestants;
        this.weaponry = weaponry;

    }

    public void partnerUp() {
        System.out.println("Welcome to the hunger games, tributes are as follows:\n");
        int count = 1;
        for (Player p: contestants){
            while(!p.hasLivingPartner()){
                Player partner = chooseRandomPerson(p, contestants);
                if(!partner.hasLivingPartner()){
                    p.setPartner(partner);
                    System.out.println("District "+ count+ " are: " + p.getName() + ", " + p.getPartner().getName());
                    count ++;
                    partner.setPartner(p);
                }
            }

        }
    }

    public void suicide(Player p) {
        System.out.println(p.getName() + " couldn't take it anymore rip.");
        killed.add(p);
    }

    public void steal(Player p){
        if(weaponry.size() < 3) {
            Weapon w = null;
            while (w == null) {
                Player two = contestants.get(r.generateNumber(contestants.size() - 1));
                for (Weapon g : two.getWeapons()) {
                    w = g;
                }
                two.removeWeapon(w);
                if(w != null) {
                    System.out.print("From " + two.getName() + "'s backpack, ");
                }
            }

            p.addWeapon(w);
        }
    }


    public void loot(Player p){
        if(!weaponry.isEmpty()) {
            int num = r.generateNumber(weaponry.size() - 1);
            Weapon w = weaponry.get(num);
            weaponry.remove(w);
            p.addWeapon(w);

        }
    }

    public void alternateAction(Player p) {
        double num = Math.random();
        if( num < 0.1){
            loot(p);
        } else if (num > 0.99){
            suicide(p);
        } else if (num < 0.15){
            steal(p);
        }
    }

    public void round(){
        System.out.println("\nDay " +d.getDay());
        d.nextDay();

        unavailable.clear();
        killed.clear();

        Double fightChance = 1.0/contestants.size();
        for(Player p : contestants){
            if(fightChance > Math.random()){
                fight(p);
            }
        }
        for(Player p1 : contestants){
            if(!unavailable.contains(p1)){
                alternateAction(p1);
            }
        }

        for(Player k: killed){
            contestants.remove(k);
        }

        if(contestants.size() == 1){
            System.out.println("Game Finished, " + contestants.get(0).getName() + " is victorious with " + contestants.get(0).getKillCount() + " kills.");
        } else if (contestants.size() == 0){
            System.out.println("Game Finished, no one is victorious.");
        } else {
            System.out.println(contestants.size() + " tributes remain...");
            round();
        }
    }

    public void isNull(Object o) {
        System.out.println( o == null);
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


    public void fight(Player p){

        Player opponent = chooseRandomPerson(p, contestants);
        if(opponent != null) {
            List<Player> totalPlayers = new ArrayList<>(Arrays.asList(
                    p,
                    opponent
            ));

            // Add friends
            if(p.hasLivingPartner()  && !totalPlayers.contains(p.getPartner())){
                totalPlayers.add(p.getPartner());
            }
            if(opponent.hasLivingPartner()  && !totalPlayers.contains(opponent.getPartner())){
                totalPlayers.add(opponent.getPartner());
            }

            System.out.print("A fight breaks out between: ");
            for(Player g : totalPlayers){
                System.out.print(g.getName() + " ");
            }
            System.out.println();

            for(int i = 0; i< totalPlayers.size(); i++){

                try{
                    Player one = chooseRandomPerson(totalPlayers);
                    Player two = chooseRandomPerson(one, totalPlayers);
                    int oneScore = one.weaponBonus() + r.generateNumber(10);
                    int twoScore = two.weaponBonus() + r.generateNumber(10);
//                    System.out.println(one.getName() +" "+ oneScore);
//                    System.out.println(two.getName() +" "+ twoScore);
                    if( oneScore> twoScore ){
                        System.out.println(one.getName()+ " has killed " + two.getName() + ".");
                        one.incKillCount();
                        for(Weapon w: two.getWeapons()){
                            one.addWeapon(w);
                        }
                        two.setAlive(false);
                        killed.add(two);
                        unavailable.add(two);
                    } else if(oneScore < twoScore){
                        System.out.println(two.getName()+ " has killed " + one.getName() + ".");
                        two.incKillCount();
                        for(Weapon w: one.getWeapons()){
                            two.addWeapon(w);
                        }
                        one.setAlive(false);
                        unavailable.add(one);
                        killed.add(one);

                    }
                } catch(NullPointerException e){

                }

            }
        }

    }
}
