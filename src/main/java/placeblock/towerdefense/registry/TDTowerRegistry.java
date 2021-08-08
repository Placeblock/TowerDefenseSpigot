package placeblock.towerdefense.registry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import placeblock.towerdefense.TowerDefense;
import placeblock.towerdefense.creators.TDTower;

import java.io.File;
import java.util.HashMap;

public class TDTowerRegistry {

    private final HashMap<String, TDTower> towers = new HashMap<>();

    public TDTower getTower(String name) {
        return towers.get(name);
    }

    public void loadTowers() {
        File towersfile = new File(TowerDefense.getInstance().getDataFolder() + "/towers.yml");
        FileConfiguration towers = YamlConfiguration.loadConfiguration(towersfile);

        for(String key : towers.getKeys(false)) {
            loadTower(key, towers.getConfigurationSection(key));
        }
    }

    private void loadTower(String name, ConfigurationSection section) {
        if(!section.contains("range") || !section.contains("damage") || !section.contains("cooldown")) {
            Bukkit.getLogger().warning("Tower " + name + " could not be loaded! Please check your towers.yml");
            return;
        }
        int range = section.getInt("range");
        int damage = section.getInt("damage");
        int cooldown = section.getInt("cooldown");

        Material boots = Material.AIR;
        if(section.contains("boots")) boots = Material.valueOf(section.getString("boots"));
        Material leggings = Material.AIR;
        if(section.contains("leggings")) leggings = Material.valueOf(section.getString("leggings"));
        Material chestplate = Material.AIR;
        if(section.contains("chestplate")) chestplate = Material.valueOf(section.getString("chestplate"));
        Material helmet = Material.AIR;
        if(section.contains("helmet")) helmet = Material.valueOf(section.getString("helmet"));

        EntityType type = EntityType.valueOf(section.getString("type"));

        towers.put(name, new TDTower(range, damage, cooldown, name, type, boots, leggings, chestplate, helmet));
    }

}
