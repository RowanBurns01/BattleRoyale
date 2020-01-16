import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetUp {

    public static void main(String args[]){

        Random r = new Random();
        Day d = new Day();

        List<Player> contestants = new ArrayList<>(Arrays.asList(
                new Player("Rowan", "Burns"),
                new Player("Jack","Paterson"),
                new Player("Rutkay", "Alici"),
                new Player("Sam", "Eames"),
                new Player("Parham", "Raoof"),
                new Player("Hugo","Treloar"),
                new Player("Jonah", "Kingery"),
                new Player("Autismo", "Perry"),
                new Player("Justin", "Ford"),
                new Player("Daniel", "Buffa"),
                new Player("Tom", "Williams"),
                new Player("Matt", "Sullivan"),
                new Player("Naren","Iyer"),
                new Player("Josh", "Lorschy"),
                new Player("Tim", "Hughes"),
                new Player("Will", "Lind"),
                new Player("Jay", "Kotecha"),
                new Player("Percy", "The Dog")
        ));

        List<Weapon> weaponry = new ArrayList<>(Arrays.asList(
                new Weapon("Speech Impediment", r.generateNumber(3)),
                new Weapon("Dragunov", r.generateNumber(3)),
                new Weapon("Warm Socks", r.generateNumber(3)),
                new Weapon("Serrated Frisbee", r.generateNumber(3))
        ));

        Simulation game = new Simulation(d, contestants ,weaponry);
        game.configure();
        while (true){
            game.run();
        }

    }
}
