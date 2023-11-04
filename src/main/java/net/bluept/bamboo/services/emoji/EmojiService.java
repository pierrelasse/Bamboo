package net.bluept.bamboo.services.emoji;

import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@ServiceInfo(description = "Support for emojis")
public class EmojiService extends Service {
    private Map<String, String> emojis;

    @Override
    public void onEnable() {
        emojis = new HashMap<>();
        reloadEmojis();
    }

    @Override
    public void onDisable() {
        emojis = null;
    }

    private void reloadEmojis() {
        emojis.clear();

        for (Field field : Emojis.class.getFields()) {
            try {
                Object v = field.get(null);
                if (v instanceof String s) {
                    emojis.put(field.getName(), s);
                }
            } catch (IllegalAccessException ignored) {
            }
        }
    }

    public String translate(String s) {
        for (Map.Entry<String, String> entry : emojis.entrySet()) {
            s = s.replaceAll("\\b" + Pattern.quote(entry.getKey()) + "\\b", entry.getValue());
        }
        return s;
    }
}
