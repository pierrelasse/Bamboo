package net.bluept.bamboo.services.system.command;

import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;
import net.bluept.bamboo.services.system.command.commands.*;

import java.util.ArrayList;
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
                new DevCmd(),
                new IdleCmd(),
                new InvseeCmd(),
                new ResetCmd(),
                new ServiceCmd(),
                new StartCmd()
        ));

        registeredCommands.forEach(i -> registerCommand(i, false));
    }

    @Override
    public void onDisable() {
        if (registeredCommands != null) {
            registeredCommands.forEach(this::unregisterCommand);
        }
    }

    public void registerCommand(org.bukkit.command.Command command) {
        registerCommand(command, true);
    }

    public void registerCommand(org.bukkit.command.Command command, boolean register) {
        CmdHelper.knownCommands.put(command.getName(), command);
        CmdHelper.commandMap.register(FALLBACK_PREFIX, command);
        if (register) {
            registeredCommands.add(command);
        }
    }

    public void unregisterCommand(org.bukkit.command.Command command) {
        CmdHelper.knownCommands.remove(command.getLabel());
        CmdHelper.knownCommands.remove(command.getName());
        command.unregister(CmdHelper.commandMap);
    }
}
