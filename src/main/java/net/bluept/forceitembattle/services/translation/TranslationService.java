package net.bluept.forceitembattle.services.translation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.service.Service;
import net.bluept.forceitembattle.services.item.ItemService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TranslationService extends Service {
    public static final Type MAP_TYPE = new TypeToken<Map<String, String>>() {
    }.getType();
    public File translationsFolder;
    private Gson gson;
    private Map<String, Map<String, String>> loadedTranslations;

    public static String translate(Locale locale, String key, String defaultValue) {
        TranslationService translationService = ForceItemBattle.INSTANCE.serviceManager.getService(TranslationService.class);
        if (translationService == null) {
            return "(unable to connect to translation service)";
        }

        String lang = (locale.getLanguage() + "_" + locale.getCountry()).toLowerCase();

        Map<String, String> translations = translationService.loadedTranslations.get(lang);
        if (translations == null) {
            if ("en_US".equals(lang)) {
                return key;
            }
            return translate(locale, "en_US", defaultValue);
        }
        String translation = translations.get(key);
        ForceItemBattle.INSTANCE.getLogger().warning(translation);
        if (translation == null) {
            return defaultValue;
        }
        return translation;
    }

    public static String translatePlayerItem(ItemService itemService, Player player) {
        Material material = itemService.getPlayerMaterial(player.getUniqueId());
        return TranslationService.translate(player.locale(), material.getItemTranslationKey(), material.name());
    }

    public static String removeFileExtension(String s) {
        int index = s.indexOf('.');
        if (index != -1) {
            return s.substring(0, index);
        } else {
            return s;
        }
    }

    @Override
    public void onEnable() {
        gson = new Gson();
        loadedTranslations = new HashMap<>();

        translationsFolder = new File(ForceItemBattle.INSTANCE.configRoot, "translations");
        if (!translationsFolder.exists()) {
            translationsFolder.mkdir();
        }

        BukkitTask task = Bukkit.getScheduler().runTaskLater(ForceItemBattle.INSTANCE, this::loadTranslations, 10);
        ForceItemBattle.INSTANCE.getLogger().info("Created translation loader task [" + task.getTaskId() + "]");

        File readmeFile = new File(translationsFolder, "README.txt");
        if (!readmeFile.exists()) {
            try (FileWriter writer = new FileWriter(readmeFile.getAbsolutePath())) {
                writer.write("Please add your Minecraft translation files here");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onDisable() {
        loadedTranslations = null;
    }

    public void loadTranslations() {
        File[] files = translationsFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".json")) {
                    loadTranslationFile(file);
                }
            }
        }
    }

    public void loadTranslationFile(File file) {
        try (Reader reader = new FileReader(file.getAbsolutePath())) {
            String lang = removeFileExtension(file.getName()).toLowerCase();
            Map<String, String> map = gson.fromJson(reader, MAP_TYPE);
            if (map != null) {
                loadedTranslations.put(lang, map);
                ForceItemBattle.INSTANCE.getLogger().info("Loaded translations for lang '" + lang + "'");
            }
        } catch (IOException ex) {
            ForceItemBattle.INSTANCE.getLogger().warning("Failed to load translation file " + file.getName());
        }
    }
}
