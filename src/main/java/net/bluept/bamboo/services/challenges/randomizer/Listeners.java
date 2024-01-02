package net.bluept.bamboo.services.challenges.randomizer;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.dep.timer.TimerService;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class Listeners implements Listener {
    public Listeners() {
    }

    @EventHandler
    public void event(EntityDamageEvent event) {
        final double finalDamage = event.getFinalDamage();
        if (finalDamage > 0 && event.getEntity() instanceof Player player) {
            RandomizerService randomizerService = Bamboo.INS.serviceManager.getService(RandomizerService.class);
            InvRandomizerService invRandomizerService = Bamboo.INS.serviceManager.getService(InvRandomizerService.class);
            if (randomizerService == null || invRandomizerService == null || !TimerService.isResumed()
                    || !randomizerService.config.get().getBoolean(invRandomizerService.configPrefix + "on_damage")) {
                return;
            }
            invRandomizerService.randomizePlayer(player);
            Bamboo.INS.logDev("[Randomizer/InvRandomizer] Randomized " + player.getName()
                    + "'s inventory due to taking damage(type=" + event.getCause().name() + ",amount=" + finalDamage + ")");
        }
    }

    @EventHandler
    public void event(final PlayerMoveEvent event) {
        if (event.getFrom().getBlock().equals(event.getTo().getBlock())) {
            return;
        }

        if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        RandomizerService randomizerService = Bamboo.INS.serviceManager.getService(RandomizerService.class);
        StepRandomizerService stepRandomizerService = Bamboo.INS.serviceManager.getService(StepRandomizerService.class);
        if (randomizerService == null || stepRandomizerService == null || !TimerService.isResumed()) {
            return;
        }

        final Material material = stepRandomizerService.getRandomMaterial();
        if (material != null) {
            final Block block = event.getTo().clone().add(0, -1, 0).getBlock();
            if (!block.getType().isAir() && !stepRandomizerService.blacklisted_blocks.contains(block.getType())) {
                block.setType(material);
            }
        }
    }
}
