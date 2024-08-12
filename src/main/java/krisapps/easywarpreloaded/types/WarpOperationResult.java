package krisapps.easywarpreloaded.types;

public class WarpOperationResult {

    public enum CreatePublicWarp {
        SUCCESS, ID_TAKEN, IO_ERROR
    }

    public enum CreatePrivateWarp {
        SUCCESS, ID_TAKEN, IO_ERROR
    }
}
