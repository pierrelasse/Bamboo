package net.bluept.bamboo;

import net.bluept.bamboo.listener.Listeners;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceManager;
import net.bluept.bamboo.services.challenges.cratermater.CraterMaterService;
import net.bluept.bamboo.services.challenges.dimtp.dimtp3.DimTP3Service;
import net.bluept.bamboo.services.system.appearance.AppearanceService;
import net.bluept.bamboo.services.dep.backpack.BackpackService;
import net.bluept.bamboo.services.system.command.CommandService;
import net.bluept.bamboo.services.challenges.dimtp.DimTPService;
import net.bluept.bamboo.services.dep.display.DisplayService;
import net.bluept.bamboo.services.system.emoji.EmojiService;
import net.bluept.bamboo.services.challenges.forceitembattle.ForceItemBattleService;
import net.bluept.bamboo.services.challenges.forcemobbattle.ForceMobBattleService;
import net.bluept.bamboo.services.other.goal.GoalService;
import net.bluept.bamboo.services.other.health.HealthService;
import net.bluept.bamboo.services.challenges.kmswitch.KMSwitchService;
import net.bluept.bamboo.services.challenges.multiplier.MultiplierService;
import net.bluept.bamboo.services.challenges.randomizer.RandomizerService;
import net.bluept.bamboo.services.other.test.TestService;
import net.bluept.bamboo.services.dep.timer.TimerService;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Bamboo extends JavaPlugin {
    public static Bamboo INS;

    public ServiceManager serviceManager;
    public File serverRoot;
    public File configRoot;

    @Override
    public void onLoad() {
        try {
            INS = this;
            Utils.RANDOM.setSeed(System.currentTimeMillis());

            serverRoot = Bukkit.getPluginsFolder().getParentFile();
            configRoot = getDataFolder();
            configRoot.mkdir();

            serviceManager = new ServiceManager();

            serviceManager.registerService(new TimerService());
            serviceManager.registerService(new DisplayService());
            serviceManager.registerService(new CommandService());
            serviceManager.registerService(new AppearanceService());
            serviceManager.registerService(new BackpackService());
            serviceManager.registerService(new EmojiService());
            serviceManager.registerService(new HealthService());
            serviceManager.registerService(new GoalService());
            serviceManager.registerService(new TestService());

            serviceManager.registerService(new DimTPService());
            serviceManager.registerService(new DimTP3Service());
            serviceManager.registerService(new ForceItemBattleService());
            serviceManager.registerService(new KMSwitchService());
            serviceManager.registerService(new MultiplierService());
            serviceManager.registerService(new RandomizerService());
            serviceManager.registerService(new CraterMaterService());

            logDev("System loaded!");

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
            serviceManager.startService(serviceManager.getServiceId(AppearanceService.class));

            final int gameId = getConfig().getInt("gameId");
            final Class<? extends Service> gameService = switch (gameId) {
                case 1 -> ForceItemBattleService.class;
                case 2 -> DimTPService.class;
                case 3 -> KMSwitchService.class;
                case 4 -> MultiplierService.class;
                case 5 -> RandomizerService.class;
                case 6 -> ForceMobBattleService.class;
                default -> null;
            };
            if (gameService != null) {
                logDev("GameId: " + gameId);
                serviceManager.startService(serviceManager.getServiceId(gameService));
            }

            getServer().getPluginManager().registerEvents(new Listeners(), this);

            logDev("System started! By Vertickt & pierrelasse @ https://github.com/pierrelasse/Bamboo");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        logDev("Shutting system down");

        CommandService commandService = serviceManager.getService(CommandService.class);
        if (commandService != null) {
            commandService.registeredCommands = null;
        }

        for (String service : serviceManager.getServices()) {
            serviceManager.stopService(service);
        }

        INS = null;

        logDev("System stopped successfully!");
    }

    public void logDev(String s) {
        getLogger().info(s);
        s = Utils.colorfy("&8> &7&o" + s);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.isOp()) {
                onlinePlayer.sendMessage(s);
            }
        }
    }
}
