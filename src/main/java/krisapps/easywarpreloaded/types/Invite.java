package krisapps.easywarpreloaded.types;

import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class Invite {

    private String warpID;
    private UUID inviteID;
    private int usesLeft;
    private OfflinePlayer from;
    private OfflinePlayer to;

    public Invite(String warpID, UUID inviteID, int usesLeft, OfflinePlayer from, OfflinePlayer to) {
        this.warpID = warpID;
        this.inviteID = inviteID;
        this.usesLeft = usesLeft;
        this.from = from;
        this.to = to;
    }

    public String getWarpID() {
        return warpID;
    }

    public UUID getInviteID() {
        return inviteID;
    }

    public int getUsesLeft() {
        return usesLeft;
    }

    public OfflinePlayer getFrom() {
        return from;
    }

    public OfflinePlayer getTo() {
        return to;
    }
}
