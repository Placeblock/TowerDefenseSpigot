package placeblock.towerdefense.game;

import lombok.Getter;
import net.minecraft.world.entity.Entity;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import placeblock.towerdefense.TowerDefense;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TDWave {

    @Getter private final TDGame game;

    private final static File file = new File(TowerDefense.getInstance().getDataFolder() + "/wave.yml");
    private final static YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
    @Getter private final String name;

    @Getter private final ArrayList<BukkitTask> delayedspawntasks = new ArrayList<>();
    @Getter private final List<String> spawnenemies = new ArrayList<>();
    @Getter private final ArrayList<TDEnemie> enemies = new ArrayList<>();

    public TDWave(String name, TDGame game) {
        this.game = game;
        this.name = name;

        ConfigurationSection wavedata = data.getConfigurationSection(name);
        for(String key : wavedata.getKeys(false)) {
            if(!StringUtils.isNumeric(key)) continue;
            Integer time = Integer.valueOf(key);
            if(time < 0) continue;

            spawnenemies.addAll(wavedata.getStringList(key));

            delayedspawntasks.add(new BukkitRunnable() {
                @Override
                public void run() {
                System.out.println("SPAWNING: " + key);
                for(String timeenemie : wavedata.getStringList(key)) {
                    TDEnemie enemie = new TDEnemie(timeenemie, TDWave.this);
                    TDWave.this.enemies.add(enemie);
                    delayedspawntasks.remove(this);
                }
                }
            }.runTaskLater(TowerDefense.getInstance(), time));
        }
    }

    public void removeEntity(TDEnemie enemie) {
        spawnenemies.remove(enemie.getType());
        enemies.remove(enemie);
    }

    public void checkNextWave() {
        if(spawnenemies.size() == 0 && enemies.size() == 0) {
            game.getActiveWaves().remove(this);
            this.game.nextWave();
        }
    }

    public void delete() {
        for(BukkitTask task : delayedspawntasks) {
            task.cancel();
        }
        delayedspawntasks.clear();
        spawnenemies.clear();
        for(Iterator<TDEnemie> it = enemies.iterator(); it.hasNext();) {
            TDEnemie enemie = it.next();
            it.remove();
            enemie.delete();
        }
    }

    public static boolean exists(String name) {
        return data.contains(name);
    }

    public static void addTimeEnemie(String wave, Integer time, String enemie) {
        if(!exists(wave)) {
            data.createSection(wave);
        }
        if(data.getConfigurationSection(wave).contains(time.toString())) {
            List<String> enemies = data.getConfigurationSection(wave).getStringList(time.toString());
            enemies.add(enemie);
            data.getConfigurationSection(wave).set(time.toString(), enemies);
        }else {
            data.getConfigurationSection(wave).set(time.toString(), new ArrayList<>(Arrays.asList(enemie)));
        }

        saveConfig();
    }

    public static void saveConfig() {
        try {
            data.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
