package placeblock.towerdefense.game;

import com.sun.jna.platform.win32.LMAccess;
import lombok.Getter;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFloat;
import net.minecraft.world.entity.ai.goal.PathfinderGoalMeleeAttack;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import placeblock.towerdefense.util.PathfinderGoalLoc;


public class TDEnemieEntity extends EntityCreature {

    @Getter private final TDEnemie enemie;

    public TDEnemieEntity(EntityTypes<? extends EntityCreature> entitytypes, Location location, TDEnemie enemie) {
        super(entitytypes, ((CraftWorld) location.getWorld()).getHandle());
        this.setPosition(location.getX(), location.getY(), location.getZ());
        this.setInvulnerable(true);
        this.setCanPickupLoot(false);
        this.collides = false;
        this.setCustomNameVisible(true);
        this.enemie = enemie;

        this.bP.a(0, new PathfinderGoalLoc(this, enemie.getSpeed(), enemie));
    }

    @Override
    protected void initPathfinder() {
    }

    @Override
    public void g(double d0, double d1, double d2) {
        return;
    }

    public void setItem(EnumItemSlot slot, ItemStack item) {
        this.setSlot(slot, CraftItemStack.asNMSCopy(item));
    }

    public void setName(String name) {
        this.setCustomName(new ChatComponentText(ChatColor.translateAlternateColorCodes('&', name)));
    }
}
