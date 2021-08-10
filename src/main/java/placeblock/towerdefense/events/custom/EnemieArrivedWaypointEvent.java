package placeblock.towerdefense.events.custom;

import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import placeblock.towerdefense.game.TDEnemieEntity;
import placeblock.towerdefense.game.TDPlayer;
import placeblock.towerdefense.game.TDTower;

public class EnemieArrivedWaypointEvent extends Event implements Cancellable {

    @Getter private TDEnemieEntity entity;
    private boolean isCancelled;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public EnemieArrivedWaypointEvent(TDEnemieEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean isCancelled() {
        return false;
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
