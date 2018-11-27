package messages;

public class LogIn {
    private final long localId;
    public LogIn(long localId) {
        this.localId = localId;
    }

    public long getLocalId() {
        return localId;
    }
}
