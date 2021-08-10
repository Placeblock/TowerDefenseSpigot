package placeblock.towerdefense.game;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.EntityZombie;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import placeblock.towerdefense.TowerDefense;

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;
import java.util.UUID;

public class TDTower {
    private final TDPlayer owner;
    @Getter private final Location loc;
    private final TDGame game;
    @Getter private final EntityPlayer entity;
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

    public TDTower(String type, Location location, TDGame game, TDPlayer owner) {
        this.owner = owner;
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


        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), type);
        gameProfile.getProperties().put("textxures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYwOTA0NTY1OTE3NywKICAicHJvZmlsZUlkIiA6ICJiYzRlZGZiNWYzNmM0OGE3YWM5ZjFhMzlkYzIzZjRmOCIsCiAgInByb2ZpbGVOYW1lIiA6ICI4YWNhNjgwYjIyNDYxMzQwIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzE2YzVhZjIzYmM0YmZmZmY5NzIyZTZlN2VjYTc0MjhiOWRmY2VlYjM3Y2MxNmI1ODgyYjVhY2I2YzdkZDgwZTciCiAgICB9CiAgfQp9",
                "IEnd6B63PO/Y3ApX0NC5xD6X486/CWCO810akzufb+5DmhGELWS9QB3y8arBLuPJWc5zIIZtG88Re5QmQ9e/zGXrUS1hl+adzqJC8ghpiQ8+h217I0MFuik8kepvP01fs5IM/VO8JuB44ENAMeVaqTDC3UpSTZwDgBOAitMm0JwjwWaUKP/COehEvhI47xZEevEha/uGdUn/S7oqCHC7lLwUkkNFlKRTZ6bgo9ul7HA0HmOxvvKKaPpyxT7nXPkY8dfmYKLhhAsdRmiISRQ5tx/vU5/wLUJJGo8OqHwSABmct2hopQ+vxAQyjj+uRBpGAWESm94uNNHy/5ympyUZFXI9dCFgQKmywPMEZLnXl6Jjnbq128VcX2jazDKw3rfSh/lGoxZRh4uuB5Hpa8Co974KsOFs3FzSHJ+umTtwF6IhQZpxvP3QIAmqTUakvlmuif4hhYEpeg7LqpUKjPoE03FcGfMZc3tt6QWfC2p78gM14vxJwmrpFWkUIU7YtfAKtT+h2MI771it4cDrxIckjwwYhxnTQbbtgd0+f8X8wPWazCuLG1GGBNmM+CYFDYe5uHbk26w2wqd675yoEmLthaHr+qzE7onURtlhYjB8+ZK2unnYxU8nzFSE3+vGLHkhiLw+XoHGnqymHxlfYZLhvGu1AL4x2mZbgDaFFpxNUUE="));
        entity = new EntityPlayer(nmsServer, nmsWorld, gameProfile);
        entity.setPosition(location.getX(), location.getY(), location.getZ());

        new BukkitRunnable() {
            @Override
            public void run() {
                shoot();
            }
        }.runTaskTimer(TowerDefense.getInstance(), 0, this.cooldown);
    }

    public void shoot() {
        if(this.game.getActiveWave() == null) return;
        TreeMap<Integer, TDEnemie> enemies = new TreeMap<>();
        for(TDEnemie enemie : this.game.getEnemies()) {
            if(this.loc.distance(new Location(enemie.getEntity().getWorld().getWorld(), enemie.getEntity().locX(), enemie.getEntity().locY(), enemie.getEntity().locZ())) > this.range) continue;
            enemies.put(enemie.getDamage(), enemie);
        }
        if(enemies.size() == 0) return;
        enemies.descendingMap().get(0).damage(this);
    }

    public void remove() {
        this.entity.die();
    }


    public static void registerTower(int range, int damage, int cooldown, String name, Material helmet, Material chestplate, Material leggings, Material boots) {
        data.set(name + ".range", range);
        data.set(name + ".damage", damage);
        data.set(name + ".cooldown", cooldown);
        data.set(name + ".boots", boots.toString());
        data.set(name + ".leggings", leggings.toString());
        data.set(name + ".chestplate", chestplate.toString());
        data.set(name + ".helmet", helmet.toString());

        saveConfig();
    }

    public static void saveConfig() {
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
