package net.bluept.bamboo.services.randomizer;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.timer.TimerService;
import net.bluept.bamboo.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class Listeners implements Listener {
    public Listeners() {
    }

    @EventHandler
    public void event(EntityDamageEvent event) {
        RandomizerService randomizerService = Bamboo.INS.serviceManager.getService(RandomizerService.class);
        InvRandomizerService invRandomizerService = Bamboo.INS.serviceManager.getService(InvRandomizerService.class);
        if (randomizerService == null || invRandomizerService == null || !TimerService.isResumed() || !randomizerService.config.get().getBoolean(invRandomizerService.configPrefix + "on_damage")) {
            return;
        }
        if (event.getEntity() instanceof Player player) {
            invRandomizerService.randomizePlayer(player);
            Utils.send(player, "<%dDamageLP> Huch, da hab ich wohl deine Items durch andere ausgetauscht :/".replace("%d", event.getCause().name().toLowerCase()));
        }
    }
}
