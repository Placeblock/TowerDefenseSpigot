package placeblock.towerdefense.creators;

import lombok.AllArgsConstructor;
import placeblock.towerdefense.instances.TDEnemieInstance;
import placeblock.towerdefense.instances.TDWaveInstance;

@AllArgsConstructor
public class TDEnemie {

    private final int health;
    private final int speed;
    private final int damage;
    private final String name;

    public TDEnemieInstance getInstance(TDWaveInstance wave) {
        return new TDEnemieInstance(this, wave);
    }


}
