package model.entities.sortingApproaches;

import model.entities.Player;

import java.util.Comparator;

public class SortByKills implements Comparator<Player> {
    @Override
    public int compare(Player a, Player b) {

        int killCount = b.getKillCount() - a.getKillCount();

        if (killCount != 0) {
            return killCount;
        } else {
            return a.getDistrict() - b.getDistrict();
        }
    }
}
