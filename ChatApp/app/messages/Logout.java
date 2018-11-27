package messages;


public class Logout {
    private final long localId;
    private final String room;
    public Logout(long localId, String room) {
        this.localId = localId;
        this.room = room;
    }
    public long getLocalId() {
        return localId;
    }

    public String getRoom() {
        return room;
    }
}
