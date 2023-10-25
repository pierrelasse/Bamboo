package net.bluept.forceitembattle;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.md_5.bungee.api.ChatColor.COLOR_CHAR;

@SuppressWarnings("deprecation")
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
}
