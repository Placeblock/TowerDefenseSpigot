package placeblock.towerdefense.registry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import placeblock.towerdefense.TowerDefense;
import placeblock.towerdefense.data.TDEnemie;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class TDEnemieRegistry {

    private final HashMap<String, TDEnemie> enemies = new HashMap<>();

    public TDEnemie getEnemie(String name) {
        return enemies.get(name);
    }

    public void registerEnemie(int health, int speed, int damage, String name, Material boots, Material leggings, Material chestplate, Material helmet) {
        File enemiesfile = new File(TowerDefense.getInstance().getDataFolder() + "/enemies.yml");
        FileConfiguration enemies = YamlConfiguration.loadConfiguration(enemiesfile);

        enemies.set(name + ".health", health);
        enemies.set(name + ".speed", speed);
        enemies.set(name + ".damage", damage);
        enemies.set(name + ".name", name);
        enemies.set(name + ".boots", boots.toString());
        enemies.set(name + ".leggings", leggings.toString());
        enemies.set(name + ".chestplate", chestplate.toString());
        enemies.set(name + ".helmet", helmet.toString());

        loadEnemie(name, enemies.getConfigurationSection(name));

        try {
            enemies.save(enemiesfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadEnemies() {
        File enemiesfile = new File(TowerDefense.getInstance().getDataFolder() + "/enemies.yml");
        FileConfiguration enemies = YamlConfiguration.loadConfiguration(enemiesfile);

        for(String key : enemies.getKeys(false)) {
            loadEnemie(key, enemies.getConfigurationSection(key));
        }
    }

    private void loadEnemie(String name, ConfigurationSection section) {
        if(!section.contains("health") || !section.contains("speed") || !section.contains("damage") || !section.contains("type")) {
            Bukkit.getLogger().warning("Enemie " + name + " could not be loaded! Please check your enemies.yml");
            return;
        }

        int health = section.getInt("health");
        int speed = section.getInt("speed");
        int damage = section.getInt("damage");

        Material boots = Material.AIR;
        if(section.contains("boots")) boots = Material.valueOf(section.getString("boots"));
        Material leggings = Material.AIR;
        if(section.contains("leggings")) leggings = Material.valueOf(section.getString("leggings"));
        Material chestplate = Material.AIR;
        if(section.contains("chestplate")) chestplate = Material.valueOf(section.getString("chestplate"));
        Material helmet = Material.AIR;
        if(section.contains("helmet")) helmet = Material.valueOf(section.getString("helmet"));

        EntityType type = EntityType.valueOf(section.getString("type"));

        enemies.put(name, new TDEnemie(health, speed, damage, name, type, boots, leggings, chestplate, helmet));
    }

}
