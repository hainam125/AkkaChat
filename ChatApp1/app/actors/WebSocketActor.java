package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import data.CommandData;
import messages.*;
import data.CmdCode;
import play.libs.Json;

public class WebSocketActor extends AbstractActor {
    private final ActorRef chatActor;
    private final long localId;

    public static Props props(long localId, ActorRef lobbyActor) {
        return Props.create(WebSocketActor.class, localId, lobbyActor);
    }

    private WebSocketActor(long localId, ActorRef chatActor) {
        this.localId = localId;
        this.chatActor = chatActor;
    }

    @Override
    public void preStart() throws Exception{
        super.preStart();
        chatActor.tell(new LogIn(localId), self());
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
        System.out.println(localId +  " Logout...");
        chatActor.tell(new Logout(localId), ActorRef.noSender());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(JsonNode.class, msg -> {
            CommandData command = Json.fromJson(msg, CommandData.class);
            String cmd = command.cmd;
            String roomName = command.room;
            if(cmd.equals(CmdCode.chatCmd)) {
                String message = command.msg;
                chatActor.tell(new Send(localId, message, Send.Type.ALL, roomName), ActorRef.noSender());
            }
        }).build();
    }
}