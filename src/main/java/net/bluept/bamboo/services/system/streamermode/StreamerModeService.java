package net.bluept.bamboo.services.system.streamermode;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.services.system.command.CommandService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StreamerModeService extends Service {
    public List<UUID> states;
    private StreamerModeCmd streamerModeCmd;

    @Override
    public void onEnable() {
        states = new ArrayList<>();

        CommandService commandService = Bamboo.INS.serviceManager.getService(CommandService.class);
        if (commandService != null) {
            commandService.registerCommand(streamerModeCmd = new StreamerModeCmd());
        }
    }

    @Override
    public void onDisable() {
        states = null;

        CommandService commandService = Bamboo.INS.serviceManager.getService(CommandService.class);
        if (commandService != null && streamerModeCmd != null) {
            commandService.unregisterCommand(streamerModeCmd);
        }
    }
}
