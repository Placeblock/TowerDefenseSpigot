package placeblock.towerdefense.game.inventory;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import placeblock.towerdefense.TowerDefense;
import placeblock.towerdefense.game.TDPlayer;
import placeblock.towerdefense.game.TDTower;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public abstract class TDInventorySection {

    private final HashMap<String, ItemStack> loadedHeads = new HashMap<>();

    protected final TDPlayer player;

    public TDInventorySection(TDPlayer player) {
        this.player = player;
    }

    protected abstract void setItems();

    public abstract void scroll(PlayerItemHeldEvent e);

    public abstract void interact(PlayerInteractEvent e);

    public abstract void build(BlockPlaceEvent e);

    public ItemStack getDataItem(Material type, Integer amount, String name, String data, String... lore) {
        ItemStack item = new ItemStack(type, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore.clone()));
        meta.getPersistentDataContainer().set(new NamespacedKey(TowerDefense.getInstance(), "data"), PersistentDataType.STRING, data);
        item.setItemMeta(meta);
        return item;
    }

    @Nonnull
    public ItemStack setHeadSkin(ItemStack head, String value) {
        if(!head.getType().equals(Material.PLAYER_HEAD)) return head;
        if(loadedHeads.containsKey(value) && loadedHeads.get(value).isSimilar(head)) return loadedHeads.get(value);
        ItemMeta meta = head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", value));
        try {
            Method mtd = meta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            mtd.setAccessible(true);
            mtd.invoke(meta, profile);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
        head.setItemMeta(meta);
        loadedHeads.put(value, head);
        return head;
    }
}
