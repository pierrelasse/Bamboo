package net.bluept.bamboo.services.challenges.dimtp.dimtp3;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;
import net.bluept.bamboo.services.challenges.dimtp.Generator;
import net.bluept.bamboo.services.challenges.randomizer.Listeners;
import net.bluept.bamboo.services.dep.display.DisplayController;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

@ServiceInfo(description = "cock")
public class DimTP3Service extends Service {
    private Listeners listeners;

    @Override
    public void onEnable() {
        if (listeners != null) {
            HandlerList.unregisterAll(listeners);
            listeners = null;
        }
        Bamboo.INS.getServer().getPluginManager().registerEvents(listeners = new Listeners(), Bamboo.INS);

        DisplayController.push();
    }

    @Override
    public void onDisable() {
        DisplayController.pop();

        if (listeners != null) {
            HandlerList.unregisterAll(listeners);
            listeners = null;
        }
    }

    @Override
    public void onTest() {
        final long startTime = System.currentTimeMillis();

        for (World world : Bukkit.getWorlds()) {
            final Object[] data = Generator.getRandomLocation(world, 0);

            if (!(data[0] instanceof Location)) {
                continue;
            }
        }

        int i = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() != GameMode.SURVIVAL) continue;


//            if (data[0] instanceof Location location) {
//                Bamboo.INS.logDev("[DimTP3] Location for " + player.getName() + " found after " + (System.currentTimeMillis() - startTime) + "ms and " + data[1] + " tries");
//                player.teleport(location);
//            } else {
//                Bamboo.INS.logDev("[DimTP3] &cFailed generating location for " + player.getName() + ". Tries: " + data[1]);
//            }
        }
    }
}
