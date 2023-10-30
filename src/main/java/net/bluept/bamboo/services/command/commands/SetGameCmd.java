package net.bluept.bamboo.services.command.commands;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.command.Command;
import net.bluept.bamboo.util.Utils;
import org.bukkit.command.CommandSender;

import java.util.List;

public class SetGameCmd extends Command {
    public SetGameCmd() {
        super("setgame");
        setPermission("penis");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        Integer gameId = null;
        try {
            gameId = Integer.parseInt(Utils.get(args, 0));
        } catch (NumberFormatException ignored) {
        }

        if (gameId == null || gameId > 3 || gameId < 0) {
            Utils.send(sender, "&cInvalid number");
            return;
        }

        Bamboo.INS.getConfig().set("gameId", gameId);
        Bamboo.INS.saveConfig();
        Utils.send(sender, "&aGameId set to " + gameId + ". You will need to /reload to apply changes");
    }
}
