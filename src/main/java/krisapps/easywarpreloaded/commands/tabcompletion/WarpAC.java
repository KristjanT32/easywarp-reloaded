package krisapps.easywarpreloaded.commands.tabcompletion;

import krisapps.easywarpreloaded.EasyWarpReloaded;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WarpAC implements TabCompleter {

    EasyWarpReloaded main;

    public WarpAC(EasyWarpReloaded main) {
        this.main = main;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (!sender.isOp()) {
                completions.addAll(main.dataUtility.getPublicWarps());
                completions.addAll(main.dataUtility.getPrivateWarpsForPlayer(((Player) sender).getUniqueId()));
            } else {
                completions.addAll(main.dataUtility.getPublicWarps());
                completions.addAll(main.dataUtility.getPrivateWarps());
            }
        }


        return completions;
    }
}
