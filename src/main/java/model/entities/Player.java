package model.entities;

import model.Simulation;
import model.entities.actionStrategies.ActionStrategies;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

public class Player{

    // Stats
    private String name;
    private String lastName;
    private int level;
    private int totalHealth;
    private int health;
    private int attack;
    private int defence;
    private int dexterity;
    private int killCount;

    private List<Player> alliance;
    private Player lover;

    private Player killer;

    private List<Weapon> weapons;
    private Weapon lastUsedWeapon;
    private Random r = new Random();
    private boolean alive;
    private boolean available;

    public Player (String name, String lastName) {
        this.health = r.generateNumber(40) + 80; // 80-120
        this.totalHealth = health;
        this.attack = r.generateNumber(8) + 6;   // 6-14
        this.defence = r.generateNumber(8) + 6;  // 6-14
        this.dexterity = r.generateNumber(5) + 2;// 2-7
        this.level = 1;
        this.name = name;
        this.lastName = lastName;
        this.killCount = 0;
        this.lover = null;
        this.killer = null;
        this.alive = true;
        this.available = true;
        this.alliance = new ArrayList<>();
        this.weapons = new ArrayList<>();
    }

    public void think(ActionStrategies actionStrategies, Simulation s) {
        actionStrategies.action(this, s);
    }

    public Weapon getOneWeapon(){
        if(weapons.isEmpty()){
            lastUsedWeapon = new Weapon("bare hands", 0);
        } else {
            lastUsedWeapon = weapons.get(r.generateNumber(weapons.size()-1));
        }
        return lastUsedWeapon;
    }

    public Weapon getLastUsedWeapon(){
        return lastUsedWeapon;
    }

    public int getTotalHealth(){
        return totalHealth;
    }

    public int getHealth(){
        return health;
    }

    public int getAttack(){
        return attack;
    }

    public int getDefence(){
        return attack;
    }

    public void setDefence(float num){
        this.defence += num;
    }

    public void setAttack(float num){
        this.attack += num;
    }

    public void setDexterity(int num){
        this.dexterity += num;
    }

    public int getLevel(){
        return level;
    }

    public void levelUp(int health, int defence, int attack){
        this.totalHealth += health;
        this.defence = defence;
        this.attack = attack;
        this.level ++;
        this.health += health;
    }

    public boolean attack(Player receiver) {
        float damage = ((((float) level)*2/5)+1) * ((((float) attack) + getOneWeapon().getValue())/(((float)defence)/10));
        int rounded = Math.round(damage);
        //dodge
        if(receiver.getDexterity() > r.generateNumber(100)){
            return false;
        }
        return receiver.setHP(rounded);
    }

    public int getDexterity(){
        return this.dexterity;
    }

    public boolean setHP(int damage){
        this.health -= damage;
        if(health<= 0){
            health = 0;
            return true;
        }
        return false;
    }

    public List<Weapon> getWeapons(){
        return weapons;
    }

    public void removeWeapon(Weapon w){
        this.weapons.remove(w);
    }

    public void addWeapon(Weapon w){
        this.weapons.add(w);
    }

    public void setAvailable(){ this.available = true; }

    public void setUnavailable(){ this.available = false; }

    public boolean getAvailability() { return this.available; }

    public boolean equals(Player p) {
        return p.getFullName() == name + " " + lastName;
    }

    public void setLover(Player p){
        this.lover = p;
    }

    public Player getLover(){
        return this.lover;
    }

    public boolean hasLover() {
        return ( this.getLover() != null);
    }

    public Player getKiller() { return this.killer; }

    public void setKiller(Player k) {this.killer = k; }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Boolean getAlive(){
        return this.alive;
    }

    public boolean checkIfAlly(Player p) {
        return alliance.contains(p);
    }

    public void removeAlly(Player p){
        this.getAllies().remove(p);
    }

    public Player chooseRandomAlly(){
        return alliance.get(r.generateNumber(alliance.size()-1));
    }

    public boolean hasAnAlly(){
        return !this.getAllies().isEmpty();
    }

    public void addAlly(Player p) {
        this.alliance.add(p);
    }

    public void removeAlliesAsIsDead(){
        for(Player p: alliance){
            p.removeAlly(this);
        }
        this.getAllies().clear();
    }

    public List<Player> getAllies() {
        return this.alliance;
    }

    public boolean incKillCount() {
        this.killCount++;
        if(killCount == 3|| killCount == 6|| killCount == 9){
            return true;
        }
        return false;
    }

    public void decKillCount() {this.killCount--;}

    public int getKillCount() {
        return this.killCount;
    }

    public String getImagePath(){
        return name + lastName + ".jpg";
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return name + " " + lastName;
    }

}
