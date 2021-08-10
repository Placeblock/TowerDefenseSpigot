package placeblock.towerdefense.util;

import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.level.pathfinder.PathEntity;
import org.bukkit.Location;
import placeblock.towerdefense.game.TDEnemie;

import java.util.EnumSet;

public class PathfinderGoalLoc extends PathfinderGoal {
    protected final EntityCreature a; //THE ENTITY
    protected double b; // x
    protected double c; // y
    protected double d; // z
    protected final double e; // speed
    protected PathEntity oldk = null;
    protected final TDEnemie enemie;

    public PathfinderGoalLoc(EntityCreature paramEntityCreature, double speed, TDEnemie enemie)
    {
        this.enemie = enemie;
        a = paramEntityCreature;
        e = speed;
        Location loc = enemie.nextWaypoint();
        b = loc.getX();
        c = loc.getY();
        d = loc.getZ();
        System.out.println("INIT LOCATION");
        System.out.println(loc);
        this.a(EnumSet.of(Type.d));
    }

    //RUNS EVERY SINGLE TICK (THIS IS HOW ENTITIES WORK)
    //WILL START PATHFINDING GOAL IF ITS TRUE
    @Override
    public boolean a() {
        return true; //<- runs c
    }

    //RUNS IF A IS TRUE
    public void c() {
        //runner                    x      y        z     speed
        this.a.getNavigation().a(this.b, this.c, this.d, this.e);
    }

    //RUNS AFTER C
    //RUN EVERY TICK AS LONG AS ITS TRUE (AFTER C GOTS EXECUTED)
    public boolean b() {
        System.out.println("OLDK: " + oldk + " | K: " + this.a.getNavigation().k());
        if(this.oldk == null && this.a.getNavigation().k() == null) {
            return false;
        }
        this.oldk = this.a.getNavigation().k();
        return true;
    }

    //RUNS WHEN B RETURNS FALSE
    public void d() {
        Location newloc = this.enemie.nextWaypoint();
        System.out.println("GOT NEW LOCATION");
        System.out.println(newloc);
        if(newloc == null) return;
        this.b = newloc.getX();
        this.c = newloc.getY();
        this.d = newloc.getZ();
    }

}
