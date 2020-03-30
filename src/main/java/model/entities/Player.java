package model.entities;

import model.Simulation;
import model.entities.actionStrategies.ActionStrategies;
import model.utilities.Random;

import java.util.ArrayList;
import java.util.List;

public class Player {

    // Stats
    private String name;
    private String lastName;
    private String assignedPartner;
    private int level;
    private int totalHealth;
    private int health;
    private int attack;
    private int defence;
    private int dexterity;
    private int killCount;

    private List<Player> alliance;
    private Player lover;
    private Player districtPartner;
    private int district;

    private List<Weapon> weapons;
    private Weapon lastUsedWeapon;
    private Random r = new Random();
    private boolean alive;
    private boolean available;

    public Player(String name, String lastName, String assignedPartner) {
        this.assignedPartner = assignedPartner;
        this.health = r.generateNumber(40) + 80; // 80-120
        this.attack = r.generateNumber(8) + 6;   // 6-14
        this.defence = r.generateNumber(8) + 6;  // 6-14
        this.dexterity = r.generateNumber(5) + 2;// 2-7
        this.totalHealth = health;
        this.level = 1;
        this.name = name;
        this.lastName = lastName;
        this.district = 0;
        this.killCount = 0;
        this.lover = null;
        this.alive = true;
        this.districtPartner = null;
        this.available = true;
        this.alliance = new ArrayList<>();
        this.weapons = new ArrayList<>();
    }

    public void think(ActionStrategies actionStrategies, Simulation s) {
        actionStrategies.action(this, s);
    }

    public Weapon getOneWeapon() {
        if (weapons.isEmpty()) {
            lastUsedWeapon = new Weapon("bare hands", 0);
        } else {
            lastUsedWeapon = weapons.get(r.generateNumber(weapons.size() - 1));
        }
        return lastUsedWeapon;
    }

    public String levelUp() {
        int level = this.getLevel();
        int health = r.generateNumber(this.getTotalHealth());
        int defence = 0;
        int attack = 0;
        int dexterity = 0;
        String s = String.format("%s has leveled up!", this.getName());
        switch (r.generateNumber(2)) {
            case 0:
                defence = 5 + r.generateNumber(level * 10);
                s += " He has regained health and his defence has increased.";
                break;
            case 1:
                attack = 5 + r.generateNumber(level * 10);
                s += " He has regained health and his attack has increased.";
                break;
            case 2:
                dexterity = 5 + r.generateNumber(level * 5);
                s += " He has regained health and his speed has increased.";
                break;
        }
        this.health += health;
        if (this.health > totalHealth) {
            this.totalHealth = this.health;
        }
        this.defence += defence;
        this.attack += attack;
        this.dexterity += dexterity;
        this.level++;
        return s;
    }

    public boolean attack(Player receiver) {
        float damage = ((((float) level) * 2 / 5) + 1) * ((((float) attack) + getOneWeapon().getValue()) / (((float) defence) / 10));
        int rounded = Math.round(damage);
        // Dodge
        if (receiver.getDexterity() > r.generateNumber(100)) {
            return false;
        }
        return receiver.setHP(rounded);
    }

    public boolean setHP(int damage) {
        this.health -= damage;
        if (health <= 0) {
            health = 0;
            return true;
        }
        if (health > totalHealth) {
            health = totalHealth;
        }
        return false;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public void removeWeapon(Weapon w) {
        this.weapons.remove(w);
    }

    public void addWeapon(Weapon w) {
        this.weapons.add(w);
    }

    public void setAvailable() {
        this.available = true;
    }

    public boolean getAvailability() {
        return this.available;
    }

    public boolean equals(Player p) {
        return p.getFullName().equals(this.getFullName());
    }

    public Player getLover() {
        return this.lover;
    }

    public void setLover(Player p) {
        this.lover = p;
    }

    public boolean hasLover() {
        if (this.getLover() != null) {
            return this.getLover().getAlive();
        }
        return false;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Boolean getAlive() {
        return this.alive;
    }

    private void removeAlly(Player p) {
        this.getAllies().remove(p);
    }

    public Player chooseRandomAlly() {
        return alliance.get(r.generateNumber(alliance.size() - 1));
    }

    public boolean hasAnAlly() {
        return !this.getAllies().isEmpty();
    }

    public void addAlly(Player p) {
        this.alliance.add(p);
    }

    public void removeAllAllies() {
        for (Player p : alliance) {
            // If ConcurrentModificationException here:
//            System.out.println(p.getName() + " removing " + this.getName());
            p.removeAlly(this);
        }
        this.getAllies().clear();
    }

    public List<Player> getAllies() {
        return this.alliance;
    }

    public boolean incKillCount() {
        this.killCount++;
        return killCount == 2 || killCount == 4 || killCount == 7 || killCount == 11;
    }

    public int getKillCount() {
        return this.killCount;
    }

    public String getImagePath() {
        return name + lastName + ".jpg";
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return name + " " + lastName;
    }

    public Weapon getLastUsedWeapon() {
        return lastUsedWeapon;
    }

    public int getTotalHealth() {
        return totalHealth;
    }

    public int getHealth() {
        return health;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefence() {
        return defence;
    }

    public int getLevel() {
        return level;
    }

    public int getDexterity() {
        return this.dexterity;
    }

    public int getDistrict() {
        return this.district;
    }

    public void setDistrict(int d) {
        this.district = d;
    }

    public boolean hasNoDistrictPartner() {
        return districtPartner == null;
    }

    public void setDistrictPartner(Player partner) {
        this.districtPartner = partner;
    }

    public String getAssignedPartner() {
        return this.assignedPartner;
    }

}
