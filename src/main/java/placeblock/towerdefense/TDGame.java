package placeblock.towerdefense;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class TDGame {

    private final ArrayList<TDPlayer> players = new ArrayList<>();

    public TDGame(ArrayList<Player> players) {
    }

    public void removePlayer(TDPlayer p) {
        players.remove(p);
    }

}
