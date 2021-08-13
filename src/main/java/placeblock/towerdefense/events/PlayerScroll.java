package placeblock.towerdefense.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import placeblock.towerdefense.TowerDefense;
import placeblock.towerdefense.game.TDGame;
import placeblock.towerdefense.game.TDPlayer;

public class PlayerScroll implements Listener {

    @EventHandler
    public void onScroll(PlayerItemHeldEvent e) {
        TDPlayer player = TowerDefense.getInstance().getPlayerRegistry().getPlayer(e.getPlayer());
        if(player == null) return;
        player.getInventorySection().scroll(e);
    }


}
