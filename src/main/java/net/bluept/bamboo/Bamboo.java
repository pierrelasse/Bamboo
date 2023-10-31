package net.bluept.bamboo;

import net.bluept.bamboo.listener.Listeners;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceManager;
import net.bluept.bamboo.services.emoji.EmojiService;
import net.bluept.bamboo.services.health.HealthService;
import net.bluept.bamboo.util.DisplayHelper;
import net.bluept.bamboo.services.command.CommandService;
import net.bluept.bamboo.services.dimtp.DimTPService;
import net.bluept.bamboo.services.forceitembattle.ForceItemBattleService;
import net.bluept.bamboo.services.kmswitch.KMSwitchService;
import net.bluept.bamboo.services.timer.TimerService;
import net.bluept.bamboo.services.translation.TranslationService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Random;

public class Bamboo extends JavaPlugin {
    public static Bamboo INS;

    public ServiceManager serviceManager;
    public File serverRoot;
    public File configRoot;

    public Random random;

    @Override
    public void onLoad() {
        INS = this;

        random = new Random();
        random.setSeed(System.currentTimeMillis());

        serverRoot = Bukkit.getPluginsFolder().getParentFile();
        configRoot = getDataFolder();
        configRoot.mkdir();

        serviceManager = new ServiceManager();

        serviceManager.registerService(new TranslationService());
        serviceManager.registerService(new TimerService());
        serviceManager.registerService(new CommandService());
        serviceManager.registerService(new EmojiService());
        serviceManager.registerService(new HealthService());

        serviceManager.registerService(new DimTPService());
        serviceManager.registerService(new ForceItemBattleService());
        serviceManager.registerService(new KMSwitchService());

        getLogger().info("System loaded!");
    }

    @Override
    public void onEnable() {
        serviceManager.startService(serviceManager.getServiceId(TranslationService.class));
        serviceManager.startService(serviceManager.getServiceId(TimerService.class));
        serviceManager.startService(serviceManager.getServiceId(CommandService.class));
        serviceManager.startService(serviceManager.getServiceId(EmojiService.class));
        serviceManager.startService(serviceManager.getServiceId(HealthService.class));

        final int gameId = getConfig().getInt("gameId");
        final Class<? extends Service> gameService = switch (gameId) {
            case 1 -> ForceItemBattleService.class;
            case 2 -> DimTPService.class;
            case 3 -> KMSwitchService.class;
            default -> null;
        };
        if (gameService != null) {
            getLogger().info("GameId: " + gameId);
            serviceManager.startService(serviceManager.getServiceId(gameService));
        }

        getServer().getPluginManager().registerEvents(new Listeners(), this);

        getLogger().info("System started! By Vertickt & pierrelasse @ https://github.com/pierrelasse/Bamboo");
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

        INS = null;

        getLogger().info("System stopped successfully!");
    }
}
