package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import data.CommandData;
import messages.PrivateChat;
import messages.*;
import data.CmdCode;
import models.User;
import play.Logger;
import play.libs.Json;

public class WebSocketActor extends AbstractActor {
    private final ActorRef chatActor;
    private final User user;
    private String room;

    public static Props props(User user, ActorRef lobbyActor) {
        return Props.create(WebSocketActor.class,user, lobbyActor);
    }

    private WebSocketActor(User user, ActorRef chatActor) {
        this.user = user;
        this.chatActor = chatActor;
    }

    @Override
    public void preStart() throws Exception{
        super.preStart();
        user.setIn(self());
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
        System.out.println(user.getName() +  " Logout...");
        chatActor.tell(new Logout(user, room), ActorRef.noSender());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(JsonNode.class, msg -> {
            Logger.debug(msg.toString());
            CommandData command = Json.fromJson(msg, CommandData.class);
            String cmd = command.cmd;
            String roomName = command.room;
            if(cmd.equals(CmdCode.chatCmd)) {
                String message = command.msg;
                chatActor.tell(new Send(user, message, MsgType.ALL, roomName), ActorRef.noSender());
            }
            else if(cmd.equals(CmdCode.newRoomCmd)) {
                chatActor.tell(new NewRoom(user, roomName, room), ActorRef.noSender());
            }
            else if(cmd.equals(CmdCode.joinRoomCmd)) {
                chatActor.tell(new JoinRoom(user, roomName, room), ActorRef.noSender());
                room = roomName;
            }
            else if(cmd.equals(CmdCode.privateChatCmd)) {
                chatActor.tell(new PrivateChat(roomName, user.getName()), ActorRef.noSender());
            }
        }).build();
    }
}