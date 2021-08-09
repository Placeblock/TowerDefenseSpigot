package placeblock.towerdefense.instances;

import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;
import placeblock.towerdefense.TowerDefense;
import placeblock.towerdefense.data.TDEnemie;

import java.util.ArrayList;
import java.util.HashMap;

public class TDWaveInstance {

    @Getter private final ArrayList<TDEnemieInstance> enemieInstances = new ArrayList<>();
    @Getter private final TDGameInstance game;

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

    public void removeEnemie(TDEnemieInstance enemie) {
        enemieInstances.remove(enemie);

    }

}
