package net.bluept.bamboo.services.dimtp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.List;

public class Generator {
    public static World randomDim() {
        List<World> worlds = Bukkit.getWorlds();
        return worlds.get(DimTPConfig.random.nextInt(worlds.size()));
    }

    public static Object[] getRandomLocation(final World world, int iter) {
        iter++;

        final int x = DimTPConfig.random.nextInt(DimTPConfig.X_MIN, DimTPConfig.X_MAX);
        final int z = DimTPConfig.random.nextInt(DimTPConfig.Z_MIN, DimTPConfig.Z_MAX);
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
        DimTPConfig.INTERVAL = DimTPConfig.random.nextInt(DimTPConfig.INTERVAL_MIN, DimTPConfig.INTERVAL_MAX);
    }
}
