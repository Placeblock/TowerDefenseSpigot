package placeblock.towerdefense.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import placeblock.towerdefense.events.custom.PlayerInteractTowerEvent;
import placeblock.towerdefense.game.inventory.TDInventoryTowersSection;

public class PlayerInteractTower implements Listener {

    @EventHandler
    public void onInteractTower(PlayerInteractTowerEvent e) {
        if(e.getPlayer().getInventorySection() instanceof TDInventoryTowersSection) {
            ((TDInventoryTowersSection) e.getPlayer().getInventorySection()).interactTower(e);
        }
    }

}
