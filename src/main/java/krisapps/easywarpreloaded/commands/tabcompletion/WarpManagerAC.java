package krisapps.easywarpreloaded.commands.tabcompletion;

import krisapps.easywarpreloaded.EasyWarpReloaded;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WarpManagerAC implements TabCompleter {

    EasyWarpReloaded main;

    public WarpManagerAC(EasyWarpReloaded main) {
        this.main = main;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(Arrays.asList("create", "edit", "delete", "list", "view"));
        } else if (args.length == 2) {
            switch (args[0]) {
                case "create":
                    completions.add("<warpID>");
                    break;
                case "edit":
                case "delete":
                case "view":
                    if (sender.isOp()) {
                        completions.addAll(main.dataUtility.getPublicWarps());
                        completions.addAll(main.dataUtility.getPrivateWarps());
                    } else {
                        completions.addAll(main.dataUtility.getPrivateWarpsForPlayer(((Player) sender).getUniqueId()));
                    }
                    break;
                case "list":
                    if (sender.isOp()) {
                        completions.addAll(Arrays.asList("all", "public", "private"));
                    } else {
                        completions.addAll(Arrays.asList("public", "private"));
                    }
                    break;
            }
        } else if (args.length == 3) {
            switch (args[0]) {
                case "create":
                    completions.add("private");
                    completions.add("public");
                    break;
                case "edit":
                    completions.add("display_name");
                    completions.add("location");
                    completions.add("welcome_message");
                    break;
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("edit")) {
                if (args[2].equalsIgnoreCase("location")) {
                    completions.add("current");
                    completions.add(String.valueOf(((Player) sender).getLocation().getBlockX()));
                }
            }
        } else if (args.length == 5) {
            if (args[0].equalsIgnoreCase("edit")) {
                if (args[2].equalsIgnoreCase("location")) {
                    if (!args[3].equalsIgnoreCase("current")) {
                        completions.add(String.valueOf(((Player) sender).getLocation().getBlockY()));
                    }
                }
            }
        } else if (args.length == 6) {
            if (args[0].equalsIgnoreCase("edit")) {
                if (args[2].equalsIgnoreCase("location")) {
                    if (!args[3].equalsIgnoreCase("current")) {
                        completions.add(String.valueOf(((Player) sender).getLocation().getBlockZ()));
                    }
                }
            }
        }

        if (args.length >= 4) {
            if (args[0].equalsIgnoreCase("edit")) {
                if (args[2].equalsIgnoreCase("display_name")) {
                    completions.add("<display_name>");
                } else if (args[2].equalsIgnoreCase("welcome_message")) {
                    completions.add("<welcome_message>");
                }
            }
        }

        return completions;
    }
}
