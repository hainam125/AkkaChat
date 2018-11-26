package models;

import akka.actor.ActorRef;

public class RoomRef {
    private final Room room;
    private final ActorRef actorRef;

    public RoomRef(Room room, ActorRef actorRef) {
        this.room = room;
        this.actorRef = actorRef;
    }

    public Room getRoom() {
        return room;
    }

    public ActorRef getActorRef() {
        return actorRef;
    }
}
