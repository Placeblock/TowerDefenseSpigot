package placeblock.towerdefense.instances;

import org.bukkit.Location;
import placeblock.towerdefense.creators.TDTower;

public class TDTowerInstance {

    private final TDTower tower;
    private final Location loc;
    private final TDGameInstance game;

    public TDTowerInstance(TDTower tower, Location location, TDGameInstance game) {
        this.tower = tower;
        this.loc = location;
        this.game = game;
    }

}
