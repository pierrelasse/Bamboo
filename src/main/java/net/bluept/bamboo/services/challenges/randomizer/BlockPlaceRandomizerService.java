package net.bluept.bamboo.services.challenges.randomizer;

import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;

@ServiceInfo(id = "randomizer/blockplacerandomizer")
public class BlockPlaceRandomizerService extends Service {
    private final ArrayList<Material> materials = new ArrayList<>();

    @Override
    public void onEnable() {
        registerListener();

        materials.clear();
        for (Material material : Material.values()) {
            if (material.isBlock() && !material.isAir() && material.isOccluding() && !material.isLegacy()) {
                BlockData blockData = material.createBlockData();
                if (blockData instanceof Waterlogged wBlockData) {
                    if (wBlockData.isWaterlogged()) {
                        continue;
                    }
                }
                materials.add(material);
            }
        }
    }

    @Override
    public void onDisable() {
        unregisterListener();
    }

    public Material getRandomMaterial() {
        return materials.get(Utils.RANDOM.nextInt(materials.size()));
    }

    @EventHandler
    private void event(final BlockPlaceEvent event) {
        event.getBlock().setType(getRandomMaterial());
    }
}
