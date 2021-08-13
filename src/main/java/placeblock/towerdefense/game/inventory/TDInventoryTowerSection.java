package placeblock.towerdefense.game.inventory;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import placeblock.towerdefense.TowerDefense;
import placeblock.towerdefense.game.TDPlayer;
import placeblock.towerdefense.game.TDTower;

import java.util.Arrays;

public class TDInventoryTowerSection extends TDInventorySection{

    private final TDTower tower;

    public TDInventoryTowerSection(TDPlayer player, TDTower tower) {
        super(player);
        this.tower = tower;
    }

    @Override
    protected void setItems() {
        player.getP().getInventory().setItem(0, getDataItem(Material.LIME_CONCRETE,
                tower.getLevel(),
                "§bLevel: " + tower.getLevel(),
                "upgrade",
                "§7Klicke §a§lhier", "§7um diesen Turm", "§7zu §a§lupgraden§7!")
        );
        player.getP().getInventory().setItem(1, getDataItem(Material.LIME_CONCRETE,
                tower.getLevel(),
                "§7Verkaufen für §c§l" + tower.getSellPrice() + "$",
                "sell",
                "§7Klicke §c§lhier", "§7um diesen Turm", "§7zu §c§lverkaufen§7!")
        );
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
                player.addMoney(tower.getSellPrice());
                player.getGame().getTowers().remove(tower);
                tower.remove();
                break;
            case "upgrade":
                tower.levelUp();
                break;
            case "info":
                break;
        }
    }

    @Override
    public void build(BlockPlaceEvent e) {
        e.setCancelled(true);
    }

    public ItemStack getDataItem(Material type, Integer amount, String name, String data, String... lore) {
        ItemStack item = new ItemStack(type, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore.clone()));
        meta.getPersistentDataContainer().set(new NamespacedKey(TowerDefense.getInstance(), "data"), PersistentDataType.STRING, data);
        item.setItemMeta(meta);
        return item;
    }
}
