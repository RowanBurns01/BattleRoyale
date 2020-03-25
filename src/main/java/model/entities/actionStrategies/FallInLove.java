package model.entities.actionStrategies;

import model.entities.Player;
import controller.Post;
import model.Simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FallInLove extends ActionStrategies {

    @Override
    public void action(Player chosen, Simulation s ) {
        List<String> hourLog = s.getHourLog();
        Post post = s.getPost();
        Player lover =null;
        if(chosen.hasAnAlly()){
            lover = chosen.chooseRandomAlly();
        }

        if(chosen.getLover() == null){
            if(lover != null){
                if(lover.getLover() == null) {
                    hourLog.add(chosen.getName() + " and " + lover.getName() + " have fallen in love.");
                    chosen.setLover(lover);
                    lover.setLover(chosen);
                    post.addPlayers(new ArrayList<>(Arrays.asList(chosen, lover)));
                }
            }
        }
    }
}
