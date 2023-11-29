package net.bluept.bamboo.services.goal;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.timer.TimerService;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class Listeners implements Listener {
    public Listeners() {
    }

    @EventHandler
    public void event(EntityDeathEvent event) {
        if (event.getEntity().getType() != EntityType.ENDER_DRAGON) {
            return;
        }

        TimerService timerService = Bamboo.INS.serviceManager.getService(TimerService.class);
        if (timerService == null || !timerService.resumed) {
            return;
        }

        timerService.resumed = false;
    }
}
