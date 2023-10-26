package net.bluept.forceitembattle.services.item;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.service.Service;
import net.bluept.forceitembattle.services.display.DisplayService;
import net.bluept.forceitembattle.services.tablist.TablistService;
import net.bluept.forceitembattle.services.timer.TimerService;
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
    private final Random random = new Random();
    public List<String> materials;
    public Map<UUID, Material> playerMaterials;
    public Map<UUID, Integer> playerItems;
    public FileConfiguration itemsConfig;
    public File itemsConfigFile;

    @Override
    public void onEnable() {
        materials = new ArrayList<>();
        playerMaterials = new HashMap<>();
        playerItems = new HashMap<>();
        random.setSeed(System.currentTimeMillis());
        loadConfig();
    }

    @Override
    public void onDisable() {
    }

    public Material getRandomMaterial() {
        return Material.getMaterial(materials.get(random.nextInt(materials.size())));
    }

    public Material getPlayerMaterial(UUID uuid) {
        return playerMaterials.computeIfAbsent(uuid, k -> getRandomMaterial());
    }

    public Material nextPlayerMaterial(UUID uuid) {
        playerMaterials.remove(uuid);
        return getPlayerMaterial(uuid);
    }

    public void addPlayerCollection(UUID uuid) {
        playerItems.put(uuid, playerItems.getOrDefault(uuid, 0) + 1);
    }

    public void handlePickup(EntityPickupItemEvent event) {
        Player player = (Player)event.getEntity();
        Material playerMaterial = getPlayerMaterial(player.getUniqueId());

        if (event.getItem().getItemStack().getType() == playerMaterial) {
            nextPlayerMaterial(player.getUniqueId());
            addPlayerCollection(player.getUniqueId());

            player.playSound(player.getLocation(), "bluept:pling", SoundCategory.PLAYERS, 1F, 1F);

            TablistService tablistService = ForceItemBattle.INS.serviceManager.getService(TablistService.class);
            if (tablistService != null) {
                tablistService.updatePlayer(this, player);
            }
            DisplayService displayService = ForceItemBattle.INS.serviceManager.getService(DisplayService.class);
            if (displayService != null) {
                displayService.updatePlayer(this, player);
            }
        }
    }

    public void loadConfig() {
        itemsConfigFile = new File(ForceItemBattle.INS.configRoot, "items.yml");
        itemsConfig = YamlConfiguration.loadConfiguration(itemsConfigFile);

        if (itemsConfig.getBoolean("update_materials", true)) {
            addMaterialsToConfig();
            itemsConfig.set("update_materials", false);
            saveConfig();
        }

        materials.clear();
        materials.addAll(itemsConfig.getStringList("whitelisted_materials"));
    }

    public void saveConfig() {
        try {
            itemsConfig.save(itemsConfigFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void addMaterialsToConfig() {
        List<String> whitelistedMaterials = new ArrayList<>();

        for (Material material : Material.values()) {
            if (material.isItem() && !material.isAir() && !material.isEmpty() && !material.isLegacy()) {
                String id = material.name();
                if (!whitelistedMaterials.contains(id)) {
                    whitelistedMaterials.add(id);
                }
            }
        }

        itemsConfig.set("whitelisted_materials", whitelistedMaterials);
        materials.clear();
        materials.addAll(whitelistedMaterials);
    }

    public int getPlayerItems(UUID uuid) {
        return playerItems.getOrDefault(uuid, 0);
    }
}
