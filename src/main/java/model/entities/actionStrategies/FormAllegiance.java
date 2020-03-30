package model.entities.actionStrategies;

import controller.Post;
import model.Simulation;
import model.entities.Player;
import model.utilities.Random;
import java.util.ArrayList;
import java.util.List;

public class FormAllegiance extends ActionStrategies {
    private Random r = new Random();

    @Override
    public void action(Player a, Simulation s) {

        List<String> hourLog = s.getHourLog();
        List<Player> contestants = s.getContestants();
        Post post = s.getPost();

        //Get teams:

        List<Player> teamOne = new ArrayList<>(a.getAllies());
        teamOne.add(a);

        Player b = r.chooseRandomPerson(teamOne, contestants);
        if(b != null){
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

            StringBuilder message = new StringBuilder();

            if(teamOne.size() == 1){
                message.append(teamOne.get(0).getName());
                message.append(" has formed an alliance with ");
            } else {
                for(int i = 0; i< teamOne.size()-1 ; i++) {
                    message.append(teamOne.get(i).getName()).append(", ");
                }
                message.append("and");
                message = new StringBuilder(message.toString().replace(", and", " and"));
                message.append(String.format(" %s have formed an alliance with ", teamOne.get(teamOne.size() - 1).getName()));
            }

            playersInvolvedString(hourLog, post, teamTwo, all, message.toString());

        }

    }

    static void playersInvolvedString(List<String> hourLog, Post post, List<Player> teamTwo, List<Player> all, String message) {
        if(teamTwo.size() == 1){
            message += teamTwo.get(0).getName() + ".";
        } else {
            StringBuilder messageBuilder = new StringBuilder(message);
            for(int i = 0; i< teamTwo.size()-1 ; i++) {
                messageBuilder.append(teamTwo.get(i).getName()).append(", ");
            }
            message = messageBuilder.toString();
            message += "and";
            message = message.replace(", and", " and");
            message += String.format(" %s.",teamTwo.get(teamTwo.size()-1).getName());
        }

        hourLog.add(message);
        post.addPlayers(all);
    }
}
