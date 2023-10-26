package net.bluept.forceitembattle.services.item;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.service.Service;
import net.bluept.forceitembattle.services.tablist.TablistService;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPickupItemEvent;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ItemService extends Service {
    private Random random;
    private Map<UUID, Material> playerMaterials;
    private Map<UUID, Integer> playerItems;
    private FileConfiguration itemsConfig;
    private File itemsConfigFile;

    @Override
    public void onEnable() {
        random = new Random();
        playerMaterials = new HashMap<>();
        playerItems = new HashMap<>();
        generateConfig();
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    public Material getRandomMaterial() {
        return Material.values()[random.nextInt(Material.values().length)];
    }

    public Material getPlayerMaterial(UUID uuid) {
        return playerMaterials.computeIfAbsent(uuid, k -> getRandomMaterial());
    }

    public Material nextPlayerMaterial(UUID uuid) {
        playerMaterials.remove(uuid);
        return getPlayerMaterial(uuid);
    }

    public void addPlayerCollection(UUID uuid) {
        playerItems.compute(uuid, (k, v) -> (v == null ? 1 : v + 1));
    }

    public void handlePickup(EntityPickupItemEvent event) {
        Player player = (Player)event.getEntity();
        Material playerMaterial = getPlayerMaterial(player.getUniqueId());
        if (event.getItem().getItemStack().getType() == playerMaterial) {
            nextPlayerMaterial(player.getUniqueId());
            addPlayerCollection(player.getUniqueId());

            player.playSound(player.getLocation(), "bluept:pling", SoundCategory.PLAYERS, 1F, 1F);

            TablistService tablistService = ForceItemBattle.INSTANCE.serviceManager.getServiceHandle("tablist", TablistService.class);
            if (tablistService != null) {
                tablistService.updatePlayer(this, player);
            }
        }
    }

    public void generateConfig() {
        itemsConfigFile = new File(ForceItemBattle.INSTANCE.configRoot, "items.yml");
        itemsConfig = YamlConfiguration.loadConfiguration(itemsConfigFile);

        if (itemsConfig.getBoolean("update_materials", true)) {
            addMaterialsToConfig();
        }

        itemsConfig.set("update_materials", false);

        saveConfig();
    }

    public void saveConfig() {
        try {
            itemsConfig.save(itemsConfigFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void addMaterialsToConfig() {
        List<String> materials = itemsConfig.getStringList("whitelisted_materials");
        Arrays.stream(Material.values()).map(Material::toString).filter(value -> !materials.contains(value)).forEach(materials::add);
        itemsConfig.set("whitelisted_materials", materials);
    }
}
