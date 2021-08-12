package placeblock.towerdefense.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;

public class BloodParticles {

    public static void spawnBlood(Location loc) {
        loc.getWorld().spawnParticle(
            Particle.BLOCK_CRACK,
            loc,
            20,
            0.3,
            0.2,
            0.3,
            Material.REDSTONE_BLOCK.createBlockData()
        );
    }

}
