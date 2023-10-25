package net.bluept.forceitembattle.services.item;

import net.bluept.forceitembattle.service.Service;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class ItemService extends Service {
    private Random random;
    private Map<UUID, Material> playerMaterials;

    @Override
    public void onEnable() {
        random = new Random();
        playerMaterials = new HashMap<>();
    }

    @Override
    public void onDisable() {
    }

    public Material getRandomMaterial() {
        return Material.values()[random.nextInt(Material.values().length)];
    }

    public Material getPlayerMaterial(UUID uuid) {
        if (!playerMaterials.containsKey(uuid)) {
            playerMaterials.put(uuid, getRandomMaterial());
        }
        return playerMaterials.get(uuid);
    }

    public void clearPlayerMaterials() {
        playerMaterials.clear();
    }
}
