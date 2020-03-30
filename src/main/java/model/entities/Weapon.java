package model.entities;

import model.utilities.Random;

public class Weapon {

    private String name;
    private int value;

    public Weapon(String name) {
        Random r = new Random();
        this.name = name;
        this.value = 3 + r.generateNumber(5);
    }

    Weapon(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getImagePath() {
        return name + ".jpg";
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

}
