package net.bluept.forceitembattle;

import net.bluept.forceitembattle.listener.Listeners;
import net.bluept.forceitembattle.service.ServiceManager;
import net.bluept.forceitembattle.services.command.CommandService;
import net.bluept.forceitembattle.services.display.DisplayService;
import net.bluept.forceitembattle.services.item.ItemService;
import net.bluept.forceitembattle.services.itemdisplay.ItemDisplayService;
import net.bluept.forceitembattle.services.tablist.TablistService;
import net.bluept.forceitembattle.services.timer.TimerService;
import net.bluept.forceitembattle.services.translation.TranslationService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ForceItemBattle extends JavaPlugin {
    public static ForceItemBattle INS;

    public ServiceManager serviceManager;
    public File serverRoot;
    public File configRoot;

    @Override
    public void onLoad() {
        INS = this;

        serverRoot = Bukkit.getPluginsFolder().getParentFile();
        configRoot = getDataFolder();
        configRoot.mkdir();

        saveConfig();

        serviceManager = new ServiceManager();

        serviceManager.registerService(new TranslationService());
        serviceManager.registerService(new ItemService());
        serviceManager.registerService(new DisplayService());
        serviceManager.registerService(new TimerService());
        serviceManager.registerService(new TablistService());
        serviceManager.registerService(new CommandService());
        serviceManager.registerService(new ItemDisplayService());

        getLogger().info("System loaded!");
    }

    @Override
    public void onEnable() {
        for (String service : serviceManager.getServices()) {
            try {
                long startTime = System.nanoTime();
                serviceManager.startService(service);
                getLogger().info("Service '" + service + "' started (" + (System.nanoTime() - startTime) + "ns)");
            } catch (Exception ex) {
                ex.printStackTrace();
                getLogger().info("Error while starting service '" + service + "'");
            }
        }

        getServer().getPluginManager().registerEvents(new Listeners(), this);

        getLogger().info("System started!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Shutting system down");

        CommandService commandService = serviceManager.getService(CommandService.class);
        if (commandService != null) {
            commandService.registeredCommands = null;
        }

        for (String service : serviceManager.getServices()) {
            serviceManager.stopService(service);
            getLogger().info("Service '" + service + "' stopped");
        }

        saveConfig();

        INS = null;

        getLogger().info("System stopped successfully!");
    }
}
