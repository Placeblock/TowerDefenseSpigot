package placeblock.towerdefense.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import placeblock.towerdefense.TowerDefense;
import placeblock.towerdefense.game.TDPlayer;
import placeblock.towerdefense.game.TDTower;

public class PlayerInteract implements Listener {
    @EventHandler
    public void onInteract(org.bukkit.event.player.PlayerInteractEvent e) {
        TDPlayer player = TowerDefense.getInstance().getPlayerRegistry().getPlayer(e.getPlayer());
        if(player == null) return;
    }

}
