package messages;

public class JoinRoom {
    private final String newRoom;
    private final String oldRoom;
    private final long localId;
    public JoinRoom(long localId, String newRoom, String oldRoom) {
        this.localId = localId;
        this.newRoom = newRoom;
        this.oldRoom = oldRoom;
    }

    public String getOldRoom() {
        return oldRoom;
    }

    public String getNewRoom() {
        return newRoom;
    }

    public long getLocalId() {
        return localId;
    }
}
