package model.utilities;

import model.entities.Player;
import java.util.ArrayList;
import java.util.List;

public class Random {

    public int generateNumber(int n) {
        // Ranges from 0 - n
        int min = 0;
        int range = n - min + 1;
        return (int)(Math.random() * range) + min;
    }

    public Player chooseRandomPerson(Player p, List<Player> old){
        int i = 0;
        while(i <10){
            List<Player> list = new ArrayList<>(old);
            list.remove(p);
            if(!list.isEmpty()){
                Player person = list.get(generateNumber(list.size()-1));
                if(person.getAvailability()){
                    return person;
                }
            }
            i++;
        }
        return null;
    }

    public Player chooseRandomPerson(List<Player> noGoodList, List<Player> old){
        int i = 0;
        while(i <10){
            List<Player> list = new ArrayList<>(old);
            list.removeAll(noGoodList);
            if(!list.isEmpty()){
                Player person = list.get(generateNumber(list.size()-1));
                if(person.getAvailability()){
                    return person;
                }
            }
            i++;
        }
        return null;
    }

    public Player chooseRandomPerson(List<Player> list){
        Player person = list.get(generateNumber(list.size()-1));
        int i = 0;
        while(i <10){
            if(person.getAvailability()) {
                return person;
            }
            i++;
        }
        return null;
    }
}
