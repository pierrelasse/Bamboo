package net.bluept.bamboo.services.dep.backpack;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.services.challenges.randomizer.Listeners;
import net.bluept.bamboo.util.Config;
import net.bluept.bamboo.util.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class BackpackService extends Service {
    public Config config;
    public Inventory inventory;
    private Listeners listeners;

    @Override
    public void onEnable() {
        if (config != null) {
            config.saveSafe();
        }
        config = new Config(new File(Bamboo.INS.configRoot, "backpack.yml"));

        int rows = config.get().getInt("rows", 6);
        if (rows < 1 || rows > 6) {
            rows = 6;
        }
        inventory = Bukkit.createInventory(null, 9 * rows, Component.text(Utils.colorfy("&d&lCheckpack")));

        if (config.get().isSet("content")) {
            Object contentObj = config.get().get("content");
            if (contentObj instanceof ItemStack[] content) {
                inventory.setContents(content);
            }
        }

        config.saveSafe();

        if (listeners != null) {
            HandlerList.unregisterAll(listeners);
            listeners = null;
        }
        Bamboo.INS.getServer().getPluginManager().registerEvents(listeners = new Listeners(), Bamboo.INS);
    }

    @Override
    public void onDisable() {
        if (listeners != null) {
            HandlerList.unregisterAll(listeners);
            listeners = null;
        }

        if (config != null) {
            if (inventory != null) {
                config.get().set("content", inventory.getContents());
            }

            config.saveSafe();
            config = null;
        }
    }
}
