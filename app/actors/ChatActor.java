package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import data.CommandData;
import messages.*;
import data.CmdCode;
import data.RoomListData;
import data.User;
import play.libs.Json;
import play.libs.akka.InjectedActorSupport;

import java.util.HashMap;
import java.util.Map;

public class ChatActor extends AbstractActor  implements InjectedActorSupport {
    private final String defaultRoom = "lobby";
    private Map<String, User> websockets;
    private Map<String, ActorRef> rooms;

    @Override
    public void preStart() throws Exception{
        super.preStart();
        websockets = new HashMap<>();
        rooms = new HashMap<>();
        createNewRoom(defaultRoom);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(AddUser.class, data -> {
            websockets.put(data.getUser().getName(), data.getUser());
            String[] roomList = rooms.keySet(). stream(). toArray(String[]::new);
            data.getUser().getOut().tell(Json.toJson(new RoomListData(CmdCode.newRoomCmd, roomList)), ActorRef.noSender());
        }).match(Send.class, data -> {
            rooms.get(data.getRoom()).forward(data, getContext());
        }).match(NewRoom.class, data -> {
            String roomNewName = data.getNewRoom();
            createNewRoom(roomNewName);
            notifyRoomChange();
            data.getUser().getIn().tell(Json.toJson(new CommandData(CmdCode.joinRoomCmd, roomNewName, null)), ActorRef.noSender());
        }).match(JoinRoom.class, data -> {
            ActorRef newRoom = rooms.get(data.getNewRoom());
            ActorRef oldRoom = rooms.get(data.getOldRoom());
            if(oldRoom != null) oldRoom.tell(new LeaveRoom(data.getUser()), self());
            newRoom.forward(data, getContext());
        }).match(Logout.class, data -> {
            rooms.get(data.getRoom()).tell(data, self());
            websockets.remove(data.getUser());
        }).match(RoomStatus.class, data -> {
            int member = data.getMember();
            String roomName = data.getName();
            ActorRef room = rooms.get(roomName);
            if(member == 0 && !roomName.equals(defaultRoom)) {
                rooms.remove(roomName);
                context().stop(room);
                notifyRoomChange();
            }
        }).build();
    }

    private void createNewRoom(String roomName){
        rooms.put(roomName, getContext().actorOf(RoomActor.props(roomName), roomName));
    }

    private void notifyRoomChange(){
        String[] roomList = rooms.keySet(). stream(). toArray(String[]::new);
        for (Map.Entry<String, User> entry : websockets.entrySet())
        {
            entry.getValue().getOut().tell(Json.toJson(new RoomListData(CmdCode.newRoomCmd, roomList)), ActorRef.noSender());
        }
    }
}
