package placeblock.towerdefense.game;

import lombok.Getter;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.event.NavigationCompleteEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import placeblock.towerdefense.TowerDefense;
import placeblock.towerdefense.util.BloodParticles;

import java.io.File;
import java.io.IOException;

public class TDEnemie implements Listener {

    private final TDWave wave;
    private final NPC npc;
    private int pathindex = 1;
    private int health;

    private final int startHealth;
    @Getter private final int damage;
    private final int speed;
    private final String type;
    private Material boots;
    private Material leggings;
    private Material chestplate;
    private Material helmet;
    private EntityType entityType;

    private final static File file = new File(TowerDefense.getInstance().getDataFolder() + "/enemies.yml");
    private final static YamlConfiguration data = YamlConfiguration.loadConfiguration(file);

    public TDEnemie(String type, TDWave wave) {
        this.wave = wave;
        this.type = type;

        ConfigurationSection dataSection = data.getConfigurationSection(type);
        speed = dataSection.getInt("speed", 0);
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

        npc = CitizensAPI.getNPCRegistry().createNPC(entityType, type);
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.BOOTS, new ItemStack(boots, 1));
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.LEGGINGS, new ItemStack(leggings, 1));
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.CHESTPLATE, new ItemStack(chestplate, 1));
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.HELMET, new ItemStack(helmet, 1));
        npc.getNavigator().setTarget(wave.getGame().getPath().getLocation(pathindex));
        npc.spawn(wave.getGame().getPath().getLocation(0));

        if(npc.isSpawned()) {
            npc.getEntity().setInvulnerable(true);
            if(npc.getEntity() instanceof LivingEntity) {
                ((LivingEntity) npc.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, speed, true));
            }
        }
    }

    public void damage(TDTower tower) {
        this.health -= tower.getDamage();
        if(this.health <= 0) {
            remove();
        }else {
            Location bloodLocation = tower.getLocation();
            if(bloodLocation == null) return;
            bloodLocation.setY(bloodLocation.getY() + 0.5);
            BloodParticles.spawnBlood(bloodLocation);
        }
    }

    @EventHandler
    public void onFinish(NavigationCompleteEvent e) {
        pathindex++;
        if(wave.getGame().getPath().getLocation(pathindex) == null) {
            remove();
            this.wave.getGame().damage(this);
            return;
        }
        npc.getNavigator().setTarget(wave.getGame().getPath().getLocation(pathindex));
    }

    public void remove() {
        this.npc.destroy();
        this.wave.removeEnemie(this);
    }

    public Location getLocation() {
        if(!this.npc.isSpawned()) return null;
        return this.npc.getEntity().getLocation();
    }



    public static void registerEnemie(int health, int speed, int damage, String name, Material boots, Material leggings, Material chestplate, Material helmet) {
        data.set(name + ".health", health);
        data.set(name + ".speed", speed);
        data.set(name + ".damage", damage);
        data.set(name + ".name", name);
        data.set(name + ".boots", boots.toString());
        data.set(name + ".leggings", leggings.toString());
        data.set(name + ".chestplate", chestplate.toString());
        data.set(name + ".helmet", helmet.toString());

        try {
            data.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
