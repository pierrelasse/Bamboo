package net.bluept.forceitembattle.services.command;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.service.Service;
import net.bluept.forceitembattle.services.command.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;

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
                new ResetCmd(),
                new ServiceCmd(),
                new StartCmd()
        );

        registeredCommands.forEach(this::registerCommand);
        ForceItemBattle.INSTANCE.getLogger().info("Registered " + registeredCommands.size() + " commands");
    }

    @Override
    public void onDisable() {
        if (registeredCommands != null) {
            registeredCommands.forEach(this::unregisterCommand);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.updateCommands();
            }
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
