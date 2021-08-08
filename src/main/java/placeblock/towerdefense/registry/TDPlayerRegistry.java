package placeblock.towerdefense.registry;

import org.bukkit.entity.Player;
import placeblock.towerdefense.TDGame;
import placeblock.towerdefense.TDPlayer;

import java.util.HashMap;

public class TDPlayerRegistry {

    HashMap<Player, TDPlayer> players = new HashMap<>();

    public void registerPlayer(Player p, TDGame game) {
        if(players.containsKey(p)) {
            unregisterPlayer(p);
        }
        players.put(p, new TDPlayer(p, game));
    }

    public void unregisterPlayer(Player p) {
        if(!players.containsKey(p)) return;
        players.get(p).getGame().removePlayer(players.get(p));
        players.remove(p);
    }


}
