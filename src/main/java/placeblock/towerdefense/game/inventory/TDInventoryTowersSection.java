package placeblock.towerdefense.game.inventory;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import placeblock.towerdefense.TowerDefense;
import placeblock.towerdefense.events.custom.PlayerInteractTowerEvent;
import placeblock.towerdefense.game.TDPlayer;
import placeblock.towerdefense.game.TDTower;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TDInventoryTowersSection extends TDInventorySection {

    private final int index;

    public TDInventoryTowersSection(TDPlayer player, int index) {
        super(player);
        this.index = index;
        setItems();
    }

    @Override
    protected void setItems() {
        player.getP().getInventory().clear();
        List<String> towers = player.getGame().getAllowedtowers();
        for(int i = 0; i < 9; i++) {
            if (i + index < towers.size()) {
                String name = towers.get(i + index);
                System.out.println("SET INV TOWER : " + name);
                ConfigurationSection data = TDTower.getData(name);
                ItemStack item = getDataItem(Material.PLAYER_HEAD,
                        1,
                        "§b§l" + name,
                        name,
                        "§7Range: §d§l" + data.getInt("level.1.range", 5) + "B",
                        "§7Firerate: §d§l" + (Math.round((double) 20 / data.getInt("level.1.cooldown", 5) * 100) / 100) + "/s",
                        "§7Damage: §d§l" + data.getInt("level.1.damage")
                );
                item = setHeadSkin(item, TDTower.getSkinValue(name, 0));
                player.getP().getInventory().addItem(item);
            }
        }
    }

    @Override
    public void scroll(PlayerItemHeldEvent e) {
        if(e.getPreviousSlot() == 8 && e.getNewSlot() == 0) {
            e.setCancelled(true);
            if(index + 8 >= player.getGame().getAllowedtowers().size()) return;
            player.setInventorySection(new TDInventoryTowersSection(player, index + 1));
            setItems();
        }
        if(e.getPreviousSlot() == 0 && e.getNewSlot() == 8) {
            e.setCancelled(true);
            if(index <= 0) return;
            player.setInventorySection(new TDInventoryTowersSection(player, index - 1));
            setItems();
        }
    }

    @Override
    public void build(BlockPlaceEvent e) {
        e.setCancelled(true);
        System.out.println("BLOCK");
        if(!e.getItemInHand().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(TowerDefense.getInstance(), "data"), PersistentDataType.STRING)) return;
        String tower = e.getItemInHand().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(TowerDefense.getInstance(), "data"), PersistentDataType.STRING);

        if(TDTower.getData(tower) == null) return;

        if(player.getMoney() >= TDTower.getData(tower).getInt("cost", 100)) {
            System.out.println("HAS MONEY");
            player.getGame().getTowers().add(new TDTower(tower, e.getBlockAgainst().getLocation().add(0.5, 1, 0.5), player.getGame(), player));
            player.removeMoney(TDTower.getData(tower).getInt("cost", 100));
        }else {
            player.getP().sendMessage("NOT ENOUGH MONEY!");
        }
    }

    @Override
    public void interact(PlayerInteractEvent e) {

    }

    public void interactTower(PlayerInteractTowerEvent e) {
        e.getPlayer().setInventorySection(new TDInventoryTowerSection(player, e.getTower()));
    }
}
