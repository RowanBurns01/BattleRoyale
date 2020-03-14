package model.entities;

import java.util.List;

public class Random {

    public int generateNumber(int n) {
        int max = n;
        int min = 0;
        int range = max - min + 1;
        int rand = (int)(Math.random() * range) + min;
        return rand;
    }

    public Player chooseRandomPerson(Player p, List<Player> list){
        int i = 0;
        while(i <10){
            Player person = list.get(generateNumber(list.size()-1));
            if(!person.getFullName().equals(p.getFullName()) && person.getAvailability()) {
                return person;
            }
            i++;
        }
        return null;
    }

    public Player chooseRandomPerson(List<Player> noGoodList, List<Player> list){
        int i = 0;
        while(i <10){
            Player person = list.get(generateNumber(list.size()-1));
            for(Player p : noGoodList){
                if(!person.getFullName().equals(p.getFullName()) && person.getAvailability()) {
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
