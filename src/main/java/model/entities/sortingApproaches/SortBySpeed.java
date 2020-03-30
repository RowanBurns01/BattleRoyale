package model.entities.sortingApproaches;

import model.entities.Player;

import java.util.Comparator;

public class SortBySpeed implements Comparator<Player> {
    @Override
    public int compare(Player a, Player b) {
        return b.getDexterity() - a.getDexterity();
    }
}