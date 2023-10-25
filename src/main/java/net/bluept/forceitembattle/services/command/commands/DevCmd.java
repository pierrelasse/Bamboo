package net.bluept.forceitembattle.services.command.commands;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.Utils;
import net.bluept.forceitembattle.services.command.Command;
import net.bluept.forceitembattle.services.item.ItemService;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
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
        } else if ("item".equals(args.get(0))) {
            ForceItemBattle.INSTANCE.serviceManager.getAndRun("item", ItemService.class, serv -> {
                Material material = serv.getPlayerMaterial(player.getUniqueId());
            });
        }
    }
}
