package krisapps.easywarpreloaded.util;

import krisapps.easywarpreloaded.EasyWarpReloaded;
import krisapps.easywarpreloaded.types.WarpProperty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WarpUtility {

    EasyWarpReloaded main;

    public WarpUtility(EasyWarpReloaded main) {
        this.main = main;
    }

    public void warpPlayer(Player player, String warpID, boolean isPrivate) {
        if (!isPrivate) {
            Location warpLocation = (Location) main.dataUtility.getProperty(WarpProperty.LOCATION, warpID, false);
            if (warpLocation != null) {

                // Check if dimensional warping is disabled
                if (!Boolean.parseBoolean(main.dataUtility.getConfigSetting("settings.allow-dimensional-warping")) && !warpLocation.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                    main.messageUtility.sendMessage(player, main.localizationUtility.getLocalizedPhrase("commands.warp.dim-disabled"));
                    return;
                }


                main.messageUtility.sendMessage(player, main.localizationUtility.getLocalizedPhrase("messages.warping")
                        .replaceAll("%warp%", main.dataUtility.getProperty(WarpProperty.DISPLAY_NAME, warpID, false).toString())
                );
                main.pluginData.set("latestLocation." + player.getUniqueId(), player.getLocation());
                main.saveData();
                player.teleport(warpLocation);
                if (!main.dataUtility.getWelcomeMessage(warpID, false).isEmpty() && main.dataUtility.getWelcomeMessage(warpID, false) != null) {
                    main.messageUtility.sendMessage(player, main.dataUtility.getWelcomeMessage(warpID, false));
                }
            } else {
                main.appendToLog("Failed to warp player " + player.getName() + " to warp #" + warpID + " - location not found.");
            }
        } else {
            Location warpLocation = (Location) main.dataUtility.getProperty(WarpProperty.LOCATION, warpID, true);
            if (warpLocation != null) {

                // Check if dimensional warping is disabled
                if (!Boolean.parseBoolean(main.dataUtility.getConfigSetting("settings.allow-dimensional-warping")) && !warpLocation.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                    main.messageUtility.sendMessage(player, main.localizationUtility.getLocalizedPhrase("commands.warp.dim-disabled"));
                    return;
                }

                main.messageUtility.sendMessage(player, main.localizationUtility.getLocalizedPhrase("messages.warping")
                        .replaceAll("%warp%", main.dataUtility.getProperty(WarpProperty.DISPLAY_NAME, warpID, true).toString())
                );
                main.pluginData.set("latestLocation." + player.getUniqueId(), player.getLocation());
                main.saveData();
                player.teleport(warpLocation);
                if (main.dataUtility.getWelcomeMessage(warpID, true) != null && !main.dataUtility.getWelcomeMessage(warpID, true).isEmpty()) {
                    main.messageUtility.sendMessage(player, main.dataUtility.getWelcomeMessage(warpID, true));
                }
            } else {
                main.appendToLog("Failed to warp player " + player.getName() + " to warp #" + warpID + " - location not found.");
            }
        }
    }

}
