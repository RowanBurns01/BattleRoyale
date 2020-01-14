import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetUp {

    public static void main(String args[]){

        Random r = new Random();
        Day d = new Day();

        List<Player> contestants = new ArrayList<>(Arrays.asList(
                new Player("Rowan", "img"),
                new Player("Jack","img"),
                new Player("Rutkay", "img"),
                new Player("Sam", "img"),
                new Player("Parham", "img"),
                new Player("Hugo","img"),
                new Player("Jonah", "img"),
                new Player("Will_Perry", "img"),
                new Player("Justin", "img"),
                new Player("Buffa", "img"),
                new Player("Twills", "img"),
                new Player("Matt_Sullivan", "img"),
                new Player("Naren","img"),
                new Player("Josh", "img"),
                new Player("Tim", "img"),
                new Player("Will_Lind", "img"),
                new Player("Chico", "img"),
                new Player("Percy", "img")
        ));

        List<Weapon> weaponry = new ArrayList<>(Arrays.asList(
                new Weapon("Machete", r.generateNumber(5)),
                new Weapon("Spear", r.generateNumber(5)),
                new Weapon("Club", r.generateNumber(5))
        ));

        Simulation game = new Simulation(r, d, contestants ,weaponry);
        game.partnerUp();
        game.round();

    }

}
