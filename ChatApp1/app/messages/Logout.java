package messages;


public class Logout {
    private final long localId;
    public Logout(long localId) {
        this.localId = localId;
    }
    public long getLocalId() {
        return localId;
    }
}
