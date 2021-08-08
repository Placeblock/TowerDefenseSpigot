package placeblock.towerdefense.instances;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import placeblock.towerdefense.TowerDefense;
import placeblock.towerdefense.creators.TDEnemie;

import java.util.ArrayList;
import java.util.HashMap;

public class TDWaveInstance {

    ArrayList<TDEnemieInstance> enemieInstances = new ArrayList<>();

    public TDWaveInstance(HashMap<Integer, TDEnemie> enemies) {
        enemies.forEach((integer, enemie) -> {
            //SPAWNING ENEMIES WITH DELAY
            new BukkitRunnable() {
                @Override
                public void run() {
                    enemieInstances.add(new TDEnemieInstance(enemie));
                }
            }.runTaskLater(TowerDefense.getInstance(), 0);
        });
    }

}
