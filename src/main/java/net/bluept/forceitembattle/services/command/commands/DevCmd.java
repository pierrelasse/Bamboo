package net.bluept.forceitembattle.services.command.commands;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.services.command.Command;
import net.bluept.forceitembattle.services.item.ItemService;
import net.bluept.forceitembattle.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.List;

public class DevCmd extends Command {
    public DevCmd() {
        super("dev");
    }

    @Override
    public void executePlayer(Player player, List<String> args) {
        if ("regenchunk".equals(args.get(0))) {
            Bukkit.getScheduler().runTask(ForceItemBattle.INSTANCE, () -> {
                Chunk chunk = player.getChunk();
                player.getWorld().regenerateChunk(chunk.getX(), chunk.getZ());
                Utils.send(player, "&aDone!");
            });

        } else if ("skipitem".equals(args.get(0))) {
            if (args.size() >= 2) {
                Player target = Bukkit.getPlayer(args.get(1));
                if (target != null) {
                    ItemService itemService = ForceItemBattle.INSTANCE.serviceManager.getService(ItemService.class);
                    if (itemService != null) {
                        itemService.nextPlayerMaterial(target.getUniqueId());
                        Utils.send(player, "&aSkipped " + target.getName() + "'s item");
                    }
                }
            }
        }
    }
}
