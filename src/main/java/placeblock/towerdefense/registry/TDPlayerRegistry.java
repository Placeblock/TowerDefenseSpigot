package placeblock.towerdefense.registry;

import org.bukkit.entity.Player;
import placeblock.towerdefense.instances.TDGameInstance;
import placeblock.towerdefense.instances.TDPlayer;

import java.util.HashMap;

public class TDPlayerRegistry {

    HashMap<Player, TDPlayer> players = new HashMap<>();

    public TDPlayer registerPlayer(Player p, TDGameInstance game) {
        if(players.containsKey(p)) {
            unregisterPlayer(p);
        }
        TDPlayer tdplayer = new TDPlayer(p, game);
        players.put(p, tdplayer);
        return tdplayer;
    }

    public void unregisterPlayer(Player p) {
        if(!players.containsKey(p)) return;
        players.get(p).getGame().removePlayer(players.get(p));
        players.remove(p);
    }

    public TDPlayer getPlayer(Player p) {
        return players.get(p);
    }


}
