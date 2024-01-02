package net.bluept.bamboo.services.system.streamermode;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.system.command.Command;
import net.bluept.bamboo.util.Utils;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class StreamerModeCmd extends Command {
    public StreamerModeCmd() {
        super("streamermode");
    }

    @Override
    public void executePlayer(Player player, List<String> args) {
        StreamerModeService streamerModeService = Bamboo.INS.serviceManager.getService(StreamerModeService.class, player);
        if (streamerModeService == null) {
            return;
        }

        final UUID uuid = player.getUniqueId();
        final List<UUID> states = streamerModeService.states;

        if (states.contains(uuid)) {
            states.remove(uuid);
            Utils.send(player, "&cStreamer mode turned off");
        } else {
            states.add(uuid);
            Utils.send(player, "&aStreamer mode turned on");
        }
    }
}
