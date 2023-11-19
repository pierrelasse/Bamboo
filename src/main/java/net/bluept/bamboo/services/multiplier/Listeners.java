package net.bluept.bamboo.services.multiplier;

import net.bluept.bamboo.Bamboo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class Listeners implements Listener {
    public Listeners() {
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void event(BlockDropItemEvent event) {
        MultiplierService multiplierService = Bamboo.INS.serviceManager.getService(MultiplierService.class);
        if (multiplierService != null && multiplierService.isEnabled("block_drops")) {
            final int multiplier = multiplierService.getMultiplierAndIncrease();
            for (int i = 0; i < multiplier; i++) {
                event.getItems().forEach(item -> item.getWorld().dropItem(item.getLocation(), item.getItemStack()));
            }
        }
    }

    @EventHandler
    public void event(EntityDeathEvent event) {
        MultiplierService multiplierService = Bamboo.INS.serviceManager.getService(MultiplierService.class);
        if (multiplierService != null) {
            if (multiplierService.isEnabled("mob_drops")) {
                final int multiplier = multiplierService.getMultiplierAndIncrease();
                for (int i = 0; i < multiplier; i++) {
                    for (ItemStack drop : event.getDrops()) {
                        event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), drop);
                    }
                }
            }
            if (multiplierService.isEnabled("mob_xp")) {
                event.setDroppedExp(event.getDroppedExp() * multiplierService.getMultiplierAndIncrease());
            }
        }
    }
}
