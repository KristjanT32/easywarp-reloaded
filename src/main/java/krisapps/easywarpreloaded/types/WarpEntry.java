package krisapps.easywarpreloaded.types;

import java.util.UUID;

public class WarpEntry {

    private String warpID;
    private UUID owner;
    String type;

    public WarpEntry(String warpID, UUID owner, boolean isPrivate) {
        this.warpID = warpID;
        this.owner = owner;
        this.type = isPrivate ? "private" : "public";
    }

    public String getWarpID() {
        return warpID;
    }

    public UUID getOwner() {
        return owner;
    }

    public String getType() {
        return type;
    }
}
