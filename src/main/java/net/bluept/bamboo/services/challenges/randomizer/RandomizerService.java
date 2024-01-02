package net.bluept.bamboo.services.challenges.randomizer;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceManager;
import net.bluept.bamboo.services.system.command.CommandService;
import net.bluept.bamboo.services.dep.display.DisplayController;
import net.bluept.bamboo.services.challenges.randomizer.commands.RandomizerDevCmd;
import net.bluept.bamboo.util.Config;
import org.bukkit.event.HandlerList;

import java.io.File;

public class RandomizerService extends Service {
    public Config config;
    private Listeners listeners;
    private RandomizerDevCmd randomizerDevCmd;

    @Override
    public void onEnable() {
        config = new Config(new File(Bamboo.INS.configRoot, "randomizer.yml"));
        config.saveSafe();

        if (listeners != null) {
            HandlerList.unregisterAll(listeners);
            listeners = null;
        }
        Bamboo.INS.getServer().getPluginManager().registerEvents(listeners = new Listeners(), Bamboo.INS);

        CommandService commandService = Bamboo.INS.serviceManager.getService(CommandService.class);
        if (commandService != null) {
            commandService.registerCommand(randomizerDevCmd = new RandomizerDevCmd());
        }

        ServiceManager serviceManager = Bamboo.INS.serviceManager;
        serviceManager.registerService(new InvRandomizerService());
        serviceManager.registerService(new StepRandomizerService());

        DisplayController.push();
    }

    @Override
    public void onDisable() {
        DisplayController.pop();

        ServiceManager serviceManager = Bamboo.INS.serviceManager;
        serviceManager.unregisterService(InvRandomizerService.class);
        serviceManager.unregisterService(StepRandomizerService.class);

        CommandService commandService = Bamboo.INS.serviceManager.getService(CommandService.class);
        if (commandService != null) {
            commandService.unregisterCommand(randomizerDevCmd);
        }

        if (listeners != null) {
            HandlerList.unregisterAll(listeners);
            listeners = null;
        }
    }
}
