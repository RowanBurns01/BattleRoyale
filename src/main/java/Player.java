import java.util.ArrayList;
import java.util.List;

public class Player {

    private Player partner;
    private List<Weapon> weapons;
    private int killcount;
    private String name;
    private boolean alive;
    private String image;

    public Player (String name, String image) {
        this.name = name;
        this.killcount = 0;
        this.partner = null;
        this.alive = true;
        this.image = image;
        this.weapons = new ArrayList<>();
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public void addWeapon(Weapon w) {
        this.weapons.add(w);
        System.out.println(this.getName() + " has acquired " + w.getName() + ".");
    }

    public void removeWeapon(Weapon w){
        this.weapons.remove(w);
    }

    public boolean equals(Player p) {
        return p.getName() == this.name;
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
        this.killcount ++;
    }

    public int getKillCount() {
        return this.killcount;
    }
}
