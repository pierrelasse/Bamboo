package net.bluept.bamboo.util;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("unused")
public class Config {
    public final File file;
    private YamlConfiguration config;

    public Config(File file) {
        this.file = file;
        load();
    }

    public void load() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() throws IOException {
        config.save(file);
    }

    public void saveSafe() {
        try {
            save();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public YamlConfiguration get() {
        return config;
    }

    public void setDefault(String path, Object value) {
        if (!config.isSet(path)) {
            config.set(path, value);
        }
    }
}
