package messages;

import akka.actor.ActorRef;

public class AddIn {
    private final long localId;
    public AddIn(long localId) {
        this.localId = localId;
    }

    public long getLocalId() {
        return localId;
    }
}
