package krisapps.easywarpreloaded.commands;

import krisapps.easywarpreloaded.EasyWarpReloaded;
import krisapps.easywarpreloaded.types.WarpOperationResult;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CreateWarpCommand implements CommandExecutor {

    EasyWarpReloaded main = EasyWarpReloaded.instance;


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Syntax: /createwarp <private/public> <warpId> [displayName]

        if (!(sender instanceof Player)) {
            main.messageUtility.sendMessage(sender,
                    main.localizationUtility.getLocalizedString("commands.createwarp.not_a_player")
            );
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 2) {
            main.messageUtility.sendMessage(sender,
                    main.localizationUtility.getLocalizedString("commands.createwarp.syntax")
            );
            return true;
        }

        String displayName;
        String warpId = args[1];
        boolean isPrivate = args[0].equalsIgnoreCase("private");

        // If a display name is provided, save it, otherwise set it equal to the warp id.
        if (args.length >= 3) {
            StringBuilder displayNameBuilder = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                displayNameBuilder.append(args[i]).append(" ");
            }
            displayName = displayNameBuilder.toString().trim();
        } else {
            displayName = warpId;
        }

        if (isPrivate) {
            WarpOperationResult.CreatePrivateWarp result = main.warpUtility.createPrivateWarp(warpId,
                    displayName,
                    player.getLocation(),
                    player.getUniqueId()
            );
            switch (result) {
                case SUCCESS:
                    main.messageUtility.sendMessage(sender,
                            main.localizationUtility
                                    .getLocalizedString("commands.createwarp.private.success")
                                    .replace("%warp%", displayName)
                    );
                    break;
                case ID_TAKEN:
                    main.messageUtility.sendMessage(sender,
                            main.localizationUtility.getLocalizedString("commands.createwarp.private.taken")
                    );
                    break;
                case IO_ERROR:
                    main.messageUtility.sendMessage(sender,
                            main.localizationUtility.getLocalizedString("commands.createwarp.private.io_error")
                    );
                    break;
            }
        } else {
            WarpOperationResult.CreatePublicWarp result = main.warpUtility.createPublicWarp(warpId,
                    displayName,
                    player.getLocation(),
                    player.getUniqueId()
            );
            switch (result) {
                case SUCCESS:
                    main.messageUtility.sendMessage(sender,
                            main.localizationUtility
                                    .getLocalizedString("commands.createwarp.public.success")
                                    .replace("%warp%", displayName)
                    );
                    break;
                case ID_TAKEN:
                    main.messageUtility.sendMessage(sender,
                            main.localizationUtility.getLocalizedString("commands.createwarp.public.taken")
                    );
                    break;
                case IO_ERROR:
                    main.messageUtility.sendMessage(sender,
                            main.localizationUtility.getLocalizedString("commands.createwarp.public.io_error")
                    );
                    break;
            }
        }
        return true;
    }
}
