package placeblock.towerdefense.instances;

import lombok.Getter;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.event.NavigationCompleteEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import placeblock.towerdefense.data.TDEnemie;
import placeblock.towerdefense.util.BloodParticles;

public class TDEnemieInstance implements Listener {

    @Getter private final TDEnemie enemie;
    private final TDWaveInstance wave;
    private final NPC npc;
    private int pathindex = 1;
    private int health;


    public TDEnemieInstance(TDEnemie enemie, TDWaveInstance wave) {
        this.enemie = enemie;
        this.wave = wave;
        this.health = enemie.getHealth();
        npc = CitizensAPI.getNPCRegistry().createNPC(enemie.getType(), enemie.getName());
        npc.getEntity().setInvulnerable(true);
        npc.teleport(wave.getGame().getPath().getLocation(0), PlayerTeleportEvent.TeleportCause.PLUGIN);
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.BOOTS, new ItemStack(enemie.getBoots(), 1));
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.LEGGINGS, new ItemStack(enemie.getLeggings(), 1));
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.CHESTPLATE, new ItemStack(enemie.getChestplate(), 1));
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.HELMET, new ItemStack(enemie.getHelmet(), 1));
        npc.getNavigator().setTarget(wave.getGame().getPath().getLocation(pathindex));
    }

    public void damage(TDTowerInstance tower) {
        this.health -= tower.getTower().getDamage();
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

}
