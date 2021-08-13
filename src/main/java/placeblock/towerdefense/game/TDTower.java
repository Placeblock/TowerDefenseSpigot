package placeblock.towerdefense.game;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import lombok.Getter;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import placeblock.towerdefense.TowerDefense;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Getter
public class TDTower {
    private final static File file = new File(TowerDefense.getInstance().getDataFolder() + "/towers.yml");
    private final static YamlConfiguration data = YamlConfiguration.loadConfiguration(file);

    private final TDPlayer owner;
    private final Location loc;
    private final TDGame game;
    private final String type;
    private BukkitTask shootimer;
    private BukkitTask moveheadtimer;
    private TDAttackType attackType = TDAttackType.FIRST;
    private ServerPlayer entity;
    private EntityType entityType;
    private Material boots;
    private Material leggings;
    private Material chestplate;
    private Material helmet;
    private int level = 1;
    private int range;
    private int damage;
    private int cooldown;

    public TDTower(String type, Location location, TDGame game, TDPlayer owner) {
        this.owner = owner;
        this.loc = location;
        this.game = game;
        this.type = type;

        loadData();

        shootimer = new BukkitRunnable() {
            @Override
            public void run() {
                shoot();
            }
        }.runTaskTimer(TowerDefense.getInstance(), 0, this.cooldown);

        moveheadtimer = new BukkitRunnable() {
            @Override
            public void run() {
                TDEnemie attackable = getAttackable();
                if(attackable == null) return;
                Vector difference = attackable.getEntity().getBukkitEntity().getLocation().subtract(loc).toVector().normalize();
                float degrees = (float) Math.toDegrees(Math.atan2(difference.getZ(), difference.getX()) - Math.PI / 2);
                byte angle = (byte) MathHelper.d((degrees * 256.0F) / 360.0F);

                for(TDPlayer player : TDTower.this.game.getPlayers()) {
                    ServerGamePacketListenerImpl connection = ((CraftPlayer) player.getP()).getHandle().connection;
                    connection.send(new ClientboundRotateHeadPacket(entity, angle));
                }
            }
        }.runTaskTimer(TowerDefense.getInstance(), 0, 20);
    }

    public void shoot() {
        TDEnemie attackable = getAttackable();
        if(attackable == null) return;
        attackable.damage(this);
    }

    public TDEnemie getAttackable() {
        ArrayList<TDEnemie> enemies = this.game.getEnemies();
        if(enemies.size() == 0) return null;
        if(attackType.equals(TDAttackType.STRONGEST)) {
            TreeMap<Integer, TDEnemie> enemiesdamage = new TreeMap<>();
            for(TDEnemie enemie : enemies) {
                if(this.loc.distance(enemie.getEntity().getBukkitEntity().getLocation()) > this.range) continue;
                enemiesdamage.put(enemie.getDamage(), enemie);
            }
            if(enemiesdamage.size() == 0 || enemiesdamage.descendingMap().firstKey() == null) return null;
            return enemiesdamage.descendingMap().get(enemiesdamage.descendingMap().firstKey());
        }else if(attackType.equals(TDAttackType.NEAREST)){
            TreeMap<Double, TDEnemie> enemiesdistance = new TreeMap<>();
            for(TDEnemie enemie : enemies) {
                double distance = this.loc.distance(enemie.getEntity().getBukkitEntity().getLocation());
                if(distance > this.range) continue;
                enemiesdistance.put(distance, enemie);
            }
            if(enemiesdistance.size() == 0 || enemiesdistance.firstKey() == null) return null;
            return enemiesdistance.get(enemiesdistance.firstKey());
        }else if(attackType.equals(TDAttackType.FIRST)) {
            ArrayList<TDEnemie> enemiesradius = new ArrayList<>();
            for(TDEnemie enemie : enemies) {
                if(this.loc.distance(enemie.getEntity().getBukkitEntity().getLocation()) > this.range) continue;
                enemiesradius.add(enemie);
            }
            if(enemiesradius.size() == 0) return null;
            return enemiesradius.get(0);
        }
        return null;
    }

    public void remove() {
        for(TDPlayer player : this.game.getPlayers()) {
            ServerGamePacketListenerImpl connection = ((CraftPlayer) player.getP()).getHandle().connection;
            connection.send(new ClientboundRemoveEntitiesPacket(entity.getId()));
        }
        moveheadtimer.cancel();
        shootimer.cancel();
    }

    public void levelUp() {
        if(!getData(type).contains("level." + (level + 1))) {
            owner.getP().sendMessage("Maximales Level erreicht!");
            return;
        }
        level++;
        owner.removeMoney(getData(type).getConfigurationSection("level." + level).getInt("price", 30));
        loadData();
    }

