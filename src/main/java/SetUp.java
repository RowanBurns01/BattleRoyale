import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetUp {

    public static void main(String args[]){

        Random r = new Random();

        List<Player> contestants = new ArrayList<>(Arrays.asList(
                new Player("Rowan", "Burns"),
                new Player("Jack","Paterson"),
                new Player("Rutkay", "Alici"),
                new Player("Sam", "Eames"),
                new Player("Parham", "Raoof"),
                new Player("Hugo","Treloar"),
                new Player("Jonah", "Kingery"),
                new Player("Will", "Perry"),
                new Player("Justin", "Ford"),
                new Player("Daniel", "Buffa"),
                new Player("Tom", "Williams"),
                new Player("Matt", "Sullivan"),
                new Player("Naren","Iyer"),
                new Player("Josh", "Lorschy"),
                new Player("Tim", "Hughes"),
                new Player("Will", "Lind"),
                new Player("Jay", "Kotecha"),
                new Player("Percy", "TheDog")
        ));

        List<Weapon> weaponry = new ArrayList<>(Arrays.asList(
                new Weapon("Ballistic Knife", r.generateNumber(5)),
                new Weapon("Serrated Frisbee", r.generateNumber(5)),
                new Weapon("Poison Dart Frog", r.generateNumber(5))
        ));

        Simulation game = new Simulation(contestants, weaponry);
        game.configure();
        while (true){
           game.run();
        }
    }
}
