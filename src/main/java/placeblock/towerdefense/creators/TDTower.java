package placeblock.towerdefense.creators;


import lombok.AllArgsConstructor;
import org.bukkit.Location;
import placeblock.towerdefense.instances.TDGameInstance;
import placeblock.towerdefense.instances.TDTowerInstance;

@AllArgsConstructor
public class TDTower {

    private final int range;
    private final double damage;
    private final int cooldown;
    private final String name;

    public TDTowerInstance getInstance(Location loc, TDGameInstance game) {
        return new TDTowerInstance(this, loc, game);
    }

}
