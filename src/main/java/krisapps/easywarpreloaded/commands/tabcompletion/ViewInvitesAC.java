package krisapps.easywarpreloaded.commands.tabcompletion;

import krisapps.easywarpreloaded.EasyWarpReloaded;
import krisapps.easywarpreloaded.types.WarpEntry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ViewInvitesAC implements TabCompleter {

    EasyWarpReloaded main;

    public ViewInvitesAC(EasyWarpReloaded main) {
        this.main = main;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (sender.isOp()) {
                completions.addAll(main.dataUtility.getAllWarps().stream().map(WarpEntry::getWarpID).collect(Collectors.toList()));
            } else {
                completions.addAll(main.dataUtility.getPrivateWarpsForPlayer(((Player) sender).getUniqueId()));
            }
        }

        return completions;
    }
}
