package placeblock.towerdefense.events.custom;

import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import placeblock.towerdefense.game.TDEnemie;
import placeblock.towerdefense.game.TDPlayer;
import placeblock.towerdefense.game.TDTower;

public class PlayerInteractTowerEvent extends Event implements Cancellable {

    @Getter private TDTower tower;
    @Getter private TDPlayer player;
    private boolean isCancelled;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public PlayerInteractTowerEvent(TDPlayer player, TDTower tower) {
        this.player = player;
        this.tower = tower;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
