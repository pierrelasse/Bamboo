package net.bluept.bamboo.services.challenges.multiplier;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.services.system.command.CommandService;
import net.bluept.bamboo.services.dep.display.DisplayController;
import net.bluept.bamboo.services.challenges.multiplier.commands.MultiplierDevCmd;
import net.bluept.bamboo.services.dep.timer.TimerService;
import net.bluept.bamboo.util.Config;
import net.bluept.bamboo.util.Utils;
import org.bukkit.event.HandlerList;

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
            HandlerList.unregisterAll(listeners);
            listeners = null;
        }
        Bamboo.INS.getServer().getPluginManager().registerEvents(listeners = new Listeners(), Bamboo.INS);

        config = new Config(new File(Bamboo.INS.configRoot, "multiplier.yml"));
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
            HandlerList.unregisterAll(listeners);
            listeners = null;
        }

        CommandService commandService = Bamboo.INS.serviceManager.getService(CommandService.class);
        if (commandService != null) {
            commandService.unregisterCommand(multiplierDevCmd);
        }
    }

    public boolean isEnabled(String multiplier) {
        return TimerService.isResumed() && config.get().isBoolean("multipliers." + multiplier);
    }

    public int getMultiplierAndIncrease(String path) {
        if (config.get().getBoolean("shared_multiplier")) {
            path = null;
        }
        final int multiplier = Utils.gd(multipliers.get(path), 0);
        if (config.get().getBoolean("huge_multiplier")) {
            multipliers.put(path, Math.max(multiplier, 1) * 2);
        } else {
            multipliers.put(path, multiplier + 1);
        }
        return multiplier;
    }
}
