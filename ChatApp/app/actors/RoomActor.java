package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import data.MessageData;
import messages.*;
import data.CmdCode;
import data.NewRoomData;
import models.User;
import models.UserRef;
import play.libs.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomActor extends AbstractActor {
    private Map<Long, UserRef> websockets;
    private List<String> history;
    private String name;

    public static Props props(String name) {
        return Props.create(RoomActor.class, () -> new RoomActor(name));
    }

    public RoomActor(String name) {
        this.name = name;
    }

    @Override
    public void preStart() throws Exception{
        super.preStart();
        websockets = new HashMap<>();
        history = new ArrayList<>();
    }
    @Override
    public Receive createReceive() {
        return receiveBuilder().match(EnterRoom.class, data -> {
            UserRef userRef = data.getUserRef();
            String userName = userRef.getName();
            long localId = userRef.getLocalId();
            ActorRef out = userRef.getOut();
            websockets.put(localId, userRef);
            out.tell(Json.toJson(new NewRoomData(CmdCode.joinRoomCmd, name, history)), ActorRef.noSender());
            broadcast (new MessageData(CmdCode.chatCmd, userName + " has been joined"), userName);
        }).match(Send.class, data -> {
            sendMessage(data);
        }).match(LeaveRoom.class, data -> {
            outRoom(data.getUserRef().getName(), " has been left");
        }).match(Logout.class, data -> {
            UserRef userRef = websockets.get(data.getLocalId());
            outRoom(userRef.getName(), " has been disconnected");
        }).build();
    }

    private void outRoom(String user, String notice) {
        broadcast(new MessageData(CmdCode.chatCmd, user + notice), user);
        websockets.remove(user);
        sender().tell(new RoomStatus(name, websockets.size()), ActorRef.noSender());
    }

    private void sendMessage(Send data) {
        UserRef userRef = websockets.get(data.getLocalId());
        MessageData msg = new MessageData(CmdCode.chatCmd, userRef.getName() + ": " + data.getMsg());
        if (data.isAll()) send(msg);
        else broadcast(msg, userRef.getName());
    }

    private void send(MessageData msg) {
        for (Map.Entry<Long, UserRef> entry : websockets.entrySet())
        {
            entry.getValue().getOut().tell(Json.toJson(msg), ActorRef.noSender());
        }
        history.add(msg.msg);
    }

    private void broadcast(MessageData msg, String user) {
        for (Map.Entry<Long, UserRef> entry : websockets.entrySet())
        {
            if(!entry.getValue().getName().equals(user)) entry.getValue().getOut().tell(Json.toJson(msg), ActorRef.noSender());
        }
        history.add(msg.msg);
    }
}
