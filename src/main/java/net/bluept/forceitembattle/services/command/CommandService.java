package net.bluept.forceitembattle.services.command;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.Utils;
import net.bluept.forceitembattle.service.Service;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandService extends Service {
    public List<Command> registeredCommands;

    @Override
    public void start() {
        stop();
        registeredCommands = new ArrayList<>();

        try {
            List<Class<?>> classes = Utils.ClassScanner.getClassesExtending(getClass().getPackageName() + ".commands", Command.class);
            for (Class<?> clazz : classes) {
                registeredCommands.add((Command) clazz.getDeclaredConstructor().newInstance());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        registeredCommands.forEach(this::registerCommand);
        ForceItemBattle.INSTANCE.getLogger().info("Registered " + registeredCommands.size() + " commands");
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
        unregisterCommand(command.getName().toLowerCase());
    }

    public void unregisterCommand(String command) {
        getKnownCommands().remove(command);
    }

    public Map<String, org.bukkit.command.Command> getKnownCommands() {
        try {
            Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
            return (Map<String, org.bukkit.command.Command>) knownCommandsField.get(Bukkit.getCommandMap());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
