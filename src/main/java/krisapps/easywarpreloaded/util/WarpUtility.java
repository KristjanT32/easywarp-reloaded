package krisapps.easywarpreloaded.util;

import krisapps.easywarpreloaded.EasyWarpReloaded;
import krisapps.easywarpreloaded.types.WarpOperationResult;
import krisapps.easywarpreloaded.types.WarpProperty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class WarpUtility {

    private final String PUBLIC_WARPS_PATH = "public-warps.";
    private final String PRIVATE_WARPS_PATH = "private-warps.";
    EasyWarpReloaded main = EasyWarpReloaded.instance;

    public void warpPlayer(Player player, String warpID, boolean isPrivate) {
        if (!isPrivate) {
            Location warpLocation = (Location) main.dataUtility.getProperty(WarpProperty.LOCATION, warpID, false);
            if (warpLocation != null) {

                // Check if dimensional warping is disabled
                if (!Boolean.parseBoolean(main.dataUtility.getConfigSetting("settings.allow-dimensional-warping")) && !warpLocation.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                    main.messageUtility.sendMessage(player,
                            main.localizationUtility.getLocalizedString("commands.warp.dim-disabled")
                    );
                    return;
                }


                main.messageUtility.sendMessage(player, main.localizationUtility
                        .getLocalizedString("messages.warping")
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
                    main.messageUtility.sendMessage(player,
                            main.localizationUtility.getLocalizedString("commands.warp.dim-disabled")
                    );
                    return;
                }

                main.messageUtility.sendMessage(player, main.localizationUtility
                        .getLocalizedString("messages.warping")
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

    // Logic for warping
    public void warp(Player player, String warpId) {

    }

    // Logic for the /back command to work
    public void returnPlayer(Player player) {

    }

    public WarpOperationResult.CreatePublicWarp createPublicWarp(String warpId, String displayName, Location location, UUID creator) {
        if (publicWarpExists(warpId)) {return WarpOperationResult.CreatePublicWarp.ID_TAKEN;}

        main.pluginData.set(PUBLIC_WARPS_PATH + warpId + ".displayName", displayName);
        main.pluginData.set(PUBLIC_WARPS_PATH + warpId + ".creator", creator.toString());
        main.pluginData.set(PUBLIC_WARPS_PATH + warpId + ".creationDate", new Date());
        main.pluginData.set(PUBLIC_WARPS_PATH + warpId + ".location.x", location.getBlockX());
        main.pluginData.set(PUBLIC_WARPS_PATH + warpId + ".location.y", location.getBlockY());
        main.pluginData.set(PUBLIC_WARPS_PATH + warpId + ".location.z", location.getBlockZ());
        main.pluginData.set(PUBLIC_WARPS_PATH + warpId + ".location.dim", location.getWorld().getEnvironment());

        boolean success = main.saveData();

        return !success ? WarpOperationResult.CreatePublicWarp.IO_ERROR : WarpOperationResult.CreatePublicWarp.SUCCESS;
    }

    public WarpOperationResult.CreatePrivateWarp createPrivateWarp(String warpId, String displayName, Location location, UUID creator) {
        if (privateWarpExists(warpId)) {return WarpOperationResult.CreatePrivateWarp.ID_TAKEN;}

        main.pluginData.set(PRIVATE_WARPS_PATH + warpId + ".displayName", displayName);
        main.pluginData.set(PRIVATE_WARPS_PATH + warpId + ".creator", creator.toString());
        main.pluginData.set(PRIVATE_WARPS_PATH + warpId + ".creationDate", new Date());
        main.pluginData.set(PRIVATE_WARPS_PATH + warpId + ".location.x", location.getBlockX());
        main.pluginData.set(PRIVATE_WARPS_PATH + warpId + ".location.y", location.getBlockY());
        main.pluginData.set(PRIVATE_WARPS_PATH + warpId + ".location.z", location.getBlockZ());
        main.pluginData.set(PRIVATE_WARPS_PATH + warpId + ".location.dim", location.getWorld().getEnvironment());
        boolean success = main.saveData();

        return !success
                ? WarpOperationResult.CreatePrivateWarp.IO_ERROR
                : WarpOperationResult.CreatePrivateWarp.SUCCESS;
    }

    public boolean publicWarpExists(String warpId) {
        if (!publicWarpsExist()) {return false;}
        return getPublicWarps().contains(warpId);
    }

    public boolean privateWarpExists(String warpId) {
        if (!privateWarpsExist()) {return false;}
        return getPrivateWarps().contains(warpId);
    }

    public boolean publicWarpsExist() {
        if (main.pluginData.getConfigurationSection("public-warps") == null) {return false;}
        return !main.pluginData.getConfigurationSection("public-warps").getKeys(false).isEmpty();
    }

    public boolean privateWarpsExist() {
        if (main.pluginData.getConfigurationSection("private-warps") == null) {return false;}
        return !main.pluginData.getConfigurationSection("private-warps").getKeys(false).isEmpty();
    }

    public Set<String> getPublicWarps() {
        if (!publicWarpsExist()) {return new HashSet<>(0);}
        return main.pluginData.getConfigurationSection("public-warps").getKeys(false);
    }

    public Set<String> getPrivateWarps() {
        if (!privateWarpsExist()) {return new HashSet<>(0);}
        return main.pluginData.getConfigurationSection("private-warps").getKeys(false);
    }


    public void deleteWarp(String warpId) {

    }

    public void createInvite(String warp, UUID player, int uses) {

    }

    public void invalidateInvite(String inviteId) {

    }

    public void invalidateAllInvites(String warp) {

    }

    public void getInvites(String warp) {

    }

    public int getInviteRemainingUses(String inviteId) {

    }

}
