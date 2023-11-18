package net.bluept.bamboo.services.command;

import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;
import net.bluept.bamboo.services.command.commands.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ServiceInfo(description = "It is not recommended to stop this one...")
public class CommandService extends Service {
    public static final String FALLBACK_PREFIX = "bluept";
    public List<org.bukkit.command.Command> registeredCommands;

    @Override
    public void onEnable() {
        onDisable();

        CmdHelper.init();

        registeredCommands = new ArrayList<>(List.of(
                new CloneInvCmd(),
                new ConfigCmd(),
                new IdleCmd(),
                new InvseeCmd(),
                new ResetCmd(),
                new ServiceCmd(),
                new SetGameCmd(),
                new StartCmd()
        ));

        registeredCommands.forEach(this::registerCommand);
    }

    @Override
    public void onDisable() {
        if (registeredCommands != null) {
            registeredCommands.forEach(this::unregisterCommand);
        }
    }

    public void registerCommand(org.bukkit.command.Command command) {
        CmdHelper.knownCommands.put(command.getName(), command);
        CmdHelper.commandMap.register(FALLBACK_PREFIX, command);
        registeredCommands.add(command);
    }

    public void unregisterCommand(org.bukkit.command.Command command) {
        CmdHelper.knownCommands.remove(command.getLabel());
        CmdHelper.knownCommands.remove(command.getName());
        command.unregister(CmdHelper.commandMap);
    }
}
