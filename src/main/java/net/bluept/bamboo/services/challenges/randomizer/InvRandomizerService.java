package net.bluept.bamboo.services.challenges.randomizer;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;
import net.bluept.bamboo.services.dep.timer.TimerService;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

@ServiceInfo(id = "randomizer/invrandomizer")
public class InvRandomizerService extends Service {
    public final String configPrefix = "invrandomizer.";
    public int tick;
    public int interval;
    private BukkitTask tickTask;
    public List<Material> materials;

    @Override
    public void onEnable() {
        RandomizerService randomizerService = Bamboo.INS.serviceManager.getService(RandomizerService.class);
        if (randomizerService == null) {
            return;
        }

        randomizerService.config.setDefault(configPrefix + "periodical", false);
        randomizerService.config.setDefault(configPrefix + "on_damage", false);
        randomizerService.config.setDefault(configPrefix + "interval.min", 60 * 5);
        randomizerService.config.setDefault(configPrefix + "interval.max", 60 * 10);

        randomizerService.config.saveSafe();

        tick = 0;
        interval = getRandomInterval();

        tickTask = Bukkit.getScheduler().runTaskTimer(Bamboo.INS, this::tick, 0, 20);

        materials = new ArrayList<>();
        for (Material material : Material.values()) {
            if (material.isItem()) {
                materials.add(material);
            }
        }
    }

    @Override
    public void onDisable() {
        if (tickTask != null) {
            tickTask.cancel();
            tickTask = null;
        }

        materials = null;
    }

    @Override
    public void onTest() {
        Bukkit.getOnlinePlayers().forEach(this::randomizePlayer);
    }

    private void tick() {
        RandomizerService randomizerService = Bamboo.INS.serviceManager.getService(RandomizerService.class);
        if (randomizerService == null || !TimerService.isResumed() || !randomizerService.config.get().getBoolean(configPrefix + "periodical")) {
            return;
        }

        tick++;
        if (tick > interval) {
            tick = 0;
            Bukkit.getOnlinePlayers().forEach(this::randomizePlayer);
            Bamboo.INS.logDev("[Randomizer/InvRandomizer] Randomized due to the periodical event");
        }
    }

    public int getRandomInterval() {
        RandomizerService randomizerService = Bamboo.INS.serviceManager.getService(RandomizerService.class);
        if (randomizerService == null) {
            return Integer.MAX_VALUE;
        }

        int min = randomizerService.config.get().getInt(configPrefix + "interval.min", -1);
        int max = randomizerService.config.get().getInt(configPrefix + "interval.max", -1);
        if (min == -1 || max == -1) {
            return Integer.MAX_VALUE;
        }
        return Utils.randint(min, max);
    }

    public void randomizePlayer(Player player) {
        if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
            for (ItemStack itemStack : player.getInventory()) {
                if (itemStack != null) {
                    final Material material = getRandomMaterial();
                    if (material != null) {
                        itemStack.setType(material);
                    }
                }
            }
        }
    }

    public Material getRandomMaterial() {
        if (materials != null && !materials.isEmpty()) {
            return materials.get(Utils.RANDOM.nextInt(materials.size()));
        }
        return null;
    }
}
