package messages.data;

import java.io.Serializable;

public class NewUserRequest implements Serializable {
    private final Long localId;
    private final String server;
    public NewUserRequest(Long localId, String server) {
        this.localId = localId;
        this.server = server;
    }

    public Long getLocalId() {
        return localId;
    }

    public String getServer() {
        return server;
    }
}
