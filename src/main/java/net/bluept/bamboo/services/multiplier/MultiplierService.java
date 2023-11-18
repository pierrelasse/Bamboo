package net.bluept.bamboo.services.multiplier;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.services.command.CommandService;
import net.bluept.bamboo.services.multiplier.commands.MultiplierDevCmd;
import net.bluept.bamboo.services.timer.TimerService;
import net.bluept.bamboo.util.Config;

import java.io.File;

public class MultiplierService extends Service {
    public Config config;
    public int multiplier = 0;
    private Listeners listeners;
    private MultiplierDevCmd multiplierDevCmd;

    @Override
    public void onEnable() {
        multiplier = 1;

        if (listeners != null) {
            listeners.unregister();
        }
        listeners = new Listeners();

        config = new Config(new File(Bamboo.INS.configRoot, "multiplier.yml"));
        config.load();
        config.setDefault("huge_multiplier", false);
        config.setDefault("multipliers.block_drops", false);
        config.setDefault("multipliers.mob_drops", false);
        config.setDefault("multipliers.mob_xp", false);
        config.saveSafe();

        CommandService commandService = Bamboo.INS.serviceManager.getService(CommandService.class);
        if (commandService != null) {
            commandService.registerCommand(multiplierDevCmd = new MultiplierDevCmd());
        }
    }

    @Override
    public void onDisable() {
        listeners.unregister();
        listeners = null;

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
}
