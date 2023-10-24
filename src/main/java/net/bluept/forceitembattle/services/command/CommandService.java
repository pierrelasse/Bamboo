package net.bluept.forceitembattle.services.command;

import net.bluept.forceitembattle.service.Service;
import net.bluept.forceitembattle.services.command.commands.StartCmd;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class CommandService extends Service {
    public List<Command> registeredCommands;

    @Override
    public void start() {
        stop();
        registeredCommands = new ArrayList<>();

        registeredCommands.add(new StartCmd());

        registeredCommands.forEach(this::registerCommand);
    }

    @Override
    public void stop() {
        if (registeredCommands != null) {
            registeredCommands.forEach(this::unregisterCommand);
        }
    }

    public void registerCommand(Command command) {
        Bukkit.getCommandMap().register("bluept", command);
    }

    public void unregisterCommand(Command command) {
        command.unregister(Bukkit.getCommandMap());
    }
}
