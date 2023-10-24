package net.bluept.forceitembattle;

import org.bukkit.plugin.java.JavaPlugin;

public class ForceItemBattle extends JavaPlugin {
    public static ForceItemBattle INSTANCE;

    @Override
    public void onLoad() {
        INSTANCE = this;
        INSTANCE.getLogger().info("System loaded!");
    }

    @Override
    public void onEnable() {
        INSTANCE.getLogger().info("Starting system");

        INSTANCE.getLogger().info("System started!");
    }

    @Override
    public void onDisable() {
        INSTANCE.getLogger().info("Shutting system down");
        INSTANCE.getLogger().info("System stopped successfully!");
    }
}
