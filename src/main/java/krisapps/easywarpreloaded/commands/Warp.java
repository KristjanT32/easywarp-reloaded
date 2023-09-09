package krisapps.easywarpreloaded.commands;

import krisapps.easywarpreloaded.EasyWarpReloaded;
import krisapps.easywarpreloaded.types.WarpProperty;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Warp implements CommandExecutor {

    EasyWarpReloaded main;

    public Warp(EasyWarpReloaded main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        // Syntax: /warp <warp/[-i]> [inviteID]
        if (args.length == 1) {
            String warpID = args[0];

            if (main.dataUtility.publicWarpExists(warpID)) {
                main.warpUtility.warpPlayer(((Player) sender), warpID, false);
            } else {
                if (main.dataUtility.privateWarpExists(warpID)) {
                    if (main.dataUtility.getProperty(WarpProperty.OWNER, warpID, true).toString().equals(((Player) sender).getUniqueId())) {
                        main.warpUtility.warpPlayer(((Player) sender), warpID, true);
                    } else {
                        main.messageUtility.sendMessage(sender, main.localizationUtility.getLocalizedPhrase("commands.warp.notyours"));
                    }
                } else {
                    main.messageUtility.sendMessage(sender, main.localizationUtility.getLocalizedPhrase("commands.warp.notfound")
                            .replaceAll("%warp%", warpID)
                    );
                }
            }
        } else if (args.length >= 2) {
            String flag = args[0];
            String inviteUUID = args[1];

            if (flag.contains("-i")) {
                String warpID = main.dataUtility.getWarpByInvite(UUID.fromString(inviteUUID));
                if (warpID != null) {
                    if (main.dataUtility.getInviteUses(UUID.fromString(inviteUUID)) > 0) {
                        main.warpUtility.warpPlayer((Player) sender, warpID, main.dataUtility.isPrivate(warpID));
                    } else {
                        main.messageUtility.sendMessage(sender, main.localizationUtility.getLocalizedPhrase("commands.warp.invite-invalid-used"));
                    }
                } else {
                    main.messageUtility.sendMessage(sender, main.localizationUtility.getLocalizedPhrase("commands.warp.invalid"));
                }
            } else {
                main.appendToLog("Couldn't resolve flag: '" + flag + '\'');
            }
        }

        return true;
    }
}
