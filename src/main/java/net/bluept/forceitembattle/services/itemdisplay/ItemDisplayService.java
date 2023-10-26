package net.bluept.forceitembattle.services.itemdisplay;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.service.Service;
import net.bluept.forceitembattle.services.item.ItemService;
import net.bluept.forceitembattle.services.timer.TimerService;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class ItemDisplayService extends Service {
    private BukkitTask tickTask;

    @Override
    public void onEnable() {
        tickTask = Bukkit.getScheduler().runTaskTimer(ForceItemBattle.INS, this::tick, 0L, 20L);
    }

    @Override
    public void onDisable() {
        tickTask.cancel();
        clearAllDisplays();
    }

    public void tick() {
        TimerService timerService = ForceItemBattle.INS.serviceManager.getService(TimerService.class);
        if (timerService != null && timerService.resumed) {
            ItemService itemService = ForceItemBattle.INS.serviceManager.getService(ItemService.class);
            if (itemService != null) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    ensureDisplayForPlayer(onlinePlayer);
                    updatePlayerDisplay(itemService, onlinePlayer, getPlayerDisplay(onlinePlayer));
                }
            }
            clearLoneDisplays();
        } else {
            clearAllDisplays();
        }
    }

    public void ensureDisplayForPlayer(Player player) {
        Entity display = getPlayerDisplay(player);
        if (player.getGameMode() == GameMode.SPECTATOR) {
            if (display != null) {
                display.remove();
            }
        } else if (display == null) {
            createPlayerDisplay(player);
        }
    }

    public Entity getPlayerDisplay(Player player) {
        for (Entity passenger : player.getPassengers()) {
            if (passenger.getScoreboardTags().contains("forceitembattle")) {
                return passenger;
            }
        }
        return null;
    }

    public void createPlayerDisplay(Player player) {
        ArmorStand armorStand = (ArmorStand)player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
        armorStand.setInvisible(true);
        armorStand.setMarker(true);
        armorStand.setInvulnerable(true);
        armorStand.setSmall(true);
        armorStand.setDisabledSlots(EquipmentSlot.CHEST, EquipmentSlot.FEET, EquipmentSlot.HAND, EquipmentSlot.HEAD, EquipmentSlot.LEGS, EquipmentSlot.OFF_HAND);
        armorStand.addScoreboardTag("forceitembattle");
        player.addPassenger(armorStand);
    }

    public void updatePlayerDisplay(ItemService itemService, Player player, Entity passenger) {
        if (passenger instanceof ArmorStand armorStand) {
            Material material = itemService.getPlayerMaterial(player.getUniqueId());
            if (armorStand.getEquipment().getHelmet().getType() != material) {
                armorStand.getEquipment().setHelmet(new ItemStack(material));
            }
        }
    }

    public void clearAllDisplays() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getType() == EntityType.ARMOR_STAND && entity.getScoreboardTags().contains("forceitembattle")) {
                    entity.remove();
                }
            }
        }
    }

    public void clearLoneDisplays() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getType() == EntityType.ARMOR_STAND && entity.getVehicle() == null && entity.getScoreboardTags().contains("forceitembattle")) {
                    entity.remove();
                }
            }
        }
    }
}
