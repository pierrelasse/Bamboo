package net.bluept.bamboo.services.randomizer;

import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;

import java.util.ArrayList;
import java.util.List;

@ServiceInfo(id = "randomizer/steprandomizer")
public class StepRandomizerService extends Service {
    public final List<Material> blacklisted_blocks = List.of(Material.END_PORTAL, Material.END_PORTAL_FRAME, Material.NETHER_PORTAL, Material.OBSIDIAN, Material.WATER, Material.LAVA);
    private List<Material> materials;

    @Override
    public void onEnable() {
        materials = new ArrayList<>();
        for (Material material : Material.values()) {
            if (material.isBlock() && !material.isAir() && material.isSolid()) {
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
        materials = null;
    }

    public Material getRandomMaterial() {
        return materials.get(Utils.RANDOM.nextInt(materials.size()));
    }
}
