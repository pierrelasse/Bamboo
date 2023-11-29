package net.bluept.bamboo.services.randomizer;

import net.bluept.bamboo.Bamboo;
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
        InvRandomizerService invRandomizerService = Bamboo.INS.serviceManager.getService(InvRandomizerService.class);
        if (invRandomizerService != null && event.getEntity() instanceof Player player) {
            invRandomizerService.randomizePlayer(player);
            Utils.send(player, "&6&lHuch, da hab ich wohl deine Items durch andere ausgetauscht :/");
        }
    }
}
