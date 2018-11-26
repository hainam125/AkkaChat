package messages.data;

import java.io.Serializable;

public class CreateRoomRequest implements Serializable {
    private final String name;
    private final String server;
    private final boolean isPublic;

    public CreateRoomRequest(String name, String server, boolean isPublic) {
        this.name = name;
        this.server = server;
        this.isPublic = isPublic;
    }

    public String getName() {
        return name;
    }

    public String getServer() {
        return server;
    }

    public boolean isPublic() {
        return isPublic;
    }
}
