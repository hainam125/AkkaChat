package models;

public class User {
    private final String name;
    private final String server;
    private final long id;
    private final long localId;

    public User(String name, String server, long id, long localId) {
        this.name = name;
        this.server = server;
        this.id = id;
        this.localId = localId;
    }

    public String getName() {
        return name;
    }

    public String getServer() {
        return server;
    }

    public long getId() {
        return id;
    }

    public long getLocalId() {
        return localId;
    }
}
