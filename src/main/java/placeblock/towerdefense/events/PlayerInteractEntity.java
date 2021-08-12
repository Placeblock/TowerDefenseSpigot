package placeblock.towerdefense.events;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import placeblock.towerdefense.TowerDefense;
import placeblock.towerdefense.events.custom.PlayerInteractTowerEvent;
import placeblock.towerdefense.game.TDGame;
import placeblock.towerdefense.game.TDPlayer;
import placeblock.towerdefense.game.TDTower;

public class PlayerInteractEntity implements Listener {
    @EventHandler
    public void onInteractEntity(org.bukkit.event.player.PlayerInteractEntityEvent e) {
        TDPlayer player = TowerDefense.getInstance().getPlayerRegistry().getPlayer(e.getPlayer());
        System.out.println("INTERACT ENTITY");
        if(player == null) return;
        System.out.println("IS TD PLAYER");
        for(TDGame game : TDGame.getGames()) {
            for(TDTower tower : game.getTowers()) {
                if(tower.getEntity().getBukkitEntity().equals( ((CraftEntity) e.getRightClicked()).getHandle()) ) {
                    PlayerInteractTowerEvent event = new PlayerInteractTowerEvent(player, tower);
                    Bukkit.getPluginManager().callEvent(event);
                    System.out.println("INTERACT WITH TDENTITY");
                }
            }
        }
    }

}
