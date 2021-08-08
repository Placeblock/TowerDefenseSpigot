package placeblock.towerdefense.instances;

import org.bukkit.scheduler.BukkitRunnable;
import placeblock.towerdefense.TowerDefense;
import placeblock.towerdefense.creators.TDEnemie;

import java.util.ArrayList;
import java.util.HashMap;

public class TDWaveInstance {

    private final ArrayList<TDEnemieInstance> enemieInstances = new ArrayList<>();
    private final TDGameInstance game;

    public TDWaveInstance(HashMap<Integer, TDEnemie> enemies, TDGameInstance game) {
        this.game = game;
        enemies.forEach((integer, enemie) -> {
            //SPAWNING ENEMIES WITH DELAY
            new BukkitRunnable() {
                @Override
                public void run() {
                    enemieInstances.add(enemie.getInstance(TDWaveInstance.this));
                }
            }.runTaskLater(TowerDefense.getInstance(), 0);
        });
    }

}
