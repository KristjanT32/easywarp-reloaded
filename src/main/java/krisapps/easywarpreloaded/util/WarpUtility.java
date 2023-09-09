package krisapps.easywarpreloaded.util;

import krisapps.easywarpreloaded.EasyWarpReloaded;
import krisapps.easywarpreloaded.types.WarpProperty;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WarpUtility {

    EasyWarpReloaded main;

    public WarpUtility(EasyWarpReloaded main) {
        this.main = main;
    }

    // TODO: Add warpPlayer(player, warpID, isPrivate)
    public void warpPlayer(Player player, String warpID, boolean isPrivate) {
        if (!isPrivate) {
            Location warpLocation = (Location) main.dataUtility.getProperty(WarpProperty.LOCATION, warpID, false);
            if (warpLocation != null) {
                main.messageUtility.sendMessage(player, main.localizationUtility.getLocalizedPhrase("messages.warping")
                        .replaceAll("%warp%", main.dataUtility.getProperty(WarpProperty.DISPLAY_NAME, warpID, false).toString())
                );
                player.teleport(warpLocation);
                if (main.dataUtility.getWelcomeMessage(warpID, false) != null) {
                    main.messageUtility.sendMessage(player, main.dataUtility.getWelcomeMessage(warpID, false));
                }
            } else {
                main.appendToLog("Failed to warp player " + player.getName() + " to warp #" + warpID + " - location not found.");
            }
        } else {
            Location warpLocation = (Location) main.dataUtility.getProperty(WarpProperty.LOCATION, warpID, true);
            if (warpLocation != null) {
                main.messageUtility.sendMessage(player, main.localizationUtility.getLocalizedPhrase("messages.warping")
                        .replaceAll("%warp%", main.dataUtility.getProperty(WarpProperty.DISPLAY_NAME, warpID, true).toString())
                );
                player.teleport(warpLocation);
                if (main.dataUtility.getWelcomeMessage(warpID, true) != null) {
                    main.messageUtility.sendMessage(player, main.dataUtility.getWelcomeMessage(warpID, true));
                }
            } else {
                main.appendToLog("Failed to warp player " + player.getName() + " to warp #" + warpID + " - location not found.");
            }
        }
    }

}
