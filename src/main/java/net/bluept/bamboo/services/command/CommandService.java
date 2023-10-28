package net.bluept.bamboo.services.command;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.services.command.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class CommandService extends Service {
    public List<Command> registeredCommands;

    @Override
    public void onEnable() {
        onDisable();

        registeredCommands = List.of(
                new DevCmd(),
                new IdleCmd(),
                new JokerCmd(),
                new ResetCmd(),
                new ServiceCmd(),
                new StartCmd()
        );

        registeredCommands.forEach(this::registerCommand);
        Bamboo.INS.getLogger().info("Registered " + registeredCommands.size() + " commands");
    }

    @Override
    public void onDisable() {
        if (registeredCommands != null) {
            registeredCommands.forEach(this::unregisterCommand);
        }
    }

    public void registerCommand(Command command) {
        Bukkit.getCommandMap().register("bluept", command);
    }

    public void unregisterCommand(Command command) {
        unregisterCommand(command.getName().toLowerCase());
    }

    public void unregisterCommand(String command) {
        getKnownCommands().remove(command);
    }

    public Map<String, org.bukkit.command.Command> getKnownCommands() {
        try {
            Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
            return (Map<String, org.bukkit.command.Command>)knownCommandsField.get(Bukkit.getCommandMap());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
