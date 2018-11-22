package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import messages.*;
import models.User;

import java.util.HashMap;
import java.util.Map;

public class RoomActor extends AbstractActor {
    private Map<String, ActorRef > websockets;

    @Override
    public void preStart() throws Exception{
        super.preStart();
        websockets = new HashMap<>();
    }
    @Override
    public Receive createReceive() {
        return receiveBuilder().match(JoinRoom.class, data -> {
            String userName = data.getUser().getName();
            websockets.put(userName, data.getUser().getOut());
            broadcast (userName + " has been connected", userName);
        }).match(Send.class, data -> {
            sendMessage(data);
        }).match(Logout.class, data -> {
            String user = data.getUser().getName();
            broadcast(user + " has been disconnected", user);
            websockets.remove(user);
        }).build();
    }

    private void sendMessage(Send data) {
        if (data.isAll()) send(data.fullMessage());
        else broadcast(data.fullMessage(), data.getUser().getName());
    }

    private void send(String msg) {
        for (Map.Entry<String, ActorRef> entry : websockets.entrySet())
        {
            entry.getValue().tell(msg, ActorRef.noSender());
        }
    }

    private void broadcast(String msg, String user) {
        for (Map.Entry<String, ActorRef> entry : websockets.entrySet())
        {
            if(!entry.getKey().equals(user)) entry.getValue().tell(msg, ActorRef.noSender());
        }
    }
}
