package krisapps.easywarpreloaded.util;

import krisapps.easywarpreloaded.EasyWarpReloaded;
import krisapps.easywarpreloaded.types.Invite;
import krisapps.easywarpreloaded.types.WarpEntry;
import krisapps.easywarpreloaded.types.WarpProperty;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DataUtility {

    EasyWarpReloaded main;

    public DataUtility(EasyWarpReloaded main) {
        this.main = main;
    }

    public String getConfigSetting(String field) {
        return main.pluginConfig.getString(field);
    }

    public static String formatTimeUnit(int unit) {
        return unit <= 9
                ? "0" + unit
                : String.valueOf(unit);
    }

    public String getCurrentLanguage() {
        return main.pluginConfig.getString("settings.language");
    }

    public void setCurrentLanguage(String lan) {
        main.pluginConfig.set("settings.language", lan);
        main.saveConfig();
    }

    public Date generateExpirationDate(Date startingDate, int durationInMinutes) {
        return new Date(startingDate.getTime() + TimeUnit.MINUTES.toMillis(durationInMinutes));
    }

    public String generateDurationString(Date start, Date current) {
        Instant startInstant = start.toInstant();
        Instant endInstant = current.toInstant();

        Duration dur = Duration.between(startInstant, endInstant);

        if (dur.isNegative()) {
            return main.localizationUtility.getLocalizedPhrase("messages.timer-moment");
        }

        long hours = Math.abs(dur.toHours());
        long minutes = Math.abs(dur.minusHours(hours).toMinutes());
        long seconds = Math.abs(dur.minusHours(hours).minusMinutes(minutes).toSeconds());

        return String.format("%s:%s:%s", formatTimeUnit((int) hours), formatTimeUnit((int) minutes), formatTimeUnit((int) seconds));
    }

    public void createWarp(String id, String displayName, Location location, Player creator, boolean isPrivate) {
        if (!isPrivate) {
            main.pluginData.set("publicwarps." + id + ".location.x", location.getBlockX());
            main.pluginData.set("publicwarps." + id + ".location.y", location.getBlockY());
            main.pluginData.set("publicwarps." + id + ".location.z", location.getBlockZ());
            main.pluginData.set("publicwarps." + id + ".location.world", location.getWorld().getUID().toString());
            main.pluginData.set("publicwarps." + id + ".location.dimension", location.getWorld().getEnvironment());
            main.pluginData.set("publicwarps." + id + ".displayName", displayName);
            main.pluginData.set("publicwarps." + id + ".creator", creator.getUniqueId().toString());
            main.pluginData.set("publicwarps." + id + ".creationDate", new Date());
            main.pluginData.set("publicwarps." + id + ".welcomeMessage", "");
            main.saveData();
        } else {
            main.pluginData.set("privatewarps." + id + ".location.x", location.getBlockX());
            main.pluginData.set("privatewarps." + id + ".location.y", location.getBlockY());
            main.pluginData.set("privatewarps." + id + ".location.z", location.getBlockZ());
            main.pluginData.set("privatewarps." + id + ".location.world", location.getWorld().getUID().toString());
            main.pluginData.set("privatewarps." + id + ".location.dimension", location.getWorld().getEnvironment());
            main.pluginData.set("privatewarps." + id + ".displayName", displayName);
            main.pluginData.set("privatewarps." + id + ".owner", creator.getUniqueId().toString());
            main.pluginData.set("privatewarps." + id + ".creationDate", new Date());
            main.pluginData.set("privatewarps." + id + ".welcomeMessage", "");
            main.saveData();
        }
    }

    public void updateWarpEntry(String id, String field, String value, boolean isPrivate) {
        if (!isPrivate) {
            main.pluginData.set("publicwarps." + id + "." + field, value);
            main.saveData();
        } else {
            main.pluginData.set("privatewarps." + id + "." + field, value);
            main.saveData();
        }
    }

    public void deleteWarp(String warpID) {
        if (!warpExists(warpID)) {
            return;
        }
        if (isPrivate(warpID)) {
            main.pluginData.set("privatewarps." + warpID, null);
        } else {
            main.pluginData.set("publicwarps." + warpID, null);
        }

        invalidateInvites(warpID);
    }

    public Set<String> getPrivateWarpsForPlayer(UUID player) {
        Set<String> warps = new HashSet<>();
        for (String warpID : getPrivateWarps()) {
            if (getOwner(warpID, true).toString().equals(player.toString())) {
                warps.add(warpID);
            }
        }
        return warps;
    }

    public Set<WarpEntry> getAllWarps() {
        Set<WarpEntry> warps = new HashSet<>();

        for (String warp : getPublicWarps()) {
            warps.add(new WarpEntry(warp, UUID.fromString(getProperty(WarpProperty.CREATOR, warp, false).toString()), false));
        }
        for (String warp : getPrivateWarps()) {
            warps.add(new WarpEntry(warp, UUID.fromString(getProperty(WarpProperty.OWNER, warp, true).toString()), true));
        }

        return warps;
    }

    public Set<String> getPrivateWarps() {
        return main.pluginData.getConfigurationSection("privatewarps") != null ? main.pluginData.getConfigurationSection("privatewarps").getKeys(false) : new HashSet<>(0);
    }

    public Set<String> getPublicWarps() {
        return main.pluginData.getConfigurationSection("publicwarps") != null ? main.pluginData.getConfigurationSection("publicwarps").getKeys(false) : new HashSet<>(0);
    }

    public boolean privateWarpExists(String id) {
        return main.pluginData.getConfigurationSection("privatewarps." + id) != null;
    }

    public boolean publicWarpExists(String id) {
        return main.pluginData.getConfigurationSection("publicwarps." + id) != null;
    }

    public boolean warpExists(String id) {
        return publicWarpExists(id) || privateWarpExists(id);
    }

    public Object getProperty(WarpProperty property, String warpID, boolean isPrivate) {
        switch (property) {
            case OWNER:
                if (isPrivate) {
                    return main.pluginData.getString("privatewarps." + warpID + ".owner");
                } else {
                    return main.pluginData.getString("publicwarps." + warpID + ".creator");
                }
            case CREATOR:
                if (!isPrivate) {
                    return main.pluginData.getString("publicwarps." + warpID + ".creator");
                } else {
                    return main.pluginData.getString("privatewarps." + warpID + ".owner");
                }
            case LOCATION:
                if (isPrivate) {
                    return new Location(
                            Bukkit.getWorld(UUID.fromString(main.pluginData.getString("privatewarps." + warpID + ".location.world"))),
                            main.pluginData.getDouble("privatewarps." + warpID + ".location.x"),
                            main.pluginData.getDouble("privatewarps." + warpID + ".location.y"),
                            main.pluginData.getDouble("privatewarps." + warpID + ".location.z")
                    );
                } else {
                    return new Location(
                            Bukkit.getWorld(UUID.fromString(main.pluginData.getString("publicwarps." + warpID + ".location.world"))),
                            main.pluginData.getDouble("publicwarps." + warpID + ".location.x"),
                            main.pluginData.getDouble("publicwarps." + warpID + ".location.y"),
                            main.pluginData.getDouble("publicwarps." + warpID + ".location.z")
                    );
                }
            case CREATION_DATE:
                return main.pluginData.getObject((isPrivate ? "privatewarps." : "publicwarps.") + warpID + ".creationDate", Date.class);
            case DISPLAY_NAME:
                return main.pluginData.getString((isPrivate ? "privatewarps." : "publicwarps.") + warpID + ".displayName");
            case WELCOME_MESSAGE:
                return main.pluginData.getString((isPrivate ? "privatewarps." : "publicwarps.") + warpID + ".welcomeMessage");
            default:
                return null;
        }
    }

    public UUID createInvite(Player sender, Player target, String warpID, int uses) {
        UUID uuid = UUID.randomUUID();

        main.pluginData.set("invites." + uuid.toString() + ".creator", sender.getUniqueId().toString());
        main.pluginData.set("invites." + uuid.toString() + ".target", target.getUniqueId().toString());
        main.pluginData.set("invites." + uuid.toString() + ".warp", warpID);
        main.pluginData.set("invites." + uuid.toString() + ".uses", uses);
        main.saveData();

        return uuid;
    }

    public boolean inviteExists(UUID inviteID) {
        return main.pluginData.getConfigurationSection("invites").contains(inviteID.toString());
    }

    public String getWarpByInvite(UUID inviteID) {
        if (inviteExists(inviteID)) {
            return main.pluginData.getString("invites." + inviteID + ".warp");
        } else {
            return null;
        }
    }

    public int getInviteUses(UUID inviteID) {
        if (inviteExists(inviteID)) {
            return main.pluginData.getInt("invites." + inviteID + ".uses");
        } else {
            return 0;
        }
    }

    public boolean isPrivate(String warpID) {
        if (!warpExists(warpID)) {
            return false;
        }
        return main.pluginData.getConfigurationSection("privatewarps").contains(warpID);
    }

    public String getWelcomeMessage(String warpID, boolean isPrivate) {
        return main.pluginData.getString((isPrivate ? "privatewarps." : "publicwarps.") + warpID + ".welcomeMessage");
    }

    public UUID getOwner(String warpID, boolean isPrivate) {
        return UUID.fromString(main.pluginData.getString((isPrivate ? "privatewarps." : "publicwarps.") + warpID + (isPrivate ? ".owner" : ".creator")));
    }

    public void sendPrivateWarpInvite(Player target, Player sender, String warpID, int uses, boolean isPrivate) {
        UUID inviteID = createInvite(sender, target, warpID, uses);

        BaseComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', main.localizationUtility.getLocalizedPhrase("messages.invite-header")
                .replaceAll("%warp%", getProperty(WarpProperty.DISPLAY_NAME, warpID, isPrivate).toString())
                .replaceAll("%sender%", sender.getName())
                .replaceAll("%uses%", String.valueOf(getInviteUses(inviteID)))
        ));
        BaseComponent component2 = new TextComponent(ChatColor.translateAlternateColorCodes('&', main.localizationUtility.getLocalizedPhrase("messages.invite-button-prefix")));
        BaseComponent button = main.messageUtility.createClickableButton("messages.invite-warp-button", "/warp -i " + inviteID.toString(), "messages.hovertext.warp-button");
        BaseComponent footer = new TextComponent(ChatColor.translateAlternateColorCodes('&', main.localizationUtility.getLocalizedPhrase("messages.invite-footer")));

        target.spigot().sendMessage(component, component2, button, footer);
    }

    public void decreaseInviteUses(UUID inviteUUID, int decreaseBy) {
        if (inviteExists(inviteUUID)) {
            if (getInviteUses(inviteUUID) > 0) {
                main.pluginData.set("invites." + inviteUUID.toString() + ".uses", getInviteUses(inviteUUID) - decreaseBy);
                main.saveData();
            } else {
                main.pluginData.set("invites." + inviteUUID.toString(), null);
                main.saveData();
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    public Optional<Player> tryGetPlayer(UUID player) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        if (offlinePlayer.isOnline()) {
            return Optional.of(offlinePlayer.getPlayer());
        } else {
            return Optional.empty();
        }
    }

    public Set<String> getInvites() {
        if (main.pluginData.getConfigurationSection("invites") != null) {
            return main.pluginData.getConfigurationSection("invites").getKeys(false);
        } else {
            return new HashSet<>(0);
        }
    }

    public Set<String> getInvitesFor(String warpID) {
        Set<String> matches = new HashSet<>();
        for (String inviteUUID : getInvites()) {
            if (getWarpByInvite(UUID.fromString(inviteUUID)).equals(warpID)) {
                matches.add(inviteUUID);
            }
        }
        return matches;
    }

    public ArrayList<Invite> getInviteEntriesFor(String warpID) {
        ArrayList<Invite> invites = new ArrayList<>();
        for (String inviteUUID : getInvites()) {
            if (getWarpByInvite(UUID.fromString(inviteUUID)).equals(warpID)) {
                invites.add(new Invite(
                        main.pluginData.getString("invites." + inviteUUID + ".warp"),
                        UUID.fromString(inviteUUID),
                        main.pluginData.getInt("invites." + inviteUUID + ".uses"),
                        Bukkit.getOfflinePlayer(UUID.fromString(main.pluginData.getString("invites." + inviteUUID + ".creator"))),
                        Bukkit.getOfflinePlayer(UUID.fromString(main.pluginData.getString("invites." + inviteUUID + ".target")))
                ));
            }
        }
        return invites;
    }

    public void invalidateInvites(String warpID) {
        for (Invite invite : getInviteEntriesFor(warpID)) {
            main.appendToLog("Invalidating invite " + invite.getInviteID());
            main.pluginData.set("invites." + invite.getInviteID().toString(), null);
            main.saveData();
        }
    }

    public void invalidateInvite(String inviteID) {
        if (inviteExists(UUID.fromString(inviteID))) {
            main.pluginData.set("invites." + inviteID, null);
            main.saveData();
        }
    }

    public Location getPreviousLocation(UUID player) {
        return main.pluginData.getLocation("latestLocation." + player, null);
    }
}