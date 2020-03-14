package model.entities;

public class Weapon {

    private String name;
    private int value;

    public Weapon(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getImagePath(){
        return name + ".jpg";
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

}
