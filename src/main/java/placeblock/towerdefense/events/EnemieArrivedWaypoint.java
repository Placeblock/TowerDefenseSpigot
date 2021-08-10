package placeblock.towerdefense.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import placeblock.towerdefense.events.custom.EnemieArrivedWaypointEvent;

public class EnemieArrivedWaypoint implements Listener {

    @EventHandler
    public void onEnemieArrived(EnemieArrivedWaypointEvent e) {
        e.getEntity().getEnemie().nextWaypoint();
    }

}
