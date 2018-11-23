package data;

import akka.actor.ActorRef;

public class Room {
    private final boolean isPublic;
    private final ActorRef actorRef;

    public Room(boolean isPublic, ActorRef actorRef) {
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
