package placeblock.towerdefense.game;

import lombok.Getter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import placeblock.towerdefense.TowerDefense;

public class TDPlayer {

    @Getter private final Player p;
    @Getter private int money = 500;
    @Getter private final TDGame game;
    @Getter private final TDInventoryHandler inventoryHandler;
    private BukkitTask actionBarTimer;

    public TDPlayer(Player p, TDGame game) {
        this.p = p;
        this.game = game;
        this.actionBarTimer = new BukkitRunnable() {
            @Override
            public void run() {
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.AQUA + "Geld: " + money));
            }
        }.runTaskTimerAsynchronously(TowerDefense.getInstance(), 5, 5);
        this.inventoryHandler = new TDInventoryHandler(this);
    }

    public int addMoney(int money) {
        this.money += money;
        return this.money;
    }

    public int removeMoney(int money) {
        this.money -= money;
        return this.money;
    }

    public void remove() {
        this.actionBarTimer.cancel();
    }
}
