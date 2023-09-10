package krisapps.easywarpreloaded.events.listeners;

import krisapps.easywarpreloaded.EasyWarpReloaded;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    EasyWarpReloaded main;

    public PlayerJoinListener(EasyWarpReloaded main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!main.messageUtility.getReminders().isEmpty()) {
            for (String playerUUID : main.messageUtility.getReminders()) {
                OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(playerUUID);
                if (offPlayer.isOnline()) {
                    for (String reminderMessage : main.pluginData.getConfigurationSection("reminders." + playerUUID).getKeys(false)) {
                        main.messageUtility.sendMessage(offPlayer.getPlayer(), main.pluginData.getString("reminders." + playerUUID + "." + reminderMessage));
                        main.pluginData.set("reminders." + playerUUID + "." + reminderMessage, null);
                        main.saveData();
                        main.appendToLog("Successfully sent reminder to " + playerUUID + ": " + reminderMessage);
                    }
                } else {
                    main.appendToLog("Could not send reminder to " + playerUUID + ": offline.");
                }
            }
        }
    }

}
