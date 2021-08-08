package placeblock.towerdefense;

import org.bukkit.Location;
import placeblock.towerdefense.creators.TDTower;

public class TDTowerInstance {

    private final TDTower tower;
    private final Location loc;

    public TDTowerInstance(TDTower tower, Location location) {
        this.tower = tower;
        this.loc = location;
    }

}
