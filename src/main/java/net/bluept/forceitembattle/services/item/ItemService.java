package net.bluept.forceitembattle.services.item;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.service.Service;
import net.bluept.forceitembattle.services.display.DisplayService;
import net.bluept.forceitembattle.services.tablist.TablistService;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPickupItemEvent;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ItemService extends Service {
    public static final int MAX_JOKER = 3;
    private final Random random = new Random();
    public List<String> materials;
    public Map<UUID, Material> playerMaterials;
    public Map<UUID, Integer> playerItems;
    public Map<UUID, Integer> playerJoker;
    public FileConfiguration itemsConfig;
    public File itemsConfigFile;

    @Override
    public void onEnable() {
        materials = new ArrayList<>();

        playerMaterials = new HashMap<>();
        ConfigurationSection materialsSection = ForceItemBattle.INS.getConfig().getConfigurationSection("stats.materials");
        if (materialsSection != null) {
            for (String key : materialsSection.getKeys(false)) {
                Object v = materialsSection.get(key);
                if (v instanceof String) {
                    Material material = Material.getMaterial((String)v);
                    if (material != null) {
                        playerMaterials.put(UUID.fromString(key), material);
                    }
                }
            }
        }

        playerItems = new HashMap<>();
        ConfigurationSection itemsSection = ForceItemBattle.INS.getConfig().getConfigurationSection("stats.items");
        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                Object v = itemsSection.get(key);
                if (v instanceof Integer) {
                    playerItems.put(UUID.fromString(key), (Integer)v);
                }
            }
        }

        playerJoker = new HashMap<>();
        ConfigurationSection jokerSection = ForceItemBattle.INS.getConfig().getConfigurationSection("stats.joker");
        if (jokerSection != null) {
            for (String key : jokerSection.getKeys(false)) {
                Object v = jokerSection.get(key);
                if (v instanceof Integer) {
                    playerJoker.put(UUID.fromString(key), (Integer)v);
                }
            }
        }

        random.setSeed(System.currentTimeMillis());
        loadConfig();
    }

    @Override
    public void onDisable() {
        ConfigurationSection materialsSection = ForceItemBattle.INS.getConfig().createSection("stats.materials");
        for (Map.Entry<UUID, Material> entry : playerMaterials.entrySet()) {
            materialsSection.set(entry.getKey().toString(), entry.getValue().name());
        }

        ConfigurationSection itemsSection = ForceItemBattle.INS.getConfig().createSection("stats.items");
        for (Map.Entry<UUID, Integer> entry : playerItems.entrySet()) {
            itemsSection.set(entry.getKey().toString(), entry.getValue());
        }

        ConfigurationSection jokerSection = ForceItemBattle.INS.getConfig().createSection("stats.joker");
        for (Map.Entry<UUID, Integer> entry : playerItems.entrySet()) {
            jokerSection.set(entry.getKey().toString(), entry.getValue());
        }
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

    public int getJokerLeft(UUID uuid) {
        int amount = MAX_JOKER - playerJoker.getOrDefault(uuid, 0);
        return Math.max(amount, 0);
    }

    public boolean consumeJoker(UUID uuid) {
        int amount = playerJoker.getOrDefault(uuid, 0);
        boolean canConsume = amount < MAX_JOKER;
        if (canConsume) {
            playerJoker.put(uuid, amount + 1);
        }
        return canConsume;
    }
}
