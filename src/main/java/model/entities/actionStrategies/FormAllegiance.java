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

            if(all.size() > 6){
                return;
            }

            for(Player teamOnePlayer : teamOne){
                for(Player teamTwoPlayer :teamTwo){
                    teamOnePlayer.addAlly(teamTwoPlayer);
                    teamTwoPlayer.addAlly(teamOnePlayer);
                }
            }

            String message = "";

            if(teamOne.size() == 1){
                message += teamOne.get(0).getName();
                message += " has formed an allegiance with ";
            } else {
                for(int i = 0; i< teamOne.size()-1 ; i++) {
                    message += teamOne.get(i).getName() + ", ";
                }
                message += "and";
                message = message.replace(", and", " and");
                message += String.format(" %s have formed an allegiance with ",teamOne.get(teamOne.size()-1).getName());
            }


            if(teamTwo.size() == 1){
                message += teamTwo.get(0).getName() + ".";
            } else {
                for(int i = 0; i< teamTwo.size()-1 ; i++) {
                    message += teamTwo.get(i).getName() + ", ";
                }
                message += "and";
                message = message.replace(", and", " and");
                message += String.format(" %s.",teamTwo.get(teamTwo.size()-1).getName());
            }

            hourLog.add(message);
            post.addPlayers(all);

        }

    }
}
