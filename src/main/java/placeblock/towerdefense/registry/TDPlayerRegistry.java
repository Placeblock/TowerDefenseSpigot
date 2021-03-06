package placeblock.towerdefense.registry;

import org.bukkit.entity.Player;
import placeblock.towerdefense.game.TDGame;
import placeblock.towerdefense.game.TDPlayer;

import java.util.HashMap;
import java.util.Iterator;

public class TDPlayerRegistry {

    HashMap<Player, TDPlayer> players = new HashMap<>();

    public TDPlayer registerPlayer(Player p, TDGame game) {
        if(players.containsKey(p)) {
            unregisterPlayer(p);
        }
        TDPlayer tdplayer = new TDPlayer(p, game);
        players.put(p, tdplayer);
        return tdplayer;
    }

    public void unregisterPlayer(Player p) {
        if(!players.containsKey(p)) return;
        players.remove(p);
    }

    public TDPlayer getPlayer(Player p) {
        return players.get(p);
    }

}
