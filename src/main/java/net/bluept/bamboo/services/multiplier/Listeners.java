package net.bluept.bamboo.services.multiplier;

import net.bluept.bamboo.Bamboo;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
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
            for (Item drop : event.getItems()) {
                if (drop != null) {
                    final int multiplier = multiplierService.getMultiplierAndIncrease("block_drops." + drop.getItemStack().getType().name());
                    for (int i = 0; i < multiplier; i++) {
                        drop.getWorld().dropItem(drop.getLocation(), drop.getItemStack());
                    }
                }
                if (multiplierService.config.get().getBoolean("1_item_max_multiply")) {
                    break;
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
            if (multiplierService.isEnabled("mob_xp")) {
                event.setDroppedExp(event.getDroppedExp() * multiplierService.getMultiplierAndIncrease("mob_xp"));
            }
        }
    }
}
