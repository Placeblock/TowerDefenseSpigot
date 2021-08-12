package placeblock.towerdefense.util;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;
import org.bukkit.Location;
import placeblock.towerdefense.game.TDEnemie;

import java.util.EnumSet;

public class PathfinderGoalLoc extends Goal {
    protected final PathfinderMob creature; //THE ENTITY
    protected double x; // x
    protected double y; // y
    protected double z; // z
    protected final double speed; // speed
    protected final TDEnemie enemie;
    protected int fails;
    protected Path path;

    public PathfinderGoalLoc(PathfinderMob creature, double speed, TDEnemie enemie)
    {
        this.enemie = enemie;
        this.creature = creature;
        this.speed = speed;
        Location loc = enemie.nextWaypoint();
        x = loc.getX();
        y = loc.getY();
        z = loc.getZ();
        System.out.println(x);
        System.out.println(y);
        System.out.println(z);
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() { //a
        this.path = creature.getNavigation().createPath(this.x, this.y, this.z, 32); //a(...);
        if(this.path == null) {
            if(this.fails >= 6) return false;
            this.fails++;
            return false;
        }
        this.creature.getNavigation().moveTo(this.x, this.y, this.z, this.speed);
        return true;
    }

    public boolean canContinueToUse() { //b
        return !this.creature.getNavigation().isDone();
    }

    public void start() { //c
        this.fails = 0;

    }

    public void stop() { //d
        this.path = null;
        Location newloc = this.enemie.nextWaypoint();
        if(newloc == null) return;
        this.x = newloc.getX();
        this.y = newloc.getY();
        this.z = newloc.getZ();
    }

    public void tick() { //e
    }

}
