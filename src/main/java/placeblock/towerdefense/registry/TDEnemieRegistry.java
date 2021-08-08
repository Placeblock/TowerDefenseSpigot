package placeblock.towerdefense.registry;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import placeblock.towerdefense.TowerDefense;
import placeblock.towerdefense.creators.TDEnemie;

import java.io.File;
import java.util.HashMap;

public class TDEnemieRegistry {

    private final HashMap<String, TDEnemie> enemies = new HashMap<>();

    public TDEnemie getEnemie(String name) {
        return enemies.get(name);
    }

    public void loadEnemies() {
        File enemiesfile = new File(TowerDefense.getInstance().getDataFolder() + "/enemies.yml");
        FileConfiguration enemies = YamlConfiguration.loadConfiguration(enemiesfile);

        for(String key : enemies.getKeys(false)) {
            loadEnemie(key, enemies.getConfigurationSection(key));
        }
    }

    private void loadEnemie(String name, ConfigurationSection section) {
        if(!section.contains("health") || !section.contains("speed") || !section.contains("damage")) {
            Bukkit.getLogger().warning("Enemie " + name + " could not be loaded! Please check your enemies.yml");
            return;
        }
        int health = section.getInt("health");
        int speed = section.getInt("speed");
        int damage = section.getInt("damage");
        enemies.put(name, new TDEnemie(health, speed, damage, name));
    }

}
