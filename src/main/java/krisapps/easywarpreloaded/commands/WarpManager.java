package krisapps.easywarpreloaded.commands;

import krisapps.easywarpreloaded.EasyWarpReloaded;
import krisapps.easywarpreloaded.types.WarpProperty;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpManager implements CommandExecutor {

    EasyWarpReloaded main;

    public WarpManager(EasyWarpReloaded main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        // Syntax: /warpman <create|edit|delete|list|view|> <warpID> <none|property|none> <private>

        if (args.length >= 1) {
            switch (args[0]) {
                case "create":
                    if (args.length >= 3) {
                        boolean isPrivate = args[2].equalsIgnoreCase("private");
                        main.dataUtility.createWarp(args[1], "Warp " + args[1], ((Player) sender).getLocation(), ((Player) sender), isPrivate);
                    }
                    break;
                case "edit":
                    if (args.length >= 4) {
                        String warpID = args[1];

                        if (!main.dataUtility.warpExists(warpID)) {
                            main.messageUtility.sendMessage(sender, main.localizationUtility.getLocalizedPhrase("commands.warpman.nowarp"));
                            return true;
                        } else {
                            if (main.dataUtility.isPrivate(warpID) && main.dataUtility.getProperty(WarpProperty.OWNER, warpID, true) != ((Player) sender).getUniqueId()) {
                                main.messageUtility.sendMessage(sender, main.localizationUtility.getLocalizedPhrase("commands.warpman.notyours"));
                                return true;
                            }
                        }

                        String property = args[2];
                        StringBuilder newValue = new StringBuilder();
                        for (int i = 3; i < args.length; i++) {
                            newValue.append(args[i]).append(" ");
                        }

                        // TODO: Edit logic

                    }
                    break;
                case "delete":
                    break;
                case "list":
                    break;
                case "view":
                    break;
            }
        }


        return true;
    }
}
