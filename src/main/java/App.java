import model.utilities.SetUp;
import model.Simulation;

public class App {

    public static void main(String[] args){

        SetUp setUp = new SetUp();
        Simulation s = setUp.getGame();

        s.configure();
        s.run();

    }

}
