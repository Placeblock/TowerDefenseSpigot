package placeblock.towerdefense.game;

import lombok.Getter;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import placeblock.towerdefense.TowerDefense;
import placeblock.towerdefense.util.BloodParticles;

import java.io.File;
import java.io.IOException;

public class TDEnemie implements Listener {

    private final TDWave wave;
    @Getter private final TDEnemieEntity entity;
    private int pathindex = 0;
    private int health;

    private final int startHealth;
    @Getter private final int damage;
    @Getter private final double speed;
    private final String type;
    private Material boots;
    private Material leggings;
    private Material chestplate;
    private Material helmet;
    private EntityType entityType;

    private final static File file = new File(TowerDefense.getInstance().getDataFolder() + "/enemies.yml");
    private final static YamlConfiguration data = YamlConfiguration.loadConfiguration(file);

    public TDEnemie(String type, TDWave wave) {
        System.out.println("CREATED NEW ENEMIE");
        this.wave = wave;
        this.type = type;

        ConfigurationSection dataSection = data.getConfigurationSection(type);
        speed = dataSection.getDouble("speed", 1.D);
        System.out.println("SPEED: " + speed);
        damage = dataSection.getInt("damage", 1);
        startHealth = dataSection.getInt("health", 20);
        health = startHealth;
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

        entity = new TDEnemieEntity(EntityTypes.be, this.wave.getGame().getPath().get(pathindex), this);
        entity.setHealth(startHealth);
        entity.setItem(EnumItemSlot.c, new ItemStack(boots, 1));
        entity.setItem(EnumItemSlot.d, new ItemStack(leggings, 1));
        entity.setItem(EnumItemSlot.e, new ItemStack(chestplate, 1));
        entity.setItem(EnumItemSlot.f, new ItemStack(helmet, 1));
        WorldServer world = ((CraftWorld) this.wave.getGame().getPath().get(pathindex).getWorld()).getHandle();
        world.addEntity(entity);
    }

    public void damage(TDTower tower) {
        this.health -= tower.getDamage();
        if(this.health <= 0) {
            kill();
            this.wave.getGame().getEnemies().remove(this);
        }else {
            Location bloodLocation = tower.getLoc();
            if(bloodLocation == null) return;
            bloodLocation.setY(bloodLocation.getY() + 0.5);
            BloodParticles.spawnBlood(bloodLocation);
        }
    }

    public void kill() {
        this.entity.getBukkitEntity().remove();
        this.wave.killEntity(this.type, true);
    }

    public void delete() {
        this.entity.getBukkitEntity().remove();
        this.wave.killEntity(this.type, false);
    }

    public Location nextWaypoint() {
        pathindex++;
        if(pathindex >= this.wave.getGame().getPath().size()) {
            kill();
            this.wave.getGame().getEnemies().remove(this);
            this.wave.getGame().damage(this);
            return null;
        }
        return this.wave.getGame().getPath().get(pathindex);
    }

    public static void registerEnemie(int health, int speed, int damage, String name, Material boots, Material leggings, Material chestplate, Material helmet, EntityType type) {
        data.set(name + ".health", health);
        data.set(name + ".speed", speed);
        data.set(name + ".damage", damage);
        data.set(name + ".name", name);
        data.set(name + ".boots", boots.toString());
        data.set(name + ".leggings", leggings.toString());
        data.set(name + ".chestplate", chestplate.toString());
        data.set(name + ".helmet", helmet.toString());
        data.set(name + ".entityType", type.toString());

        System.out.println("REGISTERED NEW ENTITY");

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
