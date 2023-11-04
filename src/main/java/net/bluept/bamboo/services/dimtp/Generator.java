package net.bluept.bamboo.services.dimtp;

import net.bluept.bamboo.Bamboo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.List;

public class Generator {
    private static int lastDim = -1;

    public static World randomDim() {
        List<World> worlds = Bukkit.getWorlds();
        int dim = Bamboo.INS.random.nextInt(worlds.size());
        if (dim == lastDim && worlds.size() > 1) {
            Bamboo.INS.getLogger().info("DimTP: Generated same dim. Generating new one");
            return randomDim();
        }
        lastDim = dim;
        return worlds.get(dim);
    }

    public static Object[] getRandomLocation(final World world, int iter) {
        iter++;

        final int x = Bamboo.INS.random.nextInt(DimTPConfig.X_MIN, DimTPConfig.X_MAX);
        final int z = Bamboo.INS.random.nextInt(DimTPConfig.Z_MIN, DimTPConfig.Z_MAX);
        final Location location = new Location(world, x + .5, 319, z + .5);

        while (location.getY() > 0) {
            if (isLocationSafe(location.clone())) {
                return new Object[]{location, iter};
            }
            location.setY(location.getY() - 1);
        }

        if (iter >= DimTPConfig.MAX_TRIES) {
            return new Object[]{null, iter};
        }

        return getRandomLocation(world, iter);
    }

    public static boolean isLocationSafe(final Location location) {
        return location.getBlock().getType().isAir()
                && location.add(0, -1, 0).getBlock().getType().isAir()
                && location.add(0, -1, 0).getBlock().getType().isSolid()
                && location.getBlock().getType() != Material.BEDROCK;
    }

    public static void newInterval() {
        DimTPConfig.INTERVAL = Bamboo.INS.random.nextInt(DimTPConfig.INTERVAL_MIN, DimTPConfig.INTERVAL_MAX);
    }
}
