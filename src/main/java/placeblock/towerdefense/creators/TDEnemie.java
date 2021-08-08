package placeblock.towerdefense.creators;

import lombok.AllArgsConstructor;
import placeblock.towerdefense.TDEnemieInstance;

@AllArgsConstructor
public class TDEnemie {

    private final int health;
    private final int speed;
    private final int damage;

    public TDEnemieInstance getInstance() {
        return new TDEnemieInstance(this);
    }


}
