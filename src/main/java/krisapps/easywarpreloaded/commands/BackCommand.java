package krisapps.easywarpreloaded.commands;

import krisapps.easywarpreloaded.EasyWarpReloaded;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BackCommand implements CommandExecutor {

    EasyWarpReloaded main;

    public BackCommand(EasyWarpReloaded main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        // Syntax: /back
        if (sender instanceof Player) {
            Location prev = main.dataUtility.getPreviousLocation(((Player) sender).getUniqueId());
            if (prev != null) {
                if (!prev.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                    if (Boolean.parseBoolean(main.dataUtility.getConfigSetting("settings.allow-dimensional-warping"))) {
                        ((Player) sender).teleport(prev);
                        main.messageUtility.sendMessage(sender, main.localizationUtility.getLocalizedPhrase("commands.back.returned"));
                    } else {
                        main.messageUtility.sendMessage(sender, main.localizationUtility.getLocalizedPhrase("commands.back.dim-disabled"));
                    }
                }
            } else {
                main.messageUtility.sendMessage(sender, main.localizationUtility.getLocalizedPhrase("commands.back.nolocation"));
            }
        } else {
            main.messageUtility.sendMessage(sender, main.localizationUtility.getLocalizedPhrase("commands.back.playeronly"));
        }


        return true;
    }
}
