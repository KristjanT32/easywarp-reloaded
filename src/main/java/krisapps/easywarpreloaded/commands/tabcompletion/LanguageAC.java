package krisapps.easywarpreloaded.commands.tabcompletion;

import krisapps.easywarpreloaded.EasyWarpReloaded;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class LanguageAC implements TabCompleter {

    EasyWarpReloaded main;

    public LanguageAC(EasyWarpReloaded main) {
        this.main = main;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(main.localizationUtility.getLanguages());
        }

        return completions;
    }
}
