package net.bluept.bamboo.services.command;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.services.command.commands.IdleCmd;
import net.bluept.bamboo.services.command.commands.ResetCmd;
import net.bluept.bamboo.services.command.commands.ServiceCmd;
import net.bluept.bamboo.services.command.commands.StartCmd;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandService extends Service {
    public static final String FALLBACK_PREFIX = "bluept";
    public List<Command> registeredCommands;

    @Override
    public void onEnable() {
        onDisable();

        registeredCommands = new ArrayList<>(List.of(
                new IdleCmd(),
                new ResetCmd(),
                new ServiceCmd(),
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
        Bukkit.getCommandMap().register(FALLBACK_PREFIX, command);
        getKnownCommands().put(FALLBACK_PREFIX + ":" + command.getName(), command);
        getKnownCommands().put(command.getName(), command);
        command.register(Bukkit.getCommandMap());
    }

    public void unregisterCommand(Command command) {
//        getKnownCommands().remove(FALLBACK_PREFIX + ":" + command.getName());
//        command.unregister(Bukkit.getCommandMap());
//        getKnownCommands().remove(command.getName());

        String key = command.getName();
        Map<String, org.bukkit.command.Command> knownCommands = getKnownCommands();
        if (knownCommands.containsKey(key)) {
            knownCommands.remove(key).unregister(Bukkit.getCommandMap());
        }
    }

    public Map<String, org.bukkit.command.Command> getKnownCommands() {
        return Bukkit.getCommandMap().getKnownCommands();
    }


}
