package model.entities.actionStrategies;

import model.entities.Player;
import model.Simulation;

public abstract class ActionStrategies {

    public abstract void action(Player p, Player second, Simulation s);
}
