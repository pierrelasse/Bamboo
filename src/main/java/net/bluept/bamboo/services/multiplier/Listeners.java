package net.bluept.bamboo.services.multiplier;

import net.bluept.bamboo.Bamboo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class Listeners implements Listener {
    public Listeners() {
        Bamboo.INS.getServer().getPluginManager().registerEvents(new Listeners(), Bamboo.INS);
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void event(BlockDropItemEvent event) {
        MultiplierService multiplierService = Bamboo.INS.serviceManager.getService(MultiplierService.class);
        if (multiplierService != null && multiplierService.isEnabled("block_drops")) {
            for (int i = 0; i < multiplierService.multiplier; i++) {
                event.getItems().addAll(event.getItems());
            }
        }
    }

    @EventHandler
    public void event(EntityDeathEvent event) {
        MultiplierService multiplierService = Bamboo.INS.serviceManager.getService(MultiplierService.class);
        if (multiplierService != null) {
            if (multiplierService.isEnabled("mob_drops")) {
                for (int i = 0; i < multiplierService.multiplier; i++) {
                    event.getDrops().addAll(event.getDrops());
                }
            }
            if (multiplierService.isEnabled("mob_xp")) {
                event.setDroppedExp(event.getDroppedExp() * multiplierService.multiplier);
            }
        }
    }
}
