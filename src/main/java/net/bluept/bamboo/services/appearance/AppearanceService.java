package net.bluept.bamboo.services.appearance;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.util.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.util.CachedServerIcon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class AppearanceService extends Service {
    public final String pack_url = "https://download.mc-packs.net/pack/fa8e01d9f329afb05d6c27e35da13d92638c9991.zip";
    public final String pack_sha1 = "fa8e01d9f329afb05d6c27e35da13d92638c9991";

    public Component motd;
    public Integer oldPlayerCount = null;
    public CachedServerIcon serverIcon = null;
    private Listeners listeners;

    @Override
    public void onEnable() {
        int serverId = Bamboo.INS.getConfig().getInt("serverid", -1);
        if (serverId <= 0) {
            Bamboo.INS.getConfig().set("serverid", -1);
            Bamboo.INS.saveConfig();
            Bamboo.INS.logDev("!!! Serverid required. Please set in the config !!!");
            return;
        }

        motd = MiniMessage.miniMessage().deserialize("<color:#E81C99>Panda Bamboo Server $ Panda@EU#" + serverId + "</color>");

        oldPlayerCount = Bukkit.getMaxPlayers();
        final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (currentYear > 0 && currentYear < Integer.MAX_VALUE)
            Bukkit.setMaxPlayers(currentYear);

        {
            try {
                final BufferedImage image = loadServerIcon();
                if (image != null) {
                    serverIcon = Bukkit.loadServerIcon(image);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (listeners != null) {
            HandlerList.unregisterAll(listeners);
            listeners = null;
        }
        Bamboo.INS.getServer().getPluginManager().registerEvents((listeners = new Listeners()), Bamboo.INS);
    }

    @Override
    public void onDisable() {
        if (listeners != null) {
            HandlerList.unregisterAll(listeners);
            listeners = null;
        }

        if (oldPlayerCount != null) {
            Bukkit.setMaxPlayers(oldPlayerCount);
            oldPlayerCount = null;
        }
    }

    private BufferedImage loadServerIcon() {
        try {
            final InputStream inputStream = Bamboo.INS.getResource("assets/bamboo/server-icon.png");
            if (inputStream != null) {
                final BufferedImage image = ImageIO.read(inputStream);
                inputStream.close();
                return image;
            } else {
                Bamboo.INS.logDev("Could not get server-icon input-stream");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
