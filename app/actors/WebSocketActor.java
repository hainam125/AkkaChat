package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import messages.*;
import models.User;
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
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
        chatActor.tell(new Logout(user, room), ActorRef.noSender());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(String.class, msg -> {
            String[] data = msg.split("-");
            String cmd = data[0];
            if(cmd.equals(chatCmd)) {
                String message = data[2];
                String currentRoom = data[1];
                chatActor.tell(new Send(user, message, Send.Type.ALL, currentRoom), ActorRef.noSender());
            }
            else if(cmd.equals(newRoomCmd)) {
                chatActor.tell(new NewRoom(data[1]), ActorRef.noSender());
            }
            else if(cmd.equals(joinRoomCmd)) {
                room = data[1];
                chatActor.tell(new JoinRoom(user, room), ActorRef.noSender());
            }

        }).build();
    }
    private final String newRoomCmd  = "CmdCreateRoom";
    private final String joinRoomCmd = "CmdJoinRoom";
    private final String chatCmd     = "CmdChat";
}
