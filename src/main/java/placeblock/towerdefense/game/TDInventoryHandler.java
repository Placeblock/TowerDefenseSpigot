package placeblock.towerdefense.game;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import placeblock.towerdefense.TowerDefense;

import java.util.ArrayList;
import java.util.List;

public class TDInventoryHandler {
    private final TDPlayer p;
    private final ArrayList<ItemStack> items = new ArrayList<>();
    private int towersindex = 0;
    private TDInventoryState state = TDInventoryState.TOWERS;

    public TDInventoryHandler(TDPlayer p) {
        this.p = p;
        p.getP().getInventory().setHeldItemSlot(0);
        for(int i = 0; i < 9; i++) {
            items.add(new ItemStack(Material.AIR));
        }
        setTowersItems();
        setItems();
    }

    public void onScroll(PlayerItemHeldEvent e) {
        int slotfrom = e.getPreviousSlot();
        int slotto = e.getNewSlot();
        if(slotfrom == 0 && slotto == 8) {
            e.setCancelled(true);
            if(towersindex > 0) towersindex--;
            setTowersItems();
        }
        if(slotfrom == 8 && slotto == 0) {
            e.setCancelled(true);
            if(towersindex < 8) towersindex++;
            setTowersItems();
        }
        setItems();
    }

    public void onPlace(BlockPlaceEvent e) {
        e.setCancelled(true);
        if(!e.getItemInHand().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(TowerDefense.getInstance(), "tower"), PersistentDataType.STRING)) return;
        String tower = e.getItemInHand().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(TowerDefense.getInstance(), "tower"), PersistentDataType.STRING);
        p.getGame().getTowers().add(new TDTower(tower, e.getBlockAgainst().getLocation().add(0.5, 1, 0.5), p.getGame(), p));
    }

    private void setTowersItems() {
        List<String> towers = p.getGame().getAllowedtowers();
        System.out.println("SET TOWER ITEMS : " + towers);
        for(int i = 0; i < 9; i++) {
            items.set(i, new ItemStack(Material.AIR));
            if(i + towersindex < towers.size()) {
                System.out.println("NEW  INV TOWER");
                ItemStack item = new ItemStack(Material.RED_GLAZED_TERRACOTTA);
                ItemMeta meta = item.getItemMeta();
                meta.getPersistentDataContainer().set(new NamespacedKey(TowerDefense.getInstance(), "tower"), PersistentDataType.STRING, towers.get(i+towersindex));
                meta.setDisplayName(towers.get(i+towersindex));
                item.setItemMeta(meta);
                items.set(i, item);
            }
        }
    }

    private void setItems() {
        System.out.println("SET INV ITEMS : " + items.size());
        for(int i = 0; i < 9 && i < items.size(); i++) {
            p.getP().getInventory().setItem(i, items.get(i));
        }
    }

}
