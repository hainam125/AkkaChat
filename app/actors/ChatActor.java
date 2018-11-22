package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import messages.*;
import models.User;

import java.util.HashMap;
import java.util.Map;

public class ChatActor extends AbstractActor {
    private final String defaultRoom = "lobby";
    private Map<String, User> websockets;
    private Map<String, ActorRef> rooms;

    @Override
    public void preStart() throws Exception{
        super.preStart();
        websockets = new HashMap<>();
        rooms = new HashMap<>();
        rooms.put(defaultRoom, getContext().actorOf(Props.create(RoomActor.class), defaultRoom));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(AddUser.class, data -> {
            websockets.put(data.getUser().getName(), data.getUser());
        }).match(Send.class, data -> {
            rooms.get(data.getRoom()).forward(data, getContext());
        }).match(NewRoom.class, data -> {
            String roomName = data.getName();
            rooms.put(roomName, getContext().actorOf(Props.create(RoomActor.class), roomName));
            notifyNewRoom(roomName);
        }).match(JoinRoom.class, data -> {
            ActorRef room = rooms.get(data.getRoom());
            room.forward(data, getContext());
        }).match(Logout.class, data -> {
            rooms.get(data.getRoom()).forward(data, getContext());
            websockets.remove(data.getUser());
        }).build();
    }

    private void notifyNewRoom(String room){
        for (Map.Entry<String, User> entry : websockets.entrySet())
        {
            entry.getValue().getOut().tell(room, ActorRef.noSender());
        }
    }
}
