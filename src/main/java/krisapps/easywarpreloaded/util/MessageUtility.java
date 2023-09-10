package krisapps.easywarpreloaded.util;

import krisapps.easywarpreloaded.EasyWarpReloaded;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MessageUtility {

    EasyWarpReloaded main;
    LocalizationUtility localizationUtility;

    public MessageUtility(EasyWarpReloaded main) {
        this.main = main;
        localizationUtility = new LocalizationUtility(main);
    }

    /**
     * Send a message (with color codes) to a player.
     * Messages with a placeholder will not get
     * the placeholder replaced.
     *
     * @param target  the player who receives the message
     * @param message the message to send
     * @
     */
    public void sendMessage(CommandSender target, String message) {
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void trySendMessage(UUID target, String message) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(target);
        if (offlinePlayer.isOnline()) {
            sendMessage(offlinePlayer.getPlayer(), message);
        } else {
            main.messageUtility.scheduleRemind(target, message);
        }
    }

    /**
     * Sends a title with the color codes interpreted.
     *
     * @param target   the player to send the title to.
     * @param title    the title content
     * @param subtitle the subtitle content
     * @param fadeIn   time for the title to fade in for (in ticks)
     * @param stay     time for the title to stay on screen for (in ticks)
     * @param fadeOut  time for the title to fade out for (in ticks)
     */
    public void sendTitle(Player target, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        target.sendTitle(ChatColor.translateAlternateColorCodes('&', title), ChatColor.translateAlternateColorCodes('&', subtitle), fadeIn, stay, fadeOut);
    }

    public void sendActionbarMessage(Player target, String text) {
        target.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&', text)));
    }

    public BaseComponent createClickableTeleportButton(String textPath, Location target, @Nullable String hoverTextPath) {
        BaseComponent[] component = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', main.localizationUtility.getLocalizedPhrase(textPath)));
        TextComponent out = new TextComponent(component);
        out.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp @p " + target.getBlockX() + " " + target.getBlockY() + " " + target.getBlockZ()));
        if (hoverTextPath != null) {
            out.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.translateAlternateColorCodes('&', main.localizationUtility.getLocalizedPhrase(hoverTextPath)))));
        }
        return out;
    }

    public BaseComponent createFileButton(String textPath, String filePath, @Nullable String hoverTextPath) {
        BaseComponent[] component = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', main.localizationUtility.getLocalizedPhrase(textPath)));
        TextComponent out = new TextComponent(component);
        if (hoverTextPath != null) {
            out.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.translateAlternateColorCodes('&', main.localizationUtility.getLocalizedPhrase(hoverTextPath).replaceAll("%path%", filePath)))));
        }
        return out;
    }

    public TextComponent createClickableButton(String textPath, String command, @Nullable String hoverTextPath) {
        BaseComponent[] component = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', main.localizationUtility.getLocalizedPhrase(textPath)));
        TextComponent out = new TextComponent(component);
        out.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        if (hoverTextPath != null) {
            out.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.translateAlternateColorCodes('&', main.localizationUtility.getLocalizedPhrase(hoverTextPath)))));
        }
        return out;
    }

    public void scheduleRemind(UUID player, String message) {
        main.pluginData.set("reminders." + player + ".message" + (getRemindersFor(player) > 0 ? getRemindersFor(player) + 1 : 1), message);
        main.saveData();
    }

    public Set<String> getReminders() {
        return main.pluginData.getConfigurationSection("reminders") != null ? main.pluginData.getConfigurationSection("reminders").getKeys(false) : new HashSet<>(0);
    }

    private int getRemindersFor(UUID target) {
        return main.pluginData.getConfigurationSection("reminders." + target).getKeys(false).size();
    }

}
