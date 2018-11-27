package messages;

import models.UserRef;

public class AddUser {
    private final long localId;
    private final UserRef actorRef;
    public AddUser(long localId, UserRef actorRef) {
        this.localId = localId;
        this.actorRef = actorRef;
    }

    public long getLocalId() {
        return localId;
    }

    public UserRef getActorRef() {
        return actorRef;
    }
}
