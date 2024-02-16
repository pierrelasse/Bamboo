package net.bluept.bamboo.services.challenges.randomizer;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;
import net.bluept.bamboo.service.ServiceManager;
import net.bluept.bamboo.services.dep.display.DisplayController;
import net.bluept.bamboo.util.Config;
import org.bukkit.event.HandlerList;

import java.io.File;

@ServiceInfo(description = "Host von sub servicen. Zuständig für das randomizen verschiedener sachen cock")
public class RandomizerService extends Service {
    public Config config;
    private Listeners listeners;

    @Override
    public void onEnable() {
        config = new Config(new File(Bamboo.INS.configRoot, "randomizer.yml"));
        config.saveSafe();

        if (listeners != null) {
            HandlerList.unregisterAll(listeners);
            listeners = null;
        }
        Bamboo.INS.getServer().getPluginManager().registerEvents(listeners = new Listeners(), Bamboo.INS);

        ServiceManager serviceManager = Bamboo.INS.serviceManager;
        serviceManager.registerService(new InvRandomizerService());
        serviceManager.registerService(new StepRandomizerService());
        serviceManager.registerService(new BlockPlaceRandomizerService());

        DisplayController.push();
    }

    @Override
    public void onDisable() {
        DisplayController.pop();

        ServiceManager serviceManager = Bamboo.INS.serviceManager;
        serviceManager.unregisterService(InvRandomizerService.class);
        serviceManager.unregisterService(StepRandomizerService.class);
        serviceManager.unregisterService(BlockPlaceRandomizerService.class);

        if (listeners != null) {
            HandlerList.unregisterAll(listeners);
            listeners = null;
        }
    }
}
