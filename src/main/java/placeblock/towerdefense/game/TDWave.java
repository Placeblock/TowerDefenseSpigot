package placeblock.towerdefense.game;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import placeblock.towerdefense.TowerDefense;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class TDWave {

    @Getter private final TDGame game;

    private final static File file = new File(TowerDefense.getInstance().getDataFolder() + "/wave.yml");
    private final static YamlConfiguration data = YamlConfiguration.loadConfiguration(file);

    public TDWave(String name, TDGame game) {
        this.game = game;

        ConfigurationSection wavedata = data.getConfigurationSection(name);
        for(String key : wavedata.getKeys(false)) {
            if(!StringUtils.isNumeric(key)) continue;
            Integer time = Integer.valueOf(key);
            if(time < 0) continue;

            new BukkitRunnable() {
                @Override
                public void run() {
                    for(String timeenemie : wavedata.getStringList(name + "." + time)) {
                        TDWave.this.game.getEnemies().add(new TDEnemie(timeenemie, TDWave.this));
                    }
                }
            }.runTaskLater(TowerDefense.getInstance(), time);
        }
    }

    public void removeEnemie(TDEnemie enemie) {
        this.game.getEnemies().remove(enemie);
    }


    public static void registerWave(String name, HashMap<Integer, ArrayList<String>> enemies) {
        enemies.forEach((time, timeenemies) -> {
            data.set(name + "." + time, timeenemies);
        });
    }

    public static boolean exists(String name) {
        return data.contains(name);
    }

}
