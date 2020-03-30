package model.utilities;

import model.Simulation;
import model.entities.Weapon;
import model.entities.Player;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetUp {

    private Simulation game;

    public SetUp() {
        List<Player> contestants = new ArrayList<>(Arrays.asList(
                new Player("Rowan", "Burns", "Andrew Fitzpatrick"),
                new Player("Andrew", "Fitzpatrick", "Rowan Burns"),
                new Player("Jack", "Paterson", "Jay Kotecha"),
                new Player("Rutkay", "Alici", "Jarrod Chisholm"),
                new Player("Sam", "Eames", "Moto Moto McGrath"),
                new Player("Hugo", "Treloar", "Tim Hughes"),
                new Player("Will", "Perry", "Matt Sullivan"),
                new Player("Justin", "Ford", "Naren Iyer"),
                new Player("Daniel", "Buffa", "Ed Sisson"),
                new Player("Tom", "Williams", "Josh Lorschy"),
                new Player("Matt", "Sullivan", "Will Perry"),
                new Player("Naren", "Iyer", "Justin Ford"),
                new Player("Josh", "Lorschy", "Tom Williams"),
                new Player("Tim", "Hughes", "Hugo Treloar"),
                new Player("William", "Lind", "Angus Neale"),
                new Player("Jay", "Kotecha", "Jack Paterson"),
                new Player("Ed", "Sisson", "Daniel Buffa"),
                new Player("Moto", "Moto McGrath", "Sam Eames"),
                new Player("Jarrod", "Chisholm", "Rutkay Alici"),
                new Player("Xavier", "Morris", "Jack Woods"),
                new Player("Marcus", "Valastro", "Mack Perry"),
                new Player("Seb", "Galetto", "Nick Hudson"),
                new Player("Mack", "Perry", "Marcus Valastro"),
                new Player("Nick", "Hudson", "Seb Galetto"),
                new Player("Jack", "Woods", "Xavier Morris"),
                new Player("Angus", "Neale", "William Lind"),
                new Player("Gavin", "Stein", "Kai Dover"),
                new Player("Jake", "Slaytor", "Tim Richmond"),
                new Player("Tim", "Richmond", "Jake Slaytor"),
                new Player("Kai", "Dover", "Gavin Stein"),
                new Player("Hamish", "McPharlap", "Cam Fleming"),
                new Player("Cam", "Fleming", "Hamish McPharlap"),
                new Player("Alex", "Reilly", "George Jensen"),
                new Player("George", "Jensen", "Alex Reilly"),
                new Player("Locke", "Howard", "Amrit William"),
                new Player("Amrit", "William", "Locke Howard"),
                new Player("Josh", "Roberts", "Zayne Teo"),
                new Player("Zayne", "Teo", "Josh Roberts"),
                new Player("Connor", "Schmidt", "Jared Tetley"),
                new Player("Jared", "Tetley", "Connor Schmidt"),
                new Player("Parham", "Raoof", "Ben Gilmore"),
                new Player("Ben", "Gilmore", "Parham Raoof"),
                new Player("Hugo", "Perchard", "Harry Forsythe"),
                new Player("Harry", "Forsythe", "Hugo Perchard"),
                new Player("Tom", "Langford", "James O'Neil"),
                new Player("James", "O'Neil", "Tom Langford"),
                new Player("Alex", "Ng", "Johnny Sins"),
                new Player("Johnny", "Sins", "Alex Ng"),
                new Player("James", "Hudson", "John Sparksman"),
                new Player("John", "Sparksman", "James Hudson")
        ));
        List<Weapon> weaponry = new ArrayList<>(Arrays.asList(
                new Weapon("bow and arrow"),
                new Weapon("sword"),
                new Weapon("spear"),
                new Weapon("throwing knives"),
                new Weapon("machete"),
                new Weapon("throwing axe"),
                new Weapon("trident")
        ));
        game = new Simulation(contestants, weaponry);
    }

    public Simulation getGame(){
        return game;
    }
}
