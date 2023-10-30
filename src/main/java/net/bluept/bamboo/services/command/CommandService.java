package net.bluept.bamboo.services.command;

import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.services.command.commands.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandService extends Service {
    public static final String FALLBACK_PREFIX = "bluept";
    public List<Command> registeredCommands;

    @Override
    public void onEnable() {
        onDisable();

        CmdHelper.init();

        registeredCommands = new ArrayList<>(List.of(
                new CloneInvCmd(),
                new IdleCmd(),
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

    public void registerCommand(Command command) {
        CmdHelper.knownCommands.put(command.getName(), command);
        CmdHelper.commandMap.register(FALLBACK_PREFIX, command);
    }

    public void unregisterCommand(Command command) {
        CmdHelper.knownCommands.remove(command.getLabel());
        CmdHelper.knownCommands.remove(command.getName());
        command.getAliases().forEach(CmdHelper.knownCommands::remove);
        command.unregister(CmdHelper.commandMap);
        command.setAliases(Collections.emptyList());
    }
}
