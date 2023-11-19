package net.bluept.bamboo.services.translation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;
import net.bluept.bamboo.services.forceitembattle.ItemService;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ServiceInfo(description = "Provides the translations of minecraft")
public class TranslationService extends Service {
    public static final Type MAP_TYPE = new TypeToken<Map<String, String>>() {}.getType();
    public File translationsFolder;
    public Map<String, Map<String, String>> loadedTranslations;
    private Gson gson;

    public static String translate(Locale locale, String key, String defaultValue) {
        return translate(Utils.stringifyLocale(locale), key, defaultValue);
    }

    public static String translate(String lang, String key, String defaultValue) {
        TranslationService translationService = Bamboo.INS.serviceManager.getService(TranslationService.class);
        if (translationService == null) {
            return "(unable to connect to translation service)";
        }

        Map<String, String> translations = translationService.loadedTranslations.get(lang);
        if (translations == null) {
            if ("en_us".equals(lang)) {
                return key;
            }
            return translate("en_us", key, defaultValue);
        }
        String translation = translations.get(key);
        if (translation == null) {
            return defaultValue;
        }
        return translation;
    }

    public static String translatePlayerItem(ItemService itemService, Player player) {
        Material material = itemService.getPlayerMaterial(player.getUniqueId());
        return TranslationService.translate(player.locale(), material.getItemTranslationKey(), material.name());
    }

    @Override
    public void onEnable() {
        gson = new Gson();
        loadedTranslations = new HashMap<>();

        translationsFolder = new File(Bamboo.INS.configRoot, "translations");
        if (!translationsFolder.exists()) {
            translationsFolder.mkdir();
        }

        BukkitTask task = Bukkit.getScheduler().runTaskAsynchronously(Bamboo.INS, this::loadTranslations);
        Bamboo.INS.getLogger().info("Created translation loader task [" + task.getTaskId() + "]");

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
            Map<String, String> map = gson.fromJson(reader, MAP_TYPE);
            if (map != null) {
                String code = map.get("language.code");
                if (code == null) {
                    Bamboo.INS.getLogger().warning("No language code found for translation file " + file.getName());
                    return;
                }
                code = code.toLowerCase();

                if (loadedTranslations.containsKey(code)) {
                    Bamboo.INS.getLogger().warning("Translations for code '" + code + "' already loaded. File: " + file.getName());
                    return;
                }
                loadedTranslations.put(code, map);
                Bamboo.INS.getLogger().info("Loaded translations for lang '" + code + "'");
            }
        } catch (IOException ex) {
            Bamboo.INS.getLogger().warning("Failed to load translation file " + file.getName());
        }
    }
}