    public void loadData() {
        ConfigurationSection dataSection = data.getConfigurationSection(this.type);
        cooldown = dataSection.getInt("level."+level+".cooldown", 4);
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

        String skinvalue = getSkinValue(type);
        String skinsig = getSkinSig(type);

        if(this.entity instanceof LivingEntity) {
            ((LivingEntity) this.entity).getEquipment().setHelmet(new ItemStack(helmet, 1));
            ((LivingEntity) this.entity).getEquipment().setLeggings(new ItemStack(leggings, 1));
            ((LivingEntity) this.entity).getEquipment().setChestplate(new ItemStack(chestplate, 1));
            ((LivingEntity) this.entity).getEquipment().setBoots(new ItemStack(boots, 1));
        }
        if(this.entity == null) {
            MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
            ServerLevel nmsWorld = ((CraftWorld) loc.getWorld()).getHandle();
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), type);
            gameProfile.getProperties().put("textxures", new Property("textures", skinvalue, skinsig));
            entity = new ServerPlayer(nmsServer, nmsWorld, gameProfile);
            entity.setPos(loc.getX(), loc.getY(), loc.getZ());
            for(TDPlayer player : this.game.getPlayers()) {
                ServerGamePacketListenerImpl connection = ((CraftPlayer) player.getP()).getHandle().connection;
                connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, entity));
                connection.send(new ClientboundAddPlayerPacket(entity));
                connection.send(new ClientboundRotateHeadPacket(entity, (byte) (entity.getBukkitYaw() * 256 / 360)));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, entity));
                    }
                }.runTaskLater(TowerDefense.getInstance(), 10);
            }
        }

        List<Pair<EquipmentSlot, net.minecraft.world.item.ItemStack>> list = new ArrayList<>();
        list.add(new Pair<>(EquipmentSlot.FEET, CraftItemStack.asNMSCopy(new ItemStack(boots))));
        list.add(new Pair<>(EquipmentSlot.LEGS, CraftItemStack.asNMSCopy(new ItemStack(leggings))));
        list.add(new Pair<>(EquipmentSlot.CHEST, CraftItemStack.asNMSCopy(new ItemStack(chestplate))));
        list.add(new Pair<>(EquipmentSlot.HEAD, CraftItemStack.asNMSCopy(new ItemStack(helmet))));

        for(TDPlayer player : this.game.getPlayers()) {
            ServerGamePacketListenerImpl connection = ((CraftPlayer) player.getP()).getHandle().connection;
            connection.send(new ClientboundSetEquipmentPacket(entity.getId(), list));
        }
    }

    public int getSellPrice() {
        int sellprice = 0;
        ConfigurationSection towerdata = getData(this.type);
        sellprice += towerdata.getInt("sell", 50);
        for(String level : towerdata.getConfigurationSection("level").getKeys(false)) {
            sellprice += towerdata.getInt("level." + level + ".sell", 20);
        }
        return sellprice;
    }

    public static String getSkinValue(String name) {
        return data.getString(name + ".skin.value",
                "ewogICJ0aW1lc3RhbXAiIDogMTYwOTA0NTY1OTE3NywKICAicHJvZmlsZUlkIiA6ICJiYzRlZGZiNWYzNmM0OG" +
                        "E3YWM5ZjFhMzlkYzIzZjRmOCIsCiAgInByb2ZpbGVOYW1lIiA6ICI4YWNhNjgwYjIyNDYxM" +
                        "zQwIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewog" +
                        "ICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ" +
                        "0Lm5ldC90ZXh0dXJlLzE2YzVhZjIzYmM0YmZmZmY5NzIyZTZlN2VjYTc0MjhiOWRmY2VlYj" +
                        "M3Y2MxNmI1ODgyYjVhY2I2YzdkZDgwZTciCiAgICB9CiAgfQp9");
    }

    public static String getSkinSig(String name) {
        return data.getString(name + ".skin.signature",
                "IEnd6B63PO/Y3ApX0NC5xD6X486/CWCO810akzufb+5DmhGELWS9QB3y8arBLuPJWc5zIIZt" +
                        "G88Re5QmQ9e/zGXrUS1hl+adzqJC8ghpiQ8+h217I0MFuik8kepvP01fs5IM/VO8JuB" +
                        "44ENAMeVaqTDC3UpSTZwDgBOAitMm0JwjwWaUKP/COehEvhI47xZEevEha/uGdUn/S7" +
                        "oqCHC7lLwUkkNFlKRTZ6bgo9ul7HA0HmOxvvKKaPpyxT7nXPkY8dfmYKLhhAsdRmiIS" +
                        "RQ5tx/vU5/wLUJJGo8OqHwSABmct2hopQ+vxAQyjj+uRBpGAWESm94uNNHy/5ympyUZ" +
                        "FXI9dCFgQKmywPMEZLnXl6Jjnbq128VcX2jazDKw3rfSh/lGoxZRh4uuB5Hpa8Co974" +
                        "KsOFs3FzSHJ+umTtwF6IhQZpxvP3QIAmqTUakvlmuif4hhYEpeg7LqpUKjPoE03FcGf" +
                        "MZc3tt6QWfC2p78gM14vxJwmrpFWkUIU7YtfAKtT+h2MI771it4cDrxIckjwwYhxnTQ" +
                        "bbtgd0+f8X8wPWazCuLG1GGBNmM+CYFDYe5uHbk26w2wqd675yoEmLthaHr+qzE7onU" +
                        "RtlhYjB8+ZK2unnYxU8nzFSE3+vGLHkhiLw+XoHGnqymHxlfYZLhvGu1AL4x2mZbgDa" +
                        "FFpxNUUE=");
    }

    public static void registerTower(int range, int damage, int cooldown, String name, Material helmet, Material chestplate, Material leggings, Material boots, String skin) {
        data.set(name + ".level.0.range", range);
        data.set(name + ".level.0.damage", damage);
        data.set(name + ".level.0.cooldown", cooldown);
        data.set(name + ".level.0.boots", boots.toString());
        data.set(name + ".level.0.leggings", leggings.toString());
        data.set(name + ".level.0.chestplate", chestplate.toString());
        data.set(name + ".level.0.helmet", helmet.toString());
        data.set(name + ".skin", skin);

        saveConfig();
    }

    public static ConfigurationSection getData(String name) {
        return data.getConfigurationSection(name);
    }

    public static void saveConfig() {
        try {
            data.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
