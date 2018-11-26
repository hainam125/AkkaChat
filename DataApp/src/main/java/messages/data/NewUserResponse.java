package messages.data;

import java.io.Serializable;

public class NewUserResponse implements Serializable {
    private final long localId;
    private final long id;
    private final String name;
    public NewUserResponse(long localId, long id, String name) {
        this.localId = localId;
        this.id = id;
        this.name = name;
    }

    public long getLocalId() {
        return localId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
