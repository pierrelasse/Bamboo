package net.bluept.forceitembattle;

import net.bluept.forceitembattle.service.ServiceManager;
import net.bluept.forceitembattle.services.actionbar.ActionbarService;
import net.bluept.forceitembattle.services.command.CommandService;
import net.bluept.forceitembattle.services.item.ItemService;
import net.bluept.forceitembattle.services.timer.TimerService;
import net.bluept.forceitembattle.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.util.List;

public class ForceItemBattle extends JavaPlugin {
    public static ForceItemBattle INSTANCE;

    public ServiceManager serviceManager;

    @Override
    public void onLoad() {
        INSTANCE = this;

        saveConfig();
        if (getConfig().isBoolean("reset_world")) {
            List<String> blacklistedFolders = List.of("datapacks", "paper-world.yml");
            for (String world : List.of("world", "world_nether", "world_the_end")) {
                File folder = new File(Bukkit.getPluginsFolder().getParentFile(), world);
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
        }

        serviceManager = new ServiceManager();

        serviceManager.registerService("item", new ItemService());
        serviceManager.registerService("timer", new TimerService());
        serviceManager.registerService("actionbar", new ActionbarService());
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
