package net.bluept.bamboo.services.randomizer;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.timer.TimerService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class Listeners implements Listener {
    public Listeners() {
    }

    @EventHandler
    public void event(EntityDamageEvent event) {
        if (event.getDamage() > 0 && event.getEntity() instanceof Player player) {
            RandomizerService randomizerService = Bamboo.INS.serviceManager.getService(RandomizerService.class);
            InvRandomizerService invRandomizerService = Bamboo.INS.serviceManager.getService(InvRandomizerService.class);
            if (randomizerService == null || invRandomizerService == null || !TimerService.isResumed()
                    || !randomizerService.config.get().getBoolean(invRandomizerService.configPrefix + "on_damage")) {
                return;
            }
            invRandomizerService.randomizePlayer(player);
        }
    }
}
