package placeblock.towerdefense.game.inventory;

import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import placeblock.towerdefense.game.TDPlayer;

import java.util.ArrayList;
import java.util.List;

public abstract class TDInventorySection {

    protected final TDPlayer player;
    protected List<ItemStack> items = new ArrayList<>();

    public TDInventorySection(TDPlayer player) {
        this.player = player;
    }

    protected abstract void setItems();

    public abstract void scroll(PlayerItemHeldEvent e);

    public abstract void interact(PlayerInteractEvent e);

    public abstract void build(BlockPlaceEvent e);

}
