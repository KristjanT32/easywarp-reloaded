package krisapps.easywarpreloaded.commands;

import krisapps.easywarpreloaded.EasyWarpReloaded;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetLanguage implements CommandExecutor {

    EasyWarpReloaded main;

    public SetLanguage(EasyWarpReloaded main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        // Syntax: /setlanguage <language>
        if (args.length >= 1) {
            if (main.localizationUtility.languageFileExists(args[0])) {
                main.localizationUtility.changeLanguage(args[0]);
            }
        } else {
            return false;
        }

        return true;
    }
}
