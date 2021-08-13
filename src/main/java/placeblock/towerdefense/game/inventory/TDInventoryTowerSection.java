package placeblock.towerdefense.game.inventory;

import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import placeblock.towerdefense.game.TDPlayer;
import placeblock.towerdefense.game.TDTower;

public class TDInventoryTowerSection extends TDInventorySection{

    private final TDTower tower;

    public TDInventoryTowerSection(TDPlayer player, TDTower tower) {
        super(player);
        this.tower = tower;
    }

    @Override
    protected void setItems() {

    }

    @Override
    public void scroll(PlayerItemHeldEvent e) {

    }

    @Override
    public void interact(PlayerInteractEvent e) {

    }

    @Override
    public void build(BlockPlaceEvent e) {
        e.setCancelled(true);
    }
}
