package placeblock.towerdefense.util;

import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLightning;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;

import java.util.EnumSet;

public class PathfinderGoalLoc extends PathfinderGoal {
    protected final EntityCreature a; //THE ENTITY
    protected final double b; // x
    protected final double c; // y
    protected final double d; // z
    protected final double e; // speed

    public PathfinderGoalLoc(EntityCreature entityCreature, double x, double y, double z, double speed) {
        this(entityCreature, x, y, z, speed, 5);
    }

    public PathfinderGoalLoc(EntityCreature paramEntityCreature,double x, double y, double z, double speed, int timeBetweenMovement)
    {
        a = paramEntityCreature;
        e = speed;
        b = x;
        c = y;
        d = z;
        this.a(EnumSet.of(Type.a));
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
        this.a.getNavigation().o();
        return true;
    }

    //RUNS WHEN B RETURNS FALSE
    public void d() {

    }

}
