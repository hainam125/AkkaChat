package models;

import akka.actor.ActorRef;

public class RoomRef {
    private final boolean isPublic;
    private final ActorRef actorRef;

    public RoomRef(boolean isPublic, ActorRef actorRef) {
        this.isPublic = isPublic;
        this.actorRef = actorRef;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public ActorRef getActorRef() {
        return actorRef;
    }
}
