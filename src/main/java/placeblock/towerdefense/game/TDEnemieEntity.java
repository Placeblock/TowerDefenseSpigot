package placeblock.towerdefense.game;

import lombok.Getter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import placeblock.towerdefense.util.PathfinderGoalLoc;


public class TDEnemieEntity extends PathfinderMob {

    @Getter private final TDEnemie enemie;

    public TDEnemieEntity(EntityType entitytype, Location location, TDEnemie enemie) {
        super(entitytype, ((CraftWorld) location.getWorld()).getHandle());
        this.setInvulnerable(true);
        this.setPos(location.getX(), location.getY(), location.getZ());
        this.setCanPickUpLoot(false);
        this.collides = false;
        this.setCustomNameVisible(true);
        this.enemie = enemie;

        this.goalSelector.addGoal(0, new PathfinderGoalLoc(this, enemie.getSpeed(), enemie));
    }

    public void setItem(EquipmentSlot slot, ItemStack item) {
        this.setSlot(slot, CraftItemStack.asNMSCopy(item), true);
    }

}
