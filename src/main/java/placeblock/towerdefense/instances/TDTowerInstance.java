package placeblock.towerdefense.instances;

import lombok.Getter;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import placeblock.towerdefense.TowerDefense;
import placeblock.towerdefense.data.TDTower;

import java.util.TreeMap;

public class TDTowerInstance {

    @Getter private final TDTower tower;
    private final Location loc;
    private final TDGameInstance game;
    private final NPC npc;

    public TDTowerInstance(TDTower tower, Location location, TDGameInstance game) {
        this.tower = tower;
        this.loc = location;
        this.game = game;
        npc = CitizensAPI.getNPCRegistry().createNPC(tower.getType(), tower.getName());
        npc.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.BOOTS, new ItemStack(tower.getBoots(), 1));
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.LEGGINGS, new ItemStack(tower.getLeggings(), 1));
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.CHESTPLATE, new ItemStack(tower.getChestplate(), 1));
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.HELMET, new ItemStack(tower.getHelmet(), 1));

        new BukkitRunnable() {
            @Override
            public void run() {
                shoot();
            }
        }.runTaskTimer(TowerDefense.getInstance(), 0, this.tower.getCooldown());
    }

    public Location getLocation() {
        if(!this.npc.isSpawned()) return null;
        return this.npc.getEntity().getLocation();
    }

    public void shoot() {
        if(this.game.getActiveWave() == null) return;
        if(!this.npc.isSpawned()) return;
        TreeMap<Integer, TDEnemieInstance> enemies = new TreeMap<>();
        for(TDEnemieInstance enemie : this.game.getActiveWave().getEnemieInstances()) {
            if(this.npc.getEntity().getLocation().distance(enemie.getLocation()) > this.tower.getRange()) continue;
            enemies.put(enemie.getEnemie().getDamage(), enemie);
        }
        if(enemies.size() == 0) return;
        enemies.descendingMap().get(0).damage(this);
    }

}
