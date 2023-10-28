package net.bluept.bamboo;

import net.bluept.bamboo.listener.Listeners;
import net.bluept.bamboo.service.ServiceManager;
import net.bluept.bamboo.services.command.CommandService;
import net.bluept.bamboo.services.forceitembattle.ForceItemBattleService;
import net.bluept.bamboo.services.timer.TimerService;
import net.bluept.bamboo.services.translation.TranslationService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Bamboo extends JavaPlugin {
    public static Bamboo INS;

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
        serviceManager.registerService(new TimerService());
        serviceManager.registerService(new CommandService());

        serviceManager.registerService(new ForceItemBattleService());

        getLogger().info("System loaded!");
    }

    @Override
    public void onEnable() {
        serviceManager.startService(serviceManager.getServiceId(TranslationService.class));
        serviceManager.startService(serviceManager.getServiceId(TimerService.class));
        serviceManager.startService(serviceManager.getServiceId(CommandService.class));
        serviceManager.startService(serviceManager.getServiceId(ForceItemBattleService.class));

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
        }

        saveConfig();

        INS = null;

        getLogger().info("System stopped successfully!");
    }
}
