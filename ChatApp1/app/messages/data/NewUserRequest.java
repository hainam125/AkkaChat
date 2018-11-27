package messages.data;

import java.io.Serializable;

public class NewUserRequest implements Serializable {
    private final long localId;
    private final String server;
    public NewUserRequest(long localId, String server) {
        this.localId = localId;
        this.server = server;
    }

    public long getLocalId() {
        return localId;
    }

    public String getServer() {
        return server;
    }
}
