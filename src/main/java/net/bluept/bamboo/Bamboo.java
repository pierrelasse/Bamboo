package net.bluept.bamboo;

import net.bluept.bamboo.listener.Listeners;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceManager;
import net.bluept.bamboo.services.command.CommandService;
import net.bluept.bamboo.services.dimtp.DimTPService;
import net.bluept.bamboo.services.display.DisplayService;
import net.bluept.bamboo.services.emoji.EmojiService;
import net.bluept.bamboo.services.forceitembattle.ForceItemBattleService;
import net.bluept.bamboo.services.goal.GoalService;
import net.bluept.bamboo.services.health.HealthService;
import net.bluept.bamboo.services.kmswitch.KMSwitchService;
import net.bluept.bamboo.services.multiplier.MultiplierService;
import net.bluept.bamboo.services.randomizer.RandomizerService;
import net.bluept.bamboo.services.test.TestService;
import net.bluept.bamboo.services.timer.TimerService;
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
        try {
            INS = this;

            random = new Random();
            random.setSeed(System.currentTimeMillis());

            serverRoot = Bukkit.getPluginsFolder().getParentFile();
            configRoot = getDataFolder();
            configRoot.mkdir();

            serviceManager = new ServiceManager();

            serviceManager.registerService(new TimerService());
            serviceManager.registerService(new DisplayService());
            serviceManager.registerService(new CommandService());
            serviceManager.registerService(new EmojiService());
            serviceManager.registerService(new HealthService());
            serviceManager.registerService(new TestService());
            serviceManager.registerService(new GoalService());

            serviceManager.registerService(new DimTPService());
            serviceManager.registerService(new ForceItemBattleService());
            serviceManager.registerService(new KMSwitchService());
            serviceManager.registerService(new MultiplierService());
            serviceManager.registerService(new RandomizerService());

            Bukkit.setMaxPlayers(2023);

            getLogger().info("System loaded!");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        try {
            serviceManager.startService(serviceManager.getServiceId(TimerService.class));
            serviceManager.startService(serviceManager.getServiceId(CommandService.class));
            serviceManager.startService(serviceManager.getServiceId(EmojiService.class));

            final int gameId = getConfig().getInt("gameId");
            final Class<? extends Service> gameService = switch (gameId) {
                case 1 -> ForceItemBattleService.class;
                case 2 -> DimTPService.class;
                case 3 -> KMSwitchService.class;
                case 4 -> MultiplierService.class;
                case 5 -> RandomizerService.class;
                default -> null;
            };
            if (gameService != null) {
                getLogger().info("GameId: " + gameId);
                serviceManager.startService(serviceManager.getServiceId(gameService));
            }

            getServer().getPluginManager().registerEvents(new Listeners(), this);

            getLogger().info("System started! By Vertickt & pierrelasse @ https://github.com/pierrelasse/Bamboo");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
