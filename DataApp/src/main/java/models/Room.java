package models;


import java.io.Serializable;

public class Room implements Serializable {
    private final String name;
    private final boolean isPublic;
    private final String server;

    public Room(String name, String server, boolean isPublic) {
        this.isPublic = isPublic;
        this.server = server;
        this.name = name;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public String getName() {
        return name;
    }

    public String getServer() {
        return server;
    }
}
