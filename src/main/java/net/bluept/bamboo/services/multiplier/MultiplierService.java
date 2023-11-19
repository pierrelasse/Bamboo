package net.bluept.bamboo.services.multiplier;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.services.command.CommandService;
import net.bluept.bamboo.services.display.DisplayController;
import net.bluept.bamboo.services.multiplier.commands.MultiplierDevCmd;
import net.bluept.bamboo.services.timer.TimerService;
import net.bluept.bamboo.util.Config;
import net.bluept.bamboo.util.Utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MultiplierService extends Service {
    public Config config;
    public Map<String, Integer> multipliers;
    private Listeners listeners;
    private MultiplierDevCmd multiplierDevCmd;

    @Override
    public void onEnable() {
        multipliers = new HashMap<>();

        if (listeners != null) {
            listeners.unregister();
        }
        listeners = new Listeners();
        Bamboo.INS.getServer().getPluginManager().registerEvents(listeners, Bamboo.INS);

        config = new Config(new File(Bamboo.INS.configRoot, "multiplier.yml"));
        config.load();
        config.setDefault("huge_multiplier", false);
        config.setDefault("shared_multiplier", false);
        config.setDefault("multipliers.block_drops", false);
        config.setDefault("multipliers.mob_drops", false);
        config.setDefault("multipliers.mob_xp", false);
        config.saveSafe();

        CommandService commandService = Bamboo.INS.serviceManager.getService(CommandService.class);
        if (commandService != null) {
            commandService.registerCommand(multiplierDevCmd = new MultiplierDevCmd());
        }

        DisplayController.push();
    }

    @Override
    public void onDisable() {
        DisplayController.pop();

        if (listeners != null) {
            listeners.unregister();
            listeners = null;
        }

        CommandService commandService = Bamboo.INS.serviceManager.getService(CommandService.class);
        if (commandService != null) {
            commandService.unregisterCommand(multiplierDevCmd);
        }
    }

    public boolean isEnabled(String multiplier) {
        TimerService timerService = Bamboo.INS.serviceManager.getService(TimerService.class);
        if (timerService == null || !timerService.resumed) {
            return false;
        }
        return config.get().isBoolean("multipliers." + multiplier);
    }

    public int getMultiplierAndIncrease(String path) {
        if (config.get().getBoolean("shared_multiplier")) {
            path = null;
        }
        final int multiplier = Utils.gd(multipliers.get(path), 0);
        if (config.get().getBoolean("huge_multiplier")) {
            multipliers.put(path, Math.max(multiplier, 1) * Math.max(multiplier, 2));
        } else {
            multipliers.put(path, multiplier + 1);
        }
        return multiplier;
    }
}
