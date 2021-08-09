package placeblock.towerdefense.registry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import placeblock.towerdefense.TowerDefense;
import placeblock.towerdefense.data.TDTower;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class TDTowerRegistry {

    private final HashMap<String, TDTower> towers = new HashMap<>();

    public TDTower getTower(String name) {
        return towers.get(name);
    }

    public void registerTower(int range, int damage, int cooldown, String name, Material helmet, Material chestplate, Material leggings, Material boots, EntityType type) {
        File towersfile = new File(TowerDefense.getInstance().getDataFolder() + "/towers.yml");
        FileConfiguration towers = YamlConfiguration.loadConfiguration(towersfile);

        towers.set(name + ".range", range);
        towers.set(name + ".damage", damage);
        towers.set(name + ".cooldown", cooldown);
        towers.set(name + ".name", name);
        towers.set(name + ".boots", boots.toString());
        towers.set(name + ".leggings", leggings.toString());
        towers.set(name + ".chestplate", chestplate.toString());
        towers.set(name + ".helmet", helmet.toString());
        towers.set(name + ".type", type.toString());

        this.towers.put(name, new TDTower(range, damage, cooldown, name, type, boots, leggings, chestplate, helmet));

        try {
            towers.save(towersfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
