package net.bluept.bamboo.services.multiplier;

import net.bluept.bamboo.Bamboo;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class Listeners implements Listener {
    public Listeners() {
    }

    @EventHandler
    public void event(BlockDropItemEvent event) {
        MultiplierService multiplierService = Bamboo.INS.serviceManager.getService(MultiplierService.class);
        if (multiplierService != null && multiplierService.isEnabled("block_drops")) {
            for (Item drop : event.getItems()) {
                if (drop != null) {
                    final int multiplier = multiplierService.getMultiplierAndIncrease("block_drops." + drop.getItemStack().getType().name());
                    for (int i = 0; i < multiplier; i++) {
                        drop.getWorld().dropItem(drop.getLocation(), drop.getItemStack());
                    }
                }
            }
        }
    }

    @EventHandler
    public void event(EntityDeathEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER) {
            return;
        }

        MultiplierService multiplierService = Bamboo.INS.serviceManager.getService(MultiplierService.class);
        if (multiplierService != null) {
            if (multiplierService.isEnabled("mob_drops")) {
                final int multiplier = multiplierService.getMultiplierAndIncrease("mob_drops." + event.getEntity().getType().name());
                for (int i = 0; i < multiplier; i++) {
                    for (ItemStack drop : event.getDrops()) {
                        event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), drop);
                    }
                }
            }

            int xp = event.getDroppedExp();
            if (xp > 0) {
                if (multiplierService.isEnabled("mob_xp")) {
                    event.setDroppedExp(xp * multiplierService.getMultiplierAndIncrease("mob_xp"));
                }
            }
        }
    }
}
