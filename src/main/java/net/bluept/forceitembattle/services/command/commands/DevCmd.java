package net.bluept.forceitembattle.services.command.commands;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.services.command.Command;
import net.bluept.forceitembattle.services.item.ItemService;
import net.bluept.forceitembattle.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class DevCmd extends Command {
    public List<String> revealColors;
    public String defaultRevealColor;

    public DevCmd() {
        super("dev");
        revealColors = Arrays.asList("6", "7", "&<#BF8970>");
        defaultRevealColor = "8";
    }

    @Override
    public void executePlayer(Player player, List<String> args) {
        if ("regenchunk".equals(Utils.get(args, 0))) {
            Bukkit.getScheduler().runTask(ForceItemBattle.INS, () -> {
                Chunk chunk = player.getChunk();
                player.getWorld().regenerateChunk(chunk.getX(), chunk.getZ());
                Utils.send(player, "&aDone!");
            });

        } else if ("skipitem".equals(Utils.get(args, 0)) && args.size() >= 2) {
            Player target = Bukkit.getPlayer(args.get(1));
            if (target != null) {
                ItemService itemService = ForceItemBattle.INS.serviceManager.getService(ItemService.class);
                if (itemService != null) {
                    itemService.nextPlayerMaterial(target.getUniqueId());
                    Utils.send(player, "&aSkipped " + target.getName() + "'s item");
                }
            }
        } else if ("reveal".equals(Utils.get(args, 0))) {
            ItemService itemService = ForceItemBattle.INS.serviceManager.getService(ItemService.class);
            if (itemService == null) {
                Utils.send(player, "&cFailed to connect to the item service");
                return;
            }

            if (itemService.playerItems.isEmpty()) {
                Utils.send(player, "&cNothing to display");
                return;
            }


            List<Map.Entry<UUID, Integer>> entryList = new ArrayList<>(itemService.playerItems.entrySet());
            entryList.sort((a, b) -> b.getValue().compareTo(a.getValue()));

            StringBuilder sb = new StringBuilder("&d&lErgebnisse:");
            int index = 0;
            for (Map.Entry<UUID, Integer> entry : entryList) {
                Player targetPlayer = Bukkit.getPlayer(entry.getKey());
                if (targetPlayer != null) {
                    sb.append("\n  &").append(Utils.get(revealColors, index, defaultRevealColor))
                            .append("#").append(index + 1).append(" &d&l").append(targetPlayer.getName())
                            .append(" &8- &d").append(entry.getValue());
                    index++;
                }
            }
            String message = Utils.colorfy(sb.toString());
            Bukkit.getOnlinePlayers().forEach(i -> i.sendMessage(message));

        } else if ("reset".equals(Utils.get(args, 0))) {
            ItemService itemService = ForceItemBattle.INS.serviceManager.getService(ItemService.class);
            if (itemService == null) {
                Utils.send(player, "&cFailed to connect to the item service");
                return;
            }

            itemService.playerItems.clear();
            itemService.playerMaterials.clear();
            itemService.playerJoker.clear();

        } else if ("resetjokers".equals(Utils.get(args, 0))) {
            ItemService itemService = ForceItemBattle.INS.serviceManager.getService(ItemService.class);
            if (itemService == null) {
                Utils.send(player, "&cFailed to connect to the item service");
                return;
            }

            itemService.playerJoker.remove(player.getUniqueId());
            Utils.send(player, "&aUsed joker amount reset");
        }
    }
}
