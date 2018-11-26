package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import data.CommandData;
import messages.PrivateChat;
import messages.*;
import data.CmdCode;
import models.UserRef;
import play.libs.Json;

public class WebSocketActor extends AbstractActor {
    private final ActorRef chatActor;
    private final UserRef userRef;
    private String room;

    public static Props props(UserRef userRef, ActorRef lobbyActor) {
        return Props.create(WebSocketActor.class, userRef, lobbyActor);
    }

    private WebSocketActor(UserRef userRef, ActorRef chatActor) {
        this.userRef = userRef;
        this.chatActor = chatActor;
    }

    @Override
    public void preStart() throws Exception{
        super.preStart();
        userRef.setIn(self());
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
        System.out.println(userRef.getName() +  " Logout...");
        chatActor.tell(new Logout(userRef, room), ActorRef.noSender());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(JsonNode.class, msg -> {
            CommandData command = Json.fromJson(msg, CommandData.class);
            String cmd = command.cmd;
            String roomName = command.room;
            if(cmd.equals(CmdCode.chatCmd)) {
                String message = command.msg;
                chatActor.tell(new Send(userRef, message, Send.Type.ALL, roomName), ActorRef.noSender());
            }
            else if(cmd.equals(CmdCode.newRoomCmd)) {
                chatActor.tell(new NewRoom(userRef, roomName, room), ActorRef.noSender());
            }
            else if(cmd.equals(CmdCode.joinRoomCmd)) {
                chatActor.tell(new JoinRoom(userRef, roomName, room), ActorRef.noSender());
                room = roomName;
            }
            else if(cmd.equals(CmdCode.privateChatCmd)) {
                chatActor.tell(new PrivateChat(roomName, userRef.getId()), ActorRef.noSender());
            }
        }).build();
    }
}