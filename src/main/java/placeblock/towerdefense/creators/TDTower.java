package placeblock.towerdefense.creators;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import placeblock.towerdefense.instances.TDGameInstance;
import placeblock.towerdefense.instances.TDTowerInstance;

@AllArgsConstructor
public class TDTower {

    @Getter private final int range;
    @Getter private final int damage;
    @Getter private final int cooldown;
    @Getter private final String name;
    @Getter private final EntityType type;
    @Getter private final Material boots;
    @Getter private final Material leggings;
    @Getter private final Material chestplate;
    @Getter private final Material helmet;

    public TDTowerInstance getInstance(Location loc, TDGameInstance game) {
        return new TDTowerInstance(this, loc, game);
    }

}
