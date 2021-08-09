package placeblock.towerdefense.data;

import placeblock.towerdefense.instances.TDGameInstance;
import placeblock.towerdefense.instances.TDWaveInstance;

import java.util.HashMap;

public class TDWave {

    //SPAWNING DELAY
    private final HashMap<Integer, TDEnemie> enemies;

    public TDWave(HashMap<Integer, TDEnemie> enemies) {
        this.enemies = enemies;
    }

    public TDWaveInstance getInstance(TDGameInstance game) {
        return new TDWaveInstance(this.enemies, game);
    }

}
