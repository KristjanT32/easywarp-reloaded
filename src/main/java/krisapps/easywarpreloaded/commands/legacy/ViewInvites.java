package krisapps.easywarpreloaded.commands.legacy;

import krisapps.easywarpreloaded.EasyWarpReloaded;
import krisapps.easywarpreloaded.types.Invite;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ViewInvites implements CommandExecutor {

    EasyWarpReloaded main;

    public ViewInvites(EasyWarpReloaded main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        // Syntax: /invites <warp>/[-i] [inviteID]

        if (args.length >= 2) {
            if (args[0].equals("-i")) {
                if (main.dataUtility.inviteExists(UUID.fromString(args[1]))) {
                    main.appendToLog("Invalidated invite by user request: " + args[1]);
                    main.dataUtility.invalidateInvite(args[1]);
                    main.messageUtility.sendMessage(sender,
                            main.localizationUtility.getLocalizedString("commands.invites.invalidated")
                    );
                }
            }
        } else if (args.length == 1) {
            if (!main.dataUtility.warpExists(args[0])) {
                main.messageUtility.sendMessage(sender,
                        main.localizationUtility.getLocalizedString("commands.invites.nonexistent")
                );
                return true;
            }

            if (!main.dataUtility.getOwner(args[0], main.dataUtility.isPrivate(args[0])).toString().equals(((Player) sender).getUniqueId().toString())) {
                if (sender.isOp()) {
                    sendResponse(sender, args[0]);
                } else {
                    main.messageUtility.sendMessage(sender,
                            main.localizationUtility.getLocalizedString("commands.invites.notyours")
                    );
                }
            } else {
                sendResponse(sender, args[0]);
            }
        }

        return true;
    }

    private void sendResponse(CommandSender sender, String warpID) {
        main.messageUtility.sendMessage(sender, "&e=================================================");
        if (!main.dataUtility.getInviteEntriesFor(warpID).isEmpty()) {
            for (Invite invite : main.dataUtility.getInviteEntriesFor(warpID)) {

                BaseComponent button = main.messageUtility.createClickableButton("commands.invites.button-invalidate", "/invites -i " + invite.getInviteID(), "commands.invites.button-invalidate-hover");
                BaseComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&',
                        main.localizationUtility
                                .getLocalizedString("commands.invites.invite")
                        .replaceAll("%id%", invite.getInviteID().toString())
                        .replaceAll("%uses%", String.valueOf(invite.getUsesLeft()))
                        .replaceAll("%player%", invite.getTo().getPlayer() == null ? "unknown player" : invite.getTo().getName())));

                sender.spigot().sendMessage(message, button, new TextComponent("\n"));
            }
        } else {
            main.messageUtility.sendMessage(sender, main.localizationUtility.getLocalizedString("commands.invites.none")
                    .replaceAll("%warp%", warpID)
            );
        }
        main.messageUtility.sendMessage(sender, "&e=================================================");
    }
}
