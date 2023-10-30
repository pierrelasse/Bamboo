package net.bluept.bamboo.services.command;

import net.bluept.bamboo.Bamboo;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;

import java.lang.reflect.Field;
import java.util.Map;

public class CmdHelper {
    public static SimpleCommandMap commandMap;
    public static Map<String, Command> knownCommands;

    @SuppressWarnings("unchecked")
    public static void init() {
        try {
            if ("org.bukkit.plugin.SimplePluginManager".equals(Bukkit.getPluginManager().getClass().getName())) {
                Field commandMapField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
                commandMapField.setAccessible(true);
                commandMap = (SimpleCommandMap)commandMapField.get(Bukkit.getPluginManager());

                Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
                knownCommandsField.setAccessible(true);
                knownCommands = (Map<String, Command>)knownCommandsField.get(commandMap);
            }
        } catch (SecurityException ex) {
            Bamboo.INS.getLogger().severe("Commands: Please disable the security manager");
            commandMap = null;
        } catch (Exception ex) {
            commandMap = null;
        }
    }
}
