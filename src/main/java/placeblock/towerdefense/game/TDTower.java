package placeblock.towerdefense.game;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import placeblock.towerdefense.TowerDefense;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TDTower {
    private final TDPlayer owner;
    private int level = 1;
    private TDAttackType attackType = TDAttackType.NEAREST;
    @Getter private final Location loc;
    private final TDGame game;
    @Getter private ServerPlayer entity;
    private final String type;
    private int range;
    @Getter private int damage;
    private int cooldown;
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

        loadData();

        new BukkitRunnable() {
            @Override
            public void run() {
                shoot();
            }
        }.runTaskTimer(TowerDefense.getInstance(), 0, this.cooldown);
    }

    public void shoot() {
        ArrayList<TDEnemie> enemies = this.game.getEnemies();
        if(enemies.size() == 0) return;
        if(attackType.equals(TDAttackType.STRONGEST)) {
            TreeMap<Integer, TDEnemie> enemiesdamage = new TreeMap<>();
            for(TDEnemie enemie : enemies) {
                if(this.loc.distance(new Location(enemie.getEntity().level.getWorld(), enemie.getEntity().getX(), enemie.getEntity().getY(), enemie.getEntity().getZ())) > this.range) continue;
                enemiesdamage.put(enemie.getDamage(), enemie);
            }
            if(enemiesdamage.size() == 0 || enemiesdamage.descendingMap().firstKey() == null) return;
            enemiesdamage.descendingMap().get(enemiesdamage.descendingMap().firstKey()).damage(this);
        }else if(attackType.equals(TDAttackType.NEAREST)){
            TreeMap<Double, TDEnemie> enemiesdistance = new TreeMap<>();
            for(TDEnemie enemie : enemies) {
                double distance = this.loc.distance(new Location(enemie.getEntity().level.getWorld(), enemie.getEntity().getX(), enemie.getEntity().getY(), enemie.getEntity().getZ()));
                if(distance > this.range) continue;
                enemiesdistance.put(distance, enemie);
            }
            if(enemiesdistance.size() == 0 || enemiesdistance.firstKey() == null) return;
            enemiesdistance.get(enemiesdistance.firstKey()).damage(this);
        }else if(attackType.equals(TDAttackType.FIRST)) {
            ArrayList<TDEnemie> enemiesradius = new ArrayList<>();
            for(TDEnemie enemie : enemies) {
                if(this.loc.distance(new Location(enemie.getEntity().level.getWorld(), enemie.getEntity().getX(), enemie.getEntity().getY(), enemie.getEntity().getZ())) > this.range) continue;
                enemiesradius.add(enemie);
            }
            enemiesradius.get(0).damage(this);
        }
    }

    public void remove() {
        for(TDPlayer player : this.game.getPlayers()) {
            ServerGamePacketListenerImpl connection = ((CraftPlayer) player.getP()).getHandle().connection;
            connection.send(new ClientboundRemoveEntitiesPacket(entity.getId()));
        }
    }

    public void levelUp() {
        level++;
        loadData();
    }

    public void loadData() {
        ConfigurationSection dataSection = data.getConfigurationSection(this.type);
        cooldown = dataSection.getInt("level."+level+".cooldown", 20);
        range = dataSection.getInt("level."+level+".range", 5);
        damage = dataSection.getInt("level."+level+".damage", 2);
        try {
            boots = Material.valueOf(dataSection.getString("level."+level+".boots", "AIR"));
        } catch (IllegalArgumentException e) {
            boots = Material.AIR;
        }
        try {
            leggings = Material.valueOf(dataSection.getString("level."+level+".leggings", "AIR"));
        } catch (IllegalArgumentException e) {
            leggings = Material.AIR;
        }
        try {
            chestplate = Material.valueOf(dataSection.getString("level."+level+".chestplate", "AIR"));
        } catch (IllegalArgumentException e) {
            chestplate = Material.AIR;
        }
        try {
            helmet = Material.valueOf(dataSection.getString("level."+level+".helmet", "AIR"));
        } catch (IllegalArgumentException e) {
            helmet = Material.AIR;
        }
        try {
            entityType = EntityType.valueOf(dataSection.getString("level."+level+".entityType", "ZOMBIE"));
        } catch (IllegalArgumentException e) {
            entityType = EntityType.ZOMBIE;
        }

        if(!entityType.isSpawnable()) entityType = EntityType.ZOMBIE;

        if(this.entity == null) {
            MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
            ServerLevel nmsWorld = ((CraftWorld) loc.getWorld()).getHandle();
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), type);
            gameProfile.getProperties().put("textxures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYwOTA0NTY1OTE3NywKICAicHJvZmlsZUlkIiA6ICJiYzRlZGZiNWYzNmM0OGE3YWM5ZjFhMzlkYzIzZjRmOCIsCiAgInByb2ZpbGVOYW1lIiA6ICI4YWNhNjgwYjIyNDYxMzQwIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzE2YzVhZjIzYmM0YmZmZmY5NzIyZTZlN2VjYTc0MjhiOWRmY2VlYjM3Y2MxNmI1ODgyYjVhY2I2YzdkZDgwZTciCiAgICB9CiAgfQp9",
                    "IEnd6B63PO/Y3ApX0NC5xD6X486/CWCO810akzufb+5DmhGELWS9QB3y8arBLuPJWc5zIIZtG88Re5QmQ9e/zGXrUS1hl+adzqJC8ghpiQ8+h217I0MFuik8kepvP01fs5IM/VO8JuB44ENAMeVaqTDC3UpSTZwDgBOAitMm0JwjwWaUKP/COehEvhI47xZEevEha/uGdUn/S7oqCHC7lLwUkkNFlKRTZ6bgo9ul7HA0HmOxvvKKaPpyxT7nXPkY8dfmYKLhhAsdRmiISRQ5tx/vU5/wLUJJGo8OqHwSABmct2hopQ+vxAQyjj+uRBpGAWESm94uNNHy/5ympyUZFXI9dCFgQKmywPMEZLnXl6Jjnbq128VcX2jazDKw3rfSh/lGoxZRh4uuB5Hpa8Co974KsOFs3FzSHJ+umTtwF6IhQZpxvP3QIAmqTUakvlmuif4hhYEpeg7LqpUKjPoE03FcGfMZc3tt6QWfC2p78gM14vxJwmrpFWkUIU7YtfAKtT+h2MI771it4cDrxIckjwwYhxnTQbbtgd0+f8X8wPWazCuLG1GGBNmM+CYFDYe5uHbk26w2wqd675yoEmLthaHr+qzE7onURtlhYjB8+ZK2unnYxU8nzFSE3+vGLHkhiLw+XoHGnqymHxlfYZLhvGu1AL4x2mZbgDaFFpxNUUE="));
            entity = new ServerPlayer(nmsServer, nmsWorld, gameProfile);
            entity.setPos(loc.getX(), loc.getY(), loc.getZ());

            for(TDPlayer player : this.game.getPlayers()) {
                ServerGamePacketListenerImpl connection = ((CraftPlayer) player.getP()).getHandle().connection;
                connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, entity));
                connection.send(new ClientboundAddPlayerPacket(entity));
                connection.send(new ClientboundRotateHeadPacket(entity, (byte) (entity.getBukkitYaw() * 256 / 360)));
                connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, entity));
            }
        }

        if(this.entity instanceof LivingEntity) {
            ((LivingEntity) this.entity).getEquipment().setHelmet(new ItemStack(helmet, 1));
            ((LivingEntity) this.entity).getEquipment().setLeggings(new ItemStack(leggings, 1));
            ((LivingEntity) this.entity).getEquipment().setChestplate(new ItemStack(chestplate, 1));
            ((LivingEntity) this.entity).getEquipment().setBoots(new ItemStack(boots, 1));
        }
    }

    public static void registerTower(int range, int damage, int cooldown, String name, Material helmet, Material chestplate, Material leggings, Material boots, String skin) {
        data.set(name + "level.0.range", range);
        data.set(name + "level.0.damage", damage);
        data.set(name + "level.0.cooldown", cooldown);
        data.set(name + "level.0.boots", boots.toString());
        data.set(name + "level.0.leggings", leggings.toString());
        data.set(name + "level.0.chestplate", chestplate.toString());
        data.set(name + "level.0.helmet", helmet.toString());
        data.set(name + "skin", skin);

        saveConfig();
    }

    public static Set<String> getTowers() {
        return data.getKeys(false);
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
