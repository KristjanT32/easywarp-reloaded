package krisapps.easywarpreloaded.commands.legacy;

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
                        main.messageUtility.sendMessage(sender,
                                main.localizationUtility.getLocalizedString("commands.back.returned")
                        );
                    } else {
                        main.messageUtility.sendMessage(sender,
                                main.localizationUtility.getLocalizedString("commands.back.dim-disabled")
                        );
                    }
                } else {
                    ((Player) sender).teleport(prev);
                    main.messageUtility.sendMessage(sender,
                            main.localizationUtility.getLocalizedString("commands.back.returned")
                    );
                }
            } else {
                main.messageUtility.sendMessage(sender,
                        main.localizationUtility.getLocalizedString("commands.back.nolocation")
                );
            }
        } else {
            main.messageUtility.sendMessage(sender,
                    main.localizationUtility.getLocalizedString("commands.back.playeronly")
            );
        }
        return true;
    }
}
