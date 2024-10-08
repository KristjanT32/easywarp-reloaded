package krisapps.easywarpreloaded.commands.legacy;

import krisapps.easywarpreloaded.EasyWarpReloaded;
import krisapps.easywarpreloaded.types.WarpEntry;
import krisapps.easywarpreloaded.types.WarpProperty;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class WarpManCommand implements CommandExecutor {

    EasyWarpReloaded main;

    public WarpManCommand(EasyWarpReloaded main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        // Syntax: /warpman <create|edit|delete|list|view|> <warpID> <none|property|none> <private>

        if (args.length >= 1) {
            switch (args[0]) {
                case "create":
                    if (args.length >= 3) {

                        if (!main.dataUtility.warpExists(args[1])) {
                            boolean isPrivate = args[2].equalsIgnoreCase("private");
                            main.dataUtility.createWarp(args[1], "Warp " + args[1], ((Player) sender).getLocation(), ((Player) sender), isPrivate);
                            main.messageUtility.sendMessage(sender,
                                    main.localizationUtility.getLocalizedString("commands.warpman.created")
                                    .replaceAll("%warp%", args[1])
                            );
                        } else {
                            main.messageUtility.sendMessage(sender,
                                    main.localizationUtility.getLocalizedString("commands.warpman.create.taken")
                                    .replaceAll("%warp%", args[1])
                            );
                        }
                    }
                    break;
                case "edit":
                    if (args.length >= 4) {
                        String warpID = args[1];
                        if (!main.dataUtility.warpExists(warpID)) {
                            main.messageUtility.sendMessage(sender,
                                    main.localizationUtility.getLocalizedString("commands.warpman.nowarp")
                            );
                            return true;
                        } else {
                            if (main.dataUtility.isPrivate(warpID)) {
                                if (!main.dataUtility.getOwner(warpID, true).toString().equals(((Player) sender).getUniqueId().toString())) {
                                    main.messageUtility.sendMessage(sender,
                                            main.localizationUtility.getLocalizedString("commands.warpman.notyours")
                                    );
                                    return true;
                                }
                            }
                        }
                        boolean isPrivate = main.dataUtility.isPrivate(warpID);

                        String property = args[2];
                        StringBuilder newValue = new StringBuilder();
                        for (int i = 3; i < args.length; i++) {
                            newValue.append(args[i]).append(" ");
                        }
                        if (newValue.toString().isEmpty()) {
                            main.messageUtility.sendMessage(sender,
                                    main.localizationUtility.getLocalizedString("commands.warpman.nonempty")
                            );
                            return true;
                        }

                        WarpProperty enumProperty = WarpProperty.valueOf(property.toUpperCase());
                        switch (enumProperty) {
                            case LOCATION:
                                // If the player has provided coordinates
                                if (newValue.toString().split(" ").length == 3) {
                                    int x = Integer.parseInt(newValue.toString().split(" ")[0]);
                                    int y = Integer.parseInt(newValue.toString().split(" ")[1]);
                                    int z = Integer.parseInt(newValue.toString().split(" ")[2]);

                                    main.dataUtility.updateWarpEntry(warpID, "location.x", String.valueOf(x),
                                            isPrivate);
                                    main.dataUtility.updateWarpEntry(warpID, "location.y", String.valueOf(y),
                                            isPrivate);
                                    main.dataUtility.updateWarpEntry(warpID, "location.z", String.valueOf(z),
                                            isPrivate);
                                    main.dataUtility.updateWarpEntry(warpID, "location.world", String.valueOf(((Player) sender).getLocation().getWorld()),
                                            isPrivate);
                                    main.dataUtility.updateWarpEntry(warpID, "location.dimension", String.valueOf(((Player) sender).getLocation().getWorld().getEnvironment()),
                                            isPrivate);

                                    main.messageUtility.sendMessage(sender,
                                            main.localizationUtility
                                                    .getLocalizedString("commands.warpman.edit.success-coords")
                                            .replaceAll("%x%", String.valueOf(x))
                                            .replaceAll("%y%", String.valueOf(y))
                                            .replaceAll("%z%", String.valueOf(z))
                                            .replaceAll("%warp%", warpID)
                                            .replaceAll("%isprivate%", isPrivate ? "(private)" : "")
                                    );

                                } else {
                                    // If the player wants to set the warp position to their own position.
                                    if (newValue.toString().contains("current")) {
                                        Location locationToSet = ((Player) sender).getLocation();
                                        main.dataUtility.updateWarpEntry(warpID, "location.x", String.valueOf(locationToSet.getBlockX()),
                                                isPrivate);
                                        main.dataUtility.updateWarpEntry(warpID, "location.y", String.valueOf(locationToSet.getBlockY()),
                                                isPrivate);
                                        main.dataUtility.updateWarpEntry(warpID, "location.z", String.valueOf(locationToSet.getBlockZ()),
                                                isPrivate);
                                        main.dataUtility.updateWarpEntry(warpID, "location.world", String.valueOf(locationToSet.getWorld().getUID()),
                                                isPrivate);
                                        main.dataUtility.updateWarpEntry(warpID, "location.dimension", String.valueOf(locationToSet.getWorld().getEnvironment()),
                                                isPrivate);

                                        main.messageUtility.sendMessage(sender,
                                                main.localizationUtility
                                                        .getLocalizedString("commands.warpman.edit.success-current")
                                                .replaceAll("%x%", String.valueOf(locationToSet.getBlockX()))
                                                .replaceAll("%y%", String.valueOf(locationToSet.getBlockY()))
                                                .replaceAll("%z%", String.valueOf(locationToSet.getBlockZ()))
                                                .replaceAll("%warp%", warpID)
                                                .replaceAll("%isprivate%", isPrivate ? "(private)" : "")
                                        );
                                    }
                                }
                                break;
                            case DISPLAY_NAME:
                                main.dataUtility.updateWarpEntry(warpID, "displayName", newValue.toString().trim(), isPrivate);
                                main.messageUtility.sendMessage(sender,
                                        main.localizationUtility
                                                .getLocalizedString("commands.warpman.edit.success-generic")
                                        .replaceAll("%property%", property)
                                        .replaceAll("%value%", newValue.toString().trim())
                                        .replaceAll("%warp%", warpID)
                                );
                                break;
                            case WELCOME_MESSAGE:
                                main.dataUtility.updateWarpEntry(warpID, "welcomeMessage", newValue.toString().trim(), isPrivate);
                                main.messageUtility.sendMessage(sender,
                                        main.localizationUtility
                                                .getLocalizedString("commands.warpman.edit.success-generic")
                                        .replaceAll("%property%", property)
                                        .replaceAll("%value%", newValue.toString().trim())
                                        .replaceAll("%warp%", warpID)
                                );
                                break;
                            default:
                                main.getLogger().info("Unknown property: " + property);
                                break;
                        }
                    } else {
                        return false;
                    }
                    break;
                case "delete":
                    if (args.length >= 2) {
                        String warpID = args[1];
                        if (!main.dataUtility.warpExists(warpID)) {
                            main.messageUtility.sendMessage(sender,
                                    main.localizationUtility.getLocalizedString("commands.warpman.delete.nonexistent")
                                    .replaceAll("%warp%", warpID)
                            );
                            return true;
                        }
                        // Deleting a private warp
                        if (main.dataUtility.isPrivate(warpID)) {
                            // If the sender is not the owner of the warp in question.
                            if (!main.dataUtility.getOwner(warpID, true).toString().equals(((Player) sender).getUniqueId().toString())) {
                                if (!sender.isOp()) {
                                    main.messageUtility.sendMessage(sender,
                                            main.localizationUtility.getLocalizedString(
                                                    "commands.warpmap.delete.notyours")
                                    );
                                } else {
                                    main.messageUtility.sendMessage(sender,
                                            main.localizationUtility.getLocalizedString(
                                                    "commands.warpman.delete.deleted-notown")
                                    );
                                    main.messageUtility.trySendMessage(main.dataUtility.getOwner(warpID, true),
                                            main.localizationUtility.getLocalizedString("messages.op-warp-deletion")
                                            .replaceAll("%warp%", warpID)
                                            .replaceAll("%op%", sender.getName())
                                    );
                                    main.dataUtility.deleteWarp(warpID);
                                }
                            } else {
                                main.dataUtility.deleteWarp(warpID);
                                main.messageUtility.sendMessage(sender,
                                        main.localizationUtility.getLocalizedString(
                                                "commands.warpman.delete.deleted-own")
                                );
                            }
                        } else {
                            // Deleting a public warp
                            if (!main.dataUtility.getOwner(warpID, true).toString().equals(((Player) sender).getUniqueId().toString())) {
                                if (!sender.isOp()) {
                                    main.messageUtility.sendMessage(sender,
                                            main.localizationUtility.getLocalizedString(
                                                    "commands.warpmap.delete.notyours")
                                    );
                                } else {
                                    main.dataUtility.deleteWarp(warpID);
                                    main.messageUtility.sendMessage(sender,
                                            main.localizationUtility.getLocalizedString(
                                                    "commands.warpman.delete.deleted-public")
                                    );
                                }
                            } else {
                                main.dataUtility.deleteWarp(warpID);
                                main.messageUtility.sendMessage(sender,
                                        main.localizationUtility.getLocalizedString(
                                                "commands.warpman.delete.deleted-public")
                                );
                            }
                        }
                    } else {
                        return false;
                    }
                    break;
                case "list":
                    if (args.length >= 2) {
                        String type = args[1];
                        switch (type) {
                            case "all":
                                if (sender.isOp()) {
                                    main.messageUtility.sendMessage(sender, "&e========================================");
                                    main.messageUtility.sendMessage(sender,
                                            main.localizationUtility
                                                    .getLocalizedString("commands.warpman.list.header")
                                                    .replaceAll("%type%",
                                                            main.localizationUtility.getLocalizedString(
                                                                    "commands.warpman.list.list-type-all")
                                                    )
                                    );
                                    for (WarpEntry warpEntry : main.dataUtility.getAllWarps()) {
                                        Optional<Player> owner = main.dataUtility.tryGetPlayer(warpEntry.getOwner());
                                        main.messageUtility.sendMessage(sender,
                                                main.localizationUtility
                                                        .getLocalizedString("commands.warpman.list.list-item")
                                                .replaceAll("%warp%",
                                                        (warpEntry.getType().equalsIgnoreCase("public")
                                                                ? "&b&l" + main.localizationUtility.getLocalizedString(
                                                                "commands.warpman.view.warp-type-public")
                                                                : "&c&l" + main.localizationUtility.getLocalizedString(
                                                                        "commands.warpman.view.warp-type-private")
                                                        ) + " &r&e-&r " +
                                                                warpEntry.getWarpID())
                                                .replaceAll("%owner%", owner.isPresent() ? owner.get().getName() : "&cN/A")
                                        );
                                    }
                                    main.messageUtility.sendMessage(sender, "&e========================================");
                                } else {
                                    main.messageUtility.sendMessage(sender,
                                            main.localizationUtility.getLocalizedString(
                                                    "commands.warpman.list.notallowed")
                                    );
                                }
                                break;
                            case "public":
                                if (!main.dataUtility.getPublicWarps().isEmpty()) {
                                    main.messageUtility.sendMessage(sender, "&e========================================");
                                    main.messageUtility.sendMessage(sender,
                                            main.localizationUtility
                                                    .getLocalizedString("commands.warpman.list.header")
                                                    .replaceAll("%type%",
                                                            main.localizationUtility.getLocalizedString(
                                                                    "commands.warpman.list.list-type-public")
                                                    )
                                    );

                                    for (String warpID : main.dataUtility.getPublicWarps()) {
                                        Optional<Player> owner = main.dataUtility.tryGetPlayer(main.dataUtility.getOwner(warpID, false));
                                        main.messageUtility.sendMessage(sender,
                                                main.localizationUtility
                                                        .getLocalizedString("commands.warpman.list.list-item")
                                                .replaceAll("%warp%", warpID)
                                                .replaceAll("%owner%", owner.isPresent() ? owner.get().getName() : "&cN/A")
                                        );
                                    }
                                    main.messageUtility.sendMessage(sender, "&e========================================");
                                } else {
                                    main.messageUtility.sendMessage(sender,
                                            main.localizationUtility.getLocalizedString(
                                                    "commands.warpman.list.empty-public")
                                    );
                                }
                                break;
                            case "private":
                                if (!sender.isOp()) {
                                    if (!main.dataUtility.getPrivateWarpsForPlayer(((Player) sender).getUniqueId()).isEmpty()) {
                                        main.messageUtility.sendMessage(sender, "&e========================================");
                                        main.messageUtility.sendMessage(sender,
                                                main.localizationUtility
                                                        .getLocalizedString("commands.warpman.list.header")
                                                        .replaceAll("%type%",
                                                                main.localizationUtility.getLocalizedString(
                                                                        "commands.warpman.list.list-type-private")
                                                        )
                                        );

                                        for (String warpID : main.dataUtility.getPrivateWarpsForPlayer(((Player) sender).getUniqueId())) {
                                            Optional<Player> owner = main.dataUtility.tryGetPlayer(main.dataUtility.getOwner(warpID, true));
                                            main.messageUtility.sendMessage(sender,
                                                    main.localizationUtility
                                                            .getLocalizedString("commands.warpman.list.list-item")
                                                    .replaceAll("%warp%", warpID)
                                                    .replaceAll("%owner%", owner.isPresent() ? owner.get().getName() : "&cN/A")
                                            );
                                        }
                                        main.messageUtility.sendMessage(sender, "&e========================================");
                                    } else {
                                        main.messageUtility.sendMessage(sender,
                                                main.localizationUtility.getLocalizedString(
                                                        "commands.warpman.list.empty-private")
                                        );
                                    }
                                } else {
                                    if (!main.dataUtility.getPrivateWarps().isEmpty()) {
                                        main.messageUtility.sendMessage(sender, "&e========================================");
                                        main.messageUtility.sendMessage(sender,
                                                main.localizationUtility
                                                        .getLocalizedString("commands.warpman.list.header")
                                                        .replaceAll("%type%",
                                                                main.localizationUtility.getLocalizedString(
                                                                        "commands.warpman.list.list-type-private")
                                                        )
                                        );

                                        for (String warpID : main.dataUtility.getPrivateWarps()) {
                                            Optional<Player> owner = main.dataUtility.tryGetPlayer(main.dataUtility.getOwner(warpID, true));
                                            main.messageUtility.sendMessage(sender,
                                                    main.localizationUtility
                                                            .getLocalizedString("commands.warpman.list.list-item")
                                                    .replaceAll("%warp%", warpID)
                                                    .replaceAll("%owner%", owner.isPresent() ? owner.get().getName() : "&cN/A")
                                            );
                                        }
                                        main.messageUtility.sendMessage(sender, "&e========================================");
                                    } else {
                                        main.messageUtility.sendMessage(sender,
                                                main.localizationUtility.getLocalizedString(
                                                        "commands.warpman.list.empty-private-op")
                                        );
                                    }
                                }
                                break;
                        }
                    } else {
                        return false;
                    }
                    break;
                case "view":
                    if (args.length >= 2) {
                        String warpID = args[1];
                        if (main.dataUtility.warpExists(warpID)) {
                            if (main.dataUtility.isPrivate(warpID)) {
                                if (main.dataUtility.getOwner(warpID, true) == ((Player) sender).getUniqueId()) {
                                    sendResponse(sender, warpID, true);
                                } else {
                                    if (!sender.isOp()) {
                                        main.messageUtility.sendMessage(sender,
                                                main.localizationUtility.getLocalizedString(
                                                        "commands.warpman.view.notyours")
                                        );
                                    } else {
                                        sendResponse(sender, warpID, true);
                                    }
                                }
                            } else {
                                sendResponse(sender, warpID, false);
                            }
                        } else {
                            main.messageUtility.sendMessage(sender,
                                    main.localizationUtility.getLocalizedString("commands.warpman.view.notfound")
                                    .replaceAll("%warp%", warpID)
                            );
                        }
                    } else {
                        return false;
                    }

                    break;
            }
        } else {
            return false;
        }


        return true;
    }

    private void sendResponse(CommandSender sender, String warpID, boolean isPrivate) {
        Location warpLocation = (Location) main.dataUtility.getProperty(WarpProperty.LOCATION, warpID, isPrivate);
        Optional<Player> owner = main.dataUtility.tryGetPlayer(main.dataUtility.getOwner(warpID, isPrivate));
        main.messageUtility.sendMessage(sender,
                main.localizationUtility
                        .getLocalizedString("commands.warpman.view.response")
                .replaceAll("%warp%", warpID)
                .replaceAll("%name%", main.dataUtility.getProperty(WarpProperty.DISPLAY_NAME, warpID, isPrivate).toString())
                .replaceAll("%owner%", owner.isPresent() ? owner.get().getName() : main.dataUtility.getOwner(warpID, isPrivate).toString())
                .replaceAll("%x%", String.valueOf(warpLocation.getBlockX()))
                .replaceAll("%y%", String.valueOf(warpLocation.getBlockY()))
                .replaceAll("%z%", String.valueOf(warpLocation.getBlockZ()))
                .replaceAll("%dimension%", String.valueOf(warpLocation.getWorld().getEnvironment()))
                        .replaceAll("%type%",
                                (isPrivate
                                        ? main.localizationUtility.getLocalizedString(
                                        "commands.warpman.view.warp-type-private")
                                        : main.localizationUtility.getLocalizedString(
                                                "commands.warpman.view.warp-type-public"))
                        )
                .replaceAll("%creationDate%", main.dataUtility.getProperty(WarpProperty.CREATION_DATE, warpID, isPrivate).toString())
        );
    }
}
