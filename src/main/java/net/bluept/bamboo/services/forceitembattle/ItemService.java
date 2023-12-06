package net.bluept.bamboo.services.forceitembattle;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;
import net.bluept.bamboo.util.Config;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

@ServiceInfo(id = "forceitembattle/item")
public class ItemService extends Service {
    public static final int MAX_JOKER = 3;
    public Config statsConfig;
    public Config itemsConfig;

    public List<Material> materials;
    public Map<UUID, Material> playerMaterials;
    public Map<UUID, Integer> playerItems;
    public Map<UUID, Integer> playerJoker;

    @Override
    public void onEnable() {
        materials = new ArrayList<>();

        statsConfig = new Config(new File(Bamboo.INS.configRoot, "forceitembattle_stats.yml"));
        statsConfig.load();
        itemsConfig = new Config(new File(Bamboo.INS.configRoot, "forceitembattle_items.yml"));
        itemsConfig.load();

        playerMaterials = new HashMap<>();
        ConfigurationSection materialsSection = statsConfig.get().getConfigurationSection("materials");
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
        ConfigurationSection itemsSection = statsConfig.get().getConfigurationSection("items");
        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                Object v = itemsSection.get(key);
                if (v instanceof Integer) {
                    playerItems.put(UUID.fromString(key), (Integer)v);
                }
            }
        }

        playerJoker = new HashMap<>();
        ConfigurationSection jokerSection = statsConfig.get().getConfigurationSection("joker");
        if (jokerSection != null) {
            for (String key : jokerSection.getKeys(false)) {
                Object v = jokerSection.get(key);
                if (v instanceof Integer) {
                    playerJoker.put(UUID.fromString(key), (Integer)v);
                }
            }
        }

        Utils.RANDOM.setSeed(System.currentTimeMillis());
        loadConfig();
    }

    @Override
    public void onDisable() {
        ConfigurationSection materialsSection = statsConfig.get().createSection("materials");
        for (Map.Entry<UUID, Material> entry : playerMaterials.entrySet()) {
            materialsSection.set(entry.getKey().toString(), entry.getValue().name());
        }

        ConfigurationSection itemsSection = statsConfig.get().createSection("items");
        for (Map.Entry<UUID, Integer> entry : playerItems.entrySet()) {
            itemsSection.set(entry.getKey().toString(), entry.getValue());
        }

        ConfigurationSection jokerSection = statsConfig.get().createSection("joker");
        for (Map.Entry<UUID, Integer> entry : playerItems.entrySet()) {
            jokerSection.set(entry.getKey().toString(), entry.getValue());
        }

        statsConfig.saveSafe();
    }

    public Material getRandomMaterial() {
        return materials.get(Utils.RANDOM.nextInt(materials.size()));
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
            collectItem(player);
        }
    }

    public void collectItem(Player player) {
        nextPlayerMaterial(player.getUniqueId());
        addPlayerCollection(player.getUniqueId());

        player.playSound(player.getLocation(), "bluept:pling", SoundCategory.VOICE, 1F, 1F);

        TablistService tablistService = Bamboo.INS.serviceManager.getService(TablistService.class);
        if (tablistService != null) {
            tablistService.updatePlayer(this, player);
        }
        DisplayService displayService = Bamboo.INS.serviceManager.getService(DisplayService.class);
        if (displayService != null) {
            displayService.displayActionbar(this, player);
        }
    }

    public void loadConfig() {
        if (!itemsConfig.file.exists()) {
            List<String> materials = new ArrayList<>();
            for (Material material : Material.values()) {
                if (material.isItem() && !material.isAir() && !material.isEmpty() && !material.isLegacy()) {
                    materials.add(material.name());
                }
            }
            itemsConfig.get().set("whitelisted_materials", materials);
            itemsConfig.saveSafe();
        }

        materials.clear();
        for (String id : itemsConfig.get().getStringList("whitelisted_materials")) {
            Material material = Material.getMaterial(id);
            if (material != null && material.isItem()) {
                materials.add(material);
            }
        }
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

    public void handleClick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem != null && currentItem.getType() == getPlayerMaterial(player.getUniqueId())) {
            collectItem(player);
        }
    }
}
