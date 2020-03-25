import model.Simulation;
import model.entities.Weapon;
import model.entities.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetUp {

    public static void main(String args[]){

        // Catch phrases
        List<Player> contestants = new ArrayList<>(Arrays.asList(
                new Player("Rowan", "Burns"),     // 1
                new Player("Jack","Paterson"),    // 2
                new Player("Rutkay", "Alici"),    // 3
                new Player("Sam", "Eames"),       // 4
                new Player("Parham", "Raoof"),    // 5
                new Player("Hugo","Treloar"),     // 6
                new Player("Jonah", "Kingery"),   // 7
                new Player("Will", "Perry"),      // 8
                new Player("Justin", "Ford"),     // 9
                new Player("Daniel", "Buffa"),    // 10
                new Player("Tom", "Williams"),    // 11
                new Player("Matt", "Sullivan"),   // 12
                new Player("Naren","Iyer"),       // 13
                new Player("Josh", "Lorschy"),    // 14
                new Player("Tim", "Hughes"),      // 15
                new Player("Will", "Lind"),       // 16
                new Player("Jay", "Kotecha"),     // 17
                new Player("Ed", "Sisson"),       // 18
                new Player("Joseph", "McGrath"),  // 19
                new Player("Ben", "Gilmore"),     // 20
                new Player("Jarrod", "Chisholm"), // 21
                new Player("Oscar", "Pursey"),    // 22
                new Player("Xavier", "Morris"),   // 23
                new Player("Marcus", "Valastro")  // 24
//                new Player("Tim", "Richmond"),    // 25
//                new Player("Seb", "Galetto"),     // 26
//                new Player("Jacob","Lerner"),     // 27
//                new Player("Mack", "Perry"),      // 28
//                new Player("Jake", "Slaytor"),    // 29
//                new Player("Nick", "Hudson"),     // 30
//                new Player("James", "Hudson"),    // 31
//                new Player("Jack", "Woods"),      // 32
//                new Player("Angus", "Neale"),     // 33
//                new Player("Hugh", "Farrow"),     // 34
//                new Player("Hugh","MacDiarmid"),  // 35
//                new Player("John-Edward", "Sparksman"),  // 36
//                new Player("Tom", "Langford"),    // 37
//                new Player("James", "O'Neale"),   // 38
//                new Player("Josh", "Walker"),     // 39
//                new Player("Riley", "Barron"),    // 40
//                new Player("Harry", "Forsythe"),  // 41
//                new Player("Hugo", "Perchard"),   // 42
//                new Player("Henry", "Watson"),    // 43
//                new Player("Alex","Ng")           // 44

        ));

        List<Weapon> weaponry = new ArrayList<>(Arrays.asList(
                new Weapon("Ballistic Knife", 2),
                new Weapon("Throwing Axe", 3),
                new Weapon("Bow and Arrow", 4)
        ));

        Simulation game = new Simulation(contestants, weaponry);
        game.configure();
        while (true){
           game.run();
        }
    }
}
