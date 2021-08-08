package placeblock.towerdefense.creators;

import placeblock.towerdefense.instances.TDWaveInstance;

import java.util.HashMap;

public class TDWave {

    //SPAWNING DELAY
    private final HashMap<Integer, TDEnemie> enemies;

    public TDWave(HashMap<Integer, TDEnemie> enemies) {
        this.enemies = enemies;
    }

    public TDWaveInstance getInstance() {
        return new TDWaveInstance(this.enemies);
    }

}
