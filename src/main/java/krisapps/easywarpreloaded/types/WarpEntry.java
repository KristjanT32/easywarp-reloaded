package krisapps.easywarpreloaded.types;

import java.util.UUID;

public class WarpEntry {

    private String warpID;
    private UUID owner;

    public WarpEntry(String warpID, UUID owner) {
        this.warpID = warpID;
        this.owner = owner;
    }

    public String getWarpID() {
        return warpID;
    }

    public UUID getOwner() {
        return owner;
    }
}
