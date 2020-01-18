import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player {

    private Player partner;
    private Player lover;
    private List<Weapon> weapons;
    private int killCount;
    private String name;
    private String lastName;
    private boolean alive;

    public Player (String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
        this.killCount = 0;
        this.partner = null;
        this.alive = true;
        this.lover = null;
        this.weapons = new ArrayList<Weapon>(Arrays.asList(
                new Weapon("knuckles", 0),
                new Weapon("bare hands", 0)
        ));
    }

    public String getImagePath(){
        return name + lastName + ".jpg";
    }

    public String getFullName() {
        return name +" " + lastName;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public void addWeapon(Weapon w) {
        this.weapons.add(w);
    }

    public Weapon chooseWeapon(){
        Random r = new Random();
        return weapons.get(r.generateNumber(weapons.size()-1));
    }

    public String getAlive(){
        if(alive){
            return "Alive";
        }
        return "Dead";
    }

    public void removeWeapon(Weapon w){
        this.weapons.remove(w);
    }

    public boolean equals(Player p) {
        return p.getName() == this.name;
    }

    public void setLover(Player p){
        this.lover = p;
    }

    public Player getLover(){
        return this.lover;
    }

    public int weaponBonus() {
        int total = 0;
        for(Weapon w : weapons){
            total += w.getValue();
        }
        return total;
    }

    public String getName() {
        return name;
    }

    public boolean hasLivingPartner(){
        return this.getPartner() != null && this.getPartner().isAlive();
    }

    private boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setPartner(Player p) {
        this.partner = p;
    }

    public Player getPartner() {
        return this.partner;
    }

    public void incKillCount() {
        this.killCount++;
    }

    public void decKillCount() {this.killCount--;}

    public int getKillCount() {
        return this.killCount;
    }
}
