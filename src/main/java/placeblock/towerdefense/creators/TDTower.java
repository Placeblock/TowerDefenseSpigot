package placeblock.towerdefense.creators;


import lombok.AllArgsConstructor;
import org.bukkit.Location;
import placeblock.towerdefense.instances.TDTowerInstance;

@AllArgsConstructor
public class TDTower {

    private final int range;
    private final double damage;
    private final int cooldown;

    public TDTowerInstance getInstance(Location loc) {
        return new TDTowerInstance(this, loc);
    }

}
