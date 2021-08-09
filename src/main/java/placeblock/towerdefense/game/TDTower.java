package placeblock.towerdefense.game;

import lombok.Getter;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import placeblock.towerdefense.TowerDefense;

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

public class TDTower {

    private final Location loc;
    private final TDGame game;
    private final NPC npc;
    private final String type;
    private final int range;
    @Getter private final int damage;
    private final int cooldown;
    private Material boots;
    private Material leggings;
    private Material chestplate;
    private Material helmet;
    private EntityType entityType;

    private final static File file = new File(TowerDefense.getInstance().getDataFolder() + "/towers.yml");
    private final static YamlConfiguration data = YamlConfiguration.loadConfiguration(file);

    public TDTower(String type, Location location, TDGame game) {
        this.loc = location;
        this.game = game;
        this.type = type;

        ConfigurationSection dataSection = data.getConfigurationSection(this.type);
        cooldown = dataSection.getInt("cooldown", 20);
        range = dataSection.getInt("range", 5);
        damage = dataSection.getInt("damage", 2);
        try {
            boots = Material.valueOf(dataSection.getString("boots", "AIR"));
        } catch (IllegalArgumentException e) {
            boots = Material.AIR;
        }
        try {
            leggings = Material.valueOf(dataSection.getString("leggings", "AIR"));
        } catch (IllegalArgumentException e) {
            leggings = Material.AIR;
        }
        try {
            chestplate = Material.valueOf(dataSection.getString("chestplate", "AIR"));
        } catch (IllegalArgumentException e) {
            chestplate = Material.AIR;
        }
        try {
            helmet = Material.valueOf(dataSection.getString("helmet", "AIR"));
        } catch (IllegalArgumentException e) {
            helmet = Material.AIR;
        }
        try {
            entityType = EntityType.valueOf(dataSection.getString("entityType", "ZOMBIE"));
        } catch (IllegalArgumentException e) {
            entityType = EntityType.ZOMBIE;
        }

        if(!entityType.isSpawnable()) entityType = EntityType.ZOMBIE;

        npc = CitizensAPI.getNPCRegistry().createNPC(entityType, type);
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.BOOTS, new ItemStack(boots, 1));
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.LEGGINGS, new ItemStack(leggings, 1));
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.CHESTPLATE, new ItemStack(chestplate, 1));
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.HELMET, new ItemStack(helmet, 1));
        npc.spawn(location);

        new BukkitRunnable() {
            @Override
            public void run() {
                shoot();
            }
        }.runTaskTimer(TowerDefense.getInstance(), 0, this.cooldown);
    }

    public Location getLocation() {
        if(!this.npc.isSpawned()) return null;
        return this.npc.getEntity().getLocation();
    }

    public void shoot() {
        if(this.game.getActiveWave() == null) return;
        if(!this.npc.isSpawned()) return;
        TreeMap<Integer, TDEnemie> enemies = new TreeMap<>();
        for(TDEnemie enemie : this.game.getEnemies()) {
            if(this.npc.getEntity().getLocation().distance(enemie.getLocation()) > this.range) continue;
            enemies.put(enemie.getDamage(), enemie);
        }
        if(enemies.size() == 0) return;
        enemies.descendingMap().get(0).damage(this);
    }


    public static void registerTower(int range, int damage, int cooldown, String name, Material helmet, Material chestplate, Material leggings, Material boots, EntityType type) {
        data.set(name + ".range", range);
        data.set(name + ".damage", damage);
        data.set(name + ".cooldown", cooldown);
        data.set(name + ".type", name);
        data.set(name + ".boots", boots.toString());
        data.set(name + ".leggings", leggings.toString());
        data.set(name + ".chestplate", chestplate.toString());
        data.set(name + ".helmet", helmet.toString());
        data.set(name + ".entityType", type.toString());

        try {
            data.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean exists(String type) {
        return data.contains(type);
    }
}
