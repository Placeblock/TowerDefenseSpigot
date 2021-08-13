package placeblock.towerdefense.game.inventory;

import org.bukkit.NamespacedKey;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import placeblock.towerdefense.TowerDefense;
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
        if(e.getItem() == null) return;
        ItemStack item = e.getItem();
        if(!item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(TowerDefense.getInstance(), "data"), PersistentDataType.STRING)) return;
        String data = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(TowerDefense.getInstance(), "data"), PersistentDataType.STRING);
        switch(data.toLowerCase()) {
            case "sell":
                break;
            case "upgrade":
                break;
            case "info":
                break;
        }
    }

    @Override
    public void build(BlockPlaceEvent e) {
        e.setCancelled(true);
    }
}
