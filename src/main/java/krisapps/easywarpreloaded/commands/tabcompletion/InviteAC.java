package krisapps.easywarpreloaded.commands.tabcompletion;

import krisapps.easywarpreloaded.EasyWarpReloaded;
import krisapps.easywarpreloaded.types.WarpEntry;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InviteAC implements TabCompleter {

    EasyWarpReloaded main;

    public InviteAC(EasyWarpReloaded main) {
        this.main = main;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        } else if (args.length == 2) {
            if (sender.isOp()) {
                completions.addAll(main.dataUtility.getAllWarps().stream().map(WarpEntry::getWarpID).collect(Collectors.toList()));
            } else {
                completions.addAll(main.dataUtility.getPublicWarps());
                completions.addAll(main.dataUtility.getPrivateWarpsForPlayer(((Player) sender).getUniqueId()));
            }
        } else if (args.length == 3) {
            completions.add("<useCount>");
        }
        return completions;
    }
}
