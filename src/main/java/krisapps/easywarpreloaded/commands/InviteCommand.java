package krisapps.easywarpreloaded.commands;

import krisapps.easywarpreloaded.EasyWarpReloaded;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InviteCommand implements CommandExecutor {

    EasyWarpReloaded main;

    public InviteCommand(EasyWarpReloaded main) {
        this.main = main;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        // Syntax: /invite <player> <warp> <uses>

        if (sender instanceof Player) {

            if (args.length >= 3) {
                String playerName = args[0];
                String warpID = args[1];
                int uses = Integer.parseInt(args[2]);

                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
                if (offlinePlayer.isOnline()) {
                    main.dataUtility.sendPrivateWarpInvite(offlinePlayer.getPlayer(), (Player) sender, warpID, uses);
                    main.messageUtility.sendMessage(sender, main.localizationUtility.getLocalizedPhrase("commands.invite.sent")
                            .replaceAll("%player%", playerName)
                    );
                } else {
                    if (offlinePlayer.hasPlayedBefore()) {
                        main.messageUtility.sendMessage(sender, main.localizationUtility.getLocalizedPhrase("commands.invite.offline")
                                .replaceAll("%player%", playerName)
                        );
                    } else {
                        main.messageUtility.sendMessage(sender, main.localizationUtility.getLocalizedPhrase("commands.invite.invalidplayer")
                                .replaceAll("%player%", playerName)
                        );
                    }
                }
            } else {
                return false;
            }
        } else {
            main.messageUtility.sendMessage(sender, main.localizationUtility.getLocalizedPhrase("commands.invite.playeronly"));
        }
        return true;
    }
}
