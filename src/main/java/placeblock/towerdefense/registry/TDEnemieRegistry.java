package placeblock.towerdefense.registry;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import placeblock.towerdefense.instances.TDEnemieInstance;
import placeblock.towerdefense.TowerDefense;
import placeblock.towerdefense.creators.TDEnemie;

import java.io.File;
import java.util.HashMap;

public class TDEnemieRegistry {

    private final HashMap<String, TDEnemie> enemies = new HashMap<>();

    public TDEnemieInstance getInstance(String name) {
        if(!enemies.containsKey(name)) {
            Bukkit.getLogger().warning("Tried to get Tower " + name + " which doesnt exists");
        }
        return enemies.get(name).getInstance();
    }

    public void loadEnemies() {
        File enemiesfile = new File(TowerDefense.getInstance().getDataFolder() + "/enemies.yml");
        FileConfiguration enemies = YamlConfiguration.loadConfiguration(enemiesfile);

        for(String key : enemies.getKeys(false)) {
            loadEnemie(key, enemies.getConfigurationSection(key));
        }
    }

    private void loadEnemie(String name, ConfigurationSection section) {
        if(!section.contains("range") || !section.contains("damage") || !section.contains("cooldown")) {
            Bukkit.getLogger().warning("Tower " + name + " could not be loaded! Please check your towers.yml");
            return;
        }
        int health = section.getInt("health");
        int speed = section.getInt("speed");
        int damage = section.getInt("damage");
        enemies.put(name, new TDEnemie(health, speed, damage));
    }

}
