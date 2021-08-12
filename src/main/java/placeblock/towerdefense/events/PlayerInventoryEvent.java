package placeblock.towerdefense.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import placeblock.towerdefense.TowerDefense;

public class PlayerInventoryEvent implements Listener {

    @EventHandler
    public void onMove(InventoryDragEvent e) {
        if(!(e.getWhoClicked() instanceof Player)) return;
        if(TowerDefense.getInstance().getPlayerRegistry().getPlayer((Player) e.getWhoClicked()) != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof Player)) return;
        if(TowerDefense.getInstance().getPlayerRegistry().getPlayer((Player) e.getWhoClicked()) != null) {
            e.setCancelled(true);
        }
    }
}
