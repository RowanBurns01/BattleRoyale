package model.entities.sortingApproaches;

import model.entities.Player;

import java.util.Comparator;

public class SortByDistrict implements Comparator<Player> {
    @Override
    public int compare(Player a, Player b) {
        int district = a.getDistrict() - b.getDistrict();

        if (district != 0) {
            return district;
        } else {
            return b.getKillCount() - a.getKillCount();
        }
    }
}
