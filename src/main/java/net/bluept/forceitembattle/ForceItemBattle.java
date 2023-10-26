package net.bluept.forceitembattle;

import net.bluept.forceitembattle.listener.PickupListener;
import net.bluept.forceitembattle.service.ServiceManager;
import net.bluept.forceitembattle.services.command.CommandService;
import net.bluept.forceitembattle.services.display.DisplayService;
import net.bluept.forceitembattle.services.item.ItemService;
import net.bluept.forceitembattle.services.tablist.TablistService;
import net.bluept.forceitembattle.services.timer.TimerService;
import net.bluept.forceitembattle.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public class ForceItemBattle extends JavaPlugin {
    public static ForceItemBattle INSTANCE;

    public ServiceManager serviceManager;
    public File serverRoot;
    public File configRoot;

    @Override
    public void onLoad() {
        INSTANCE = this;

        serverRoot = Bukkit.getPluginsFolder().getParentFile();
        configRoot = getDataFolder();
        configRoot.mkdir();

        saveConfig();
        if (getConfig().isBoolean("reset_world")) {
            getLogger().info("Resetting worlds");

            List<String> blacklistedFolders = List.of("datapacks", "paper-world.yml");

            for (String world : List.of("world", "world_nether", "world_the_end")) {
                File folder = new File(serverRoot, world);
                File[] files = folder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (!blacklistedFolders.contains(file.getName())) {
                            Utils.rDelete(file, true);
                        }
                    }
                }
            }

            getConfig().set("reset_world", false);
            saveConfig();

            new File(serverRoot, "world/playerdata").mkdir();

            getLogger().info("Worlds reset!");
        }

        serviceManager = new ServiceManager();

        serviceManager.registerService("item", new ItemService());
        serviceManager.registerService("display", new DisplayService());
        serviceManager.registerService("timer", new TimerService());
        serviceManager.registerService("tablist", new TablistService());
        serviceManager.registerService("command", new CommandService());

        getLogger().info("System loaded!");
    }

    @Override
    public void onEnable() {
        getLogger().info("Starting system");

        for (String service : serviceManager.getServices()) {
            try {
                serviceManager.startService(service);
                getLogger().info("Service '" + service + "' started");
            } catch (Exception ex) {
                ex.printStackTrace();
                getLogger().info("Error while starting service '" + service + "'");
            }
        }

        getServer().getPluginManager().registerEvents(new PickupListener(), this);

        getLogger().info("System started!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Shutting system down");

        for (String service : serviceManager.getServices()) {
            serviceManager.stopService(service);
            getLogger().info("Service '" + service + "' stopped");
        }

        saveConfig();

        getLogger().info("System stopped successfully!");
    }
}
