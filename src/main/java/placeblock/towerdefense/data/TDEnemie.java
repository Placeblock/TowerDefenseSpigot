package placeblock.towerdefense.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import placeblock.towerdefense.instances.TDEnemieInstance;
import placeblock.towerdefense.instances.TDWaveInstance;

@AllArgsConstructor
public class TDEnemie {

    @Getter private final int health;
    @Getter private final int speed;
    @Getter private final int damage;
    @Getter private final String name;
    @Getter private final EntityType type;
    @Getter private final Material boots;
    @Getter private final Material leggings;
    @Getter private final Material chestplate;
    @Getter private final Material helmet;

    public TDEnemieInstance getInstance(TDWaveInstance wave) {
        return new TDEnemieInstance(this, wave);
    }


}
