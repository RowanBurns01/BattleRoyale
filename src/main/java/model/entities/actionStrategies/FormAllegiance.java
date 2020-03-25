package model.entities.actionStrategies;

import controller.Post;
import model.Simulation;
import model.entities.Player;
import model.entities.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormAllegiance extends ActionStrategies {
    Random r = new Random();
    Simulation simulation;
    List<String> hourLog;
    List<Player> contestants;
    Post post;

    @Override
    public void action(Player a, Simulation s) {

        simulation = s;
        hourLog = simulation.getHourLog();
        contestants = simulation.getContestants();
        post = simulation.getPost();

        //Get teams:

        List<Player> teamOne = new ArrayList<>(a.getAllies());
        teamOne.add(a);

        Player b = r.chooseRandomPerson(teamOne, contestants);
        if(b == null){
            return;
        } else {
            List<Player> teamTwo = new ArrayList<>(b.getAllies());
            teamTwo.add(b);

            //Everyone:
            List<Player> all = new ArrayList<>();
            all.addAll(teamOne);
            all.addAll(teamTwo);

            if(all.size() > 8){
                System.out.println("size to big");
                return;
            }

            for(Player teamOnePlayer : teamOne){
                for(Player teamTwoPlayer :teamTwo){
                    teamOnePlayer.addAlly(teamTwoPlayer);
                    teamTwoPlayer.addAlly(teamOnePlayer);
                }
            }

            String message = "An allegiance has formed between: ";
            for(Player fighter : teamOne) {
                message += fighter.getName() + ", ";
            }
            message += "and ";

            for(Player fighter : teamTwo) {
                message += fighter.getName() + ", ";
            }
            hourLog.add(message.replace(", .", "."));
            post.addPlayers(all);

        }

    }
}
