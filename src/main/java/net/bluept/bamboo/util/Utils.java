package net.bluept.bamboo.util;

import net.bluept.bamboo.Bamboo;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"deprecation", "unused"})
public class Utils {
    public static final char COLOR_CHAR = '\u00A7';
    public static final Random RANDOM = new Random();
    public static final String SPACE = " ";

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

    public static void title(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    public static <T> T get(List<T> list, int index) {
        return get(list, index, null);
    }

    public static <T> T get(List<T> list, int index, T defaultValue) {
        if (list == null || index < 0 || index >= list.size()) {
            return defaultValue;
        }
        return gd(list.get(index), defaultValue);
    }

    public static <T> T gd(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static void rDelete(File file) {
        if (file.isFile()) {
            file.deleteOnExit();
            file.delete();
            Bamboo.INS.getLogger().info("Delete: " + file.getAbsolutePath());
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File file1 : files) {
                    rDelete(file1);
                }
            }
        }
    }

    public static String stringifyLocale(Locale locale) {
        return (locale.getLanguage() + "_" + locale.getCountry()).toLowerCase(Locale.ENGLISH);
    }

    public static int randint(final int min, final int max) {
        return RANDOM.nextInt(max - min) + min;
    }

    public static String convertSecondsToDuration(long seconds) {
        long days = seconds / (24 * 3600);
        long hours = (seconds % (24 * 3600)) / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;

        StringBuilder sb = new StringBuilder();

        if (days > 0) {
            sb.append(days).append("d ");
        }
        if (hours > 0 || days > 0) {
            sb.append(hours).append("h ");
        }
        if (minutes > 0 || hours > 0 || days > 0) {
            sb.append(minutes).append("m ");
        }

        return sb.append(remainingSeconds).append("s").toString();
    }
}
