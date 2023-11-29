package net.bluept.bamboo.services.randomizer;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

@ServiceInfo(id = "randomizer/invrandomizer")
public class InvRandomizerService extends Service {
    public int tick;
    public int interval;
    private BukkitTask tickTask;
    private List<Material> materials;

    @Override
    public void onEnable() {
        tick = 0;

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

    private void tick() {
        tick++;
        if (tick > 30) {
            tick = 0;
            Bukkit.getOnlinePlayers().forEach(this::randomizePlayer);
        }
    }

    public int getRandomInterval() {
        RandomizerService randomizerService = Bamboo.INS.serviceManager.getService(RandomizerService.class);
        if (randomizerService == null) {
            return Integer.MAX_VALUE;
        }

        randomizerService.config.setDefault("");
        randomizerService.config.setDefault("");

        return Utils.randint();
    }

    @Override
    public void onTest() {
        Bukkit.getOnlinePlayers().forEach(this::randomizePlayer);
    }

    public void randomizePlayer(Player player) {
        for (ItemStack itemStack : player.getInventory()) {
            if (itemStack != null) {
                final Material material = getRandomMaterial();
                if (material != null) {
                    itemStack.setType(material);
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
