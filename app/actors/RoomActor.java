package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import data.MessageData;
import messages.*;
import data.CmdCode;
import data.NewRoomData;
import play.libs.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomActor extends AbstractActor {
    private Map<String, ActorRef> websockets;
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
        return receiveBuilder().match(JoinRoom.class, data -> {
            String userName = data.getUser().getName();
            ActorRef out = data.getUser().getOut();
            websockets.put(userName, out);
            out.tell(Json.toJson(new NewRoomData(CmdCode.joinRoomCmd, name, history)), ActorRef.noSender());
            broadcast (new MessageData(CmdCode.chatCmd, userName + " has been joined"), userName);
        }).match(Send.class, data -> {
            sendMessage(data);
        }).match(LeaveRoom.class, data -> {
            outRoom(data.getUser().getName(), " has been left");
        }).match(Logout.class, data -> {
            outRoom(data.getUser().getName(), " has been disconnected");
        }).build();
    }

    private void outRoom(String user, String notice) {
        broadcast(new MessageData(CmdCode.chatCmd, user + notice), user);
        websockets.remove(user);
        sender().tell(new RoomStatus(name, websockets.size()), ActorRef.noSender());
    }

    private void sendMessage(Send data) {
        MessageData msg = new MessageData(CmdCode.chatCmd, data.getUser().getName() + ": " + data.getMsg());
        if (data.isAll()) send(msg);
        else broadcast(msg, data.getUser().getName());
    }

    private void send(MessageData msg) {
        for (Map.Entry<String, ActorRef> entry : websockets.entrySet())
        {
            entry.getValue().tell(Json.toJson(msg), ActorRef.noSender());
        }
        history.add(msg.msg);
    }

    private void broadcast(MessageData msg, String user) {
        for (Map.Entry<String, ActorRef> entry : websockets.entrySet())
        {
            if(!entry.getKey().equals(user)) entry.getValue().tell(Json.toJson(msg), ActorRef.noSender());
        }
        history.add(msg.msg);
    }
}
