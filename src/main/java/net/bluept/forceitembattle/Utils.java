package net.bluept.forceitembattle;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.md_5.bungee.api.ChatColor.COLOR_CHAR;

@SuppressWarnings({"deprecation", "unused"})
public class Utils {
    public static String translateColor(String text, char prefix) {
        if (text == null) return null;
        return ChatColor.translateAlternateColorCodes(prefix, text);
    }

    public static String translateHexColorCodes(String startTag, String endTag, String message) {
        if (message == null) return null;
        final Pattern hexPattern = Pattern.compile(startTag + "([A-Fa-f0-9]{6})" + endTag);
        Matcher matcher = hexPattern.matcher(message);
        StringBuilder buffer = new StringBuilder(message.length() + 4 * 8);
        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x" + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1) + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3) + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5));
        }
        return matcher.appendTail(buffer).toString();
    }

    public static String colorfy(String text, char prefix) {
        return translateColor(translateHexColorCodes(prefix + "<#", ">", text.replaceAll("\n", "\n&r")) + "&r", prefix);
    }

    public static String colorfy(String text) {
        return colorfy(text, '&');
    }

    public static void send(CommandSender sender, String message) {
        sender.sendMessage(colorfy(message));
    }

    public static void send(Player player, String message) {
        player.sendMessage(colorfy(message));
    }

    public static void send(ConsoleCommandSender sender, String message) {
        sender.sendMessage(colorfy(message));
    }

    public static boolean rDelete(File folder) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    rDelete(file);
                }
            }
        }
        return folder.delete();
    }

    public static class ClassScanner {
        public static List<Class<?>> getClassesExtending(String basePackage, Class<?> superClass) throws IOException, ClassNotFoundException {
            List<Class<?>> classes = new ArrayList<>();
            String classPath = System.getProperty("java.class.path");
            String[] classPathEntries = classPath.split(File.pathSeparator);

            for (String classPathEntry : classPathEntries) {
                if (new File(classPathEntry).isDirectory()) {
                    String packagePath = basePackage.replace('.', File.separatorChar);
                    File baseDir = new File(classPathEntry + File.separator + packagePath);
                    if (baseDir.exists()) {
                        findAndAddClassesInDirectory(basePackage, superClass, baseDir, classes);
                    }
                }
            }

            return classes;
        }

        private static void findAndAddClassesInDirectory(String packageName, Class<?> superClass, File directory, List<Class<?>> classes) throws ClassNotFoundException {
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                if (file.isDirectory()) {
                    findAndAddClassesInDirectory(packageName + "." + file.getName(), superClass, file, classes);
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                    Class<?> clazz = Class.forName(className);
                    if (superClass.isAssignableFrom(clazz) && !superClass.equals(clazz)) {
                        classes.add(clazz);
                    }
                }
            }
        }
    }
}
