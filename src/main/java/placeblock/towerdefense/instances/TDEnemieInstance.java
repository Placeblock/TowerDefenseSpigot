package placeblock.towerdefense.instances;

import placeblock.towerdefense.creators.TDEnemie;

public class TDEnemieInstance {

    private final TDEnemie enemie;
    private final TDWaveInstance wave;


    public TDEnemieInstance(TDEnemie enemie, TDWaveInstance wave) {
        this.enemie = enemie;
        this.wave = wave;
    }

}
