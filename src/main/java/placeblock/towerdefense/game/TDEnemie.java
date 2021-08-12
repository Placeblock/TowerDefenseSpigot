package placeblock.towerdefense.game;

import lombok.Getter;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import placeblock.towerdefense.TowerDefense;
import placeblock.towerdefense.util.BloodParticles;

import java.io.File;
import java.io.IOException;

public class TDEnemie implements Listener {

    private TDWave wave;
    @Getter private TDEnemieEntity entity;
    private int pathindex = 0;
    private int health;

    private final int startHealth;
    @Getter private final int damage;
    @Getter private final int speed;
    @Getter private final String type;
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
        speed = dataSection.getInt("speed", 1);
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
            System.out.println(dataSection.getString("entityType", "ZOMBIE"));
            entityType = EntityType.byString(dataSection.getString("entityType", "ZOMBIE")).orElse(EntityType.SPIDER);
            System.out.println("ENTITYTYPE: " + entityType);
        } catch (IllegalArgumentException e) {
            entityType = EntityType.ZOMBIE;
        }

        if(!entityType.canSummon()) entityType = EntityType.ZOMBIE;
        System.out.println("ENTITYTYPE: " + entityType);
        entity = new TDEnemieEntity(entityType, this.wave.getGame().getPath().get(pathindex), this);
        entity.setHealth(startHealth);
        entity.setItem(EquipmentSlot.FEET, new ItemStack(boots, 1));
        entity.setItem(EquipmentSlot.LEGS, new ItemStack(leggings, 1));
        entity.setItem(EquipmentSlot.CHEST, new ItemStack(chestplate, 1));
        entity.setItem(EquipmentSlot.HEAD, new ItemStack(helmet, 1));
        this.entity.setCustomName(new TextComponent(ChatColor.DARK_RED + type + " [" + this.health + "/" + this.startHealth + "]"));
        ServerLevel world = ((CraftWorld) this.wave.getGame().getPath().get(pathindex).getWorld()).getHandle();
        world.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    public void damage(TDTower tower) {
        this.health -= tower.getDamage();
        this.entity.setCustomName(new TextComponent(ChatColor.DARK_RED + type + " [" + this.health + "/" + this.startHealth + "]"));
        if(this.health <= 0) {
            this.wave.removeEntity(this);
            this.wave.checkNextWave();
            delete();
            return;
        }else {
            Location bloodLocation = entity.getBukkitEntity().getLocation().add(0, entity.getEyeHeight(), 0);
            if(bloodLocation == null) return;
            BloodParticles.spawnBlood(bloodLocation);
        }
    }

    public void delete() {
        this.entity.getBukkitEntity().remove();
        this.entity.remove(Entity.RemovalReason.DISCARDED);
        this.entity = null;
        this.wave = null;
    }

    public Location nextWaypoint() {
        pathindex++;
        if(pathindex >= this.wave.getGame().getPath().size()) {
            System.out.println("DELETING INCAME ENTITY");
            this.wave.removeEntity(this);
            this.wave.getGame().damage(this);
            delete();
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
