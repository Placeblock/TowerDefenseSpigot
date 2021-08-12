package placeblock.towerdefense.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import placeblock.towerdefense.game.TDGame;
import placeblock.towerdefense.game.TDPlayer;

public class PlayerScroll implements Listener {

    @EventHandler
    public void onScroll(PlayerItemHeldEvent e) {
        for(TDGame game : TDGame.getGames()) {
            for(TDPlayer p : game.getPlayers()) {
                p.getInventoryHandler().onScroll(e);
            }
        }
    }


}
