package net.bluept.forceitembattle;

import net.bluept.forceitembattle.service.ServiceManager;
import net.bluept.forceitembattle.services.actionbar.ActionbarService;
import net.bluept.forceitembattle.services.item.ItemService;
import net.bluept.forceitembattle.services.timer.TimerService;
import org.bukkit.plugin.java.JavaPlugin;

public class ForceItemBattle extends JavaPlugin {
    public static ForceItemBattle INSTANCE;

    public ServiceManager serviceManager;

    @Override
    public void onLoad() {
        INSTANCE = this;

        serviceManager = new ServiceManager();

        serviceManager.registerService("item", new ItemService());
        serviceManager.registerService("timer", new TimerService());
        serviceManager.registerService("actionbar", new ActionbarService());

        INSTANCE.getLogger().info("System loaded!");
    }

    @Override
    public void onEnable() {
        INSTANCE.getLogger().info("Starting system");

        for (String service : serviceManager.getServices()) {
            serviceManager.startService(service);
        }

        INSTANCE.getLogger().info("System started!");
    }

    @Override
    public void onDisable() {
        INSTANCE.getLogger().info("Shutting system down");

        for (String service : serviceManager.getServices()) {
            serviceManager.stopService(service);
        }

        INSTANCE.getLogger().info("System stopped successfully!");
    }
}
