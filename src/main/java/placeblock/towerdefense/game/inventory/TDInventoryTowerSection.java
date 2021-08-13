package placeblock.towerdefense.game.inventory;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import placeblock.towerdefense.TowerDefense;
import placeblock.towerdefense.game.TDPlayer;
import placeblock.towerdefense.game.TDTower;

import java.util.Arrays;

public class TDInventoryTowerSection extends TDInventorySection{

    private final TDTower tower;

    public TDInventoryTowerSection(TDPlayer player, TDTower tower) {
        super(player);
        this.tower = tower;
        setItems();
    }

    @Override
    protected void setItems() {
        player.getP().getInventory().clear();
        ItemStack upgrade = getDataItem(Material.PLAYER_HEAD,
                tower.getLevel(),
                "§bLevel: " + tower.getLevel(),
                "upgrade",
                "§7Klicke §a§lhier", "§7um diesen Turm", "§7zu §a§lupgraden§7!");
        upgrade = setHeadSkin(upgrade, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2I0NWM1ZWI3OGRmZjZmYzQzZjdmOGUzOTg3Mjk0MTQ0MjJhOGViNmYzMTQ1MDVkZjhmZjlhMzNiZGQ2ZDEyZiJ9fX0=");
        player.getP().getInventory().setItem(0, upgrade);
        ItemStack remove = getDataItem(Material.PLAYER_HEAD,
                1,
                "§7Verkaufen für §c§l" + tower.getSellPrice() + "$",
                "sell",
                "§7Klicke §c§lhier", "§7um diesen Turm", "§7zu §c§lverkaufen§7!");
        remove = setHeadSkin(remove, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjc3YzgyZDdlNWZhZjFhNzRiZDYxMzVlYmMyY2Y4MzZmNWM0YzNkM2E1MTBlNzljOTI2NzE2YjE4MGQyYzQ3In19fQ==");
        player.getP().getInventory().setItem(8, remove);
        ItemStack home = getDataItem(Material.PLAYER_HEAD,
                1,
                "§a§lZurück",
                "close",
                "§a§lZurück §7zum §a§lHauptmenü");
        home = setHeadSkin(home, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzc0MTBjMDdiZmJiNDE0NTAwNGJmOTE4YzhkNjMwMWJkOTdjZTEzMjcwY2UxZjIyMWQ5YWFiZWUxYWZkNTJhMyJ9fX0=");
        player.getP().getInventory().setItem(4, home);
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
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.setInventorySection(new TDInventoryTowersSection(player, 0));
                    }
                }.runTask(TowerDefense.getInstance());
                tower.remove();
                break;
            case "upgrade":
                tower.levelUp();
                setItems();
                break;
            case "close":
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.setInventorySection(new TDInventoryTowersSection(player, 0));
                    }
                }.runTask(TowerDefense.getInstance());
                break;
        }
    }

    @Override
    public void build(BlockPlaceEvent e) {
        e.setCancelled(true);
    }

}
