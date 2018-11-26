package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.JsonNode;
import data.*;
import messages.*;
import models.Room;
import models.User;
import play.libs.Json;
import play.libs.akka.InjectedActorSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActor extends AbstractActor  implements InjectedActorSupport {
    private final String defaultRoom = "lobby";
    private Map<String, User> websockets;
    private Map<String, Room> rooms;

    @Override
    public void preStart() throws Exception{
        super.preStart();
        websockets = new HashMap<>();
        rooms = new HashMap<>();
        createNewRoom(defaultRoom, true);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(AddUser.class, data -> {
            notifyUserActivity(data.getUser(), true);
            websockets.put(data.getUser().getName(), data.getUser());
            List<String> roomList = getPublicRoom();
            String[] userList = websockets.keySet().stream().toArray(String[]::new);
            ActorRef out = data.getUser().getOut();
            out.tell(Json.toJson(new UserInfo(data.getUser().getName())), ActorRef.noSender());
            out.tell(Json.toJson(new RoomListData(roomList)), ActorRef.noSender());
            out.tell(Json.toJson(new UserActivedata(userList, true)), ActorRef.noSender());
        }).match(Send.class, data -> {
            rooms.get(data.getRoom()).getActorRef().forward(data, getContext());
        }).match(NewRoom.class, data -> {
            String roomNewName = data.getNewRoom();
            createNewRoom(roomNewName, true);
            notifyRoomChange();
            data.getUser().getIn().tell(Json.toJson(new CommandData(CmdCode.joinRoomCmd, roomNewName, null)), ActorRef.noSender());
        }).match(JoinRoom.class, data -> {
            Room newRoom = rooms.get(data.getNewRoom());
            Room oldRoom = rooms.get(data.getOldRoom());
            if(oldRoom != null) oldRoom.getActorRef().tell(new LeaveRoom(data.getUser()), self());
            newRoom.getActorRef().forward(data, getContext());
        }).match(Logout.class, data -> {
            String room = data.getRoom();
            if(room != null) rooms.get(room).getActorRef().tell(data, self());
            websockets.remove(data.getUser().getName());
            notifyUserActivity(data.getUser(), false);
        }).match(RoomStatus.class, data -> {
            int member = data.getMember();
            String roomName = data.getName();
            ActorRef room = rooms.get(roomName).getActorRef();
            if(member == 0 && !roomName.equals(defaultRoom)) {
                rooms.remove(roomName);
                context().stop(room);
                notifyRoomChange();
            }
        }).match(PrivateChat.class, data -> {
            String roomNewName = data.getName();
            JsonNode jsonNode = Json.toJson(new CommandData(CmdCode.joinRoomCmd, roomNewName, null));
            if(rooms.get(roomNewName) == null) {
                createNewRoom(roomNewName, false);
                websockets.get(data.getTo()).getIn().tell(jsonNode, ActorRef.noSender());
            }
            websockets.get(data.getFrom()).getIn().tell(jsonNode, ActorRef.noSender());
        }).build();
    }

    private void createNewRoom(String roomName, boolean isPublic){
        rooms.put(roomName, new Room(isPublic, getContext().actorOf(RoomActor.props(roomName), roomName)));
    }

    private void notifyUserActivity(User user, boolean online){
        for (Map.Entry<String, User> entry : websockets.entrySet())
        {
            entry.getValue().getOut().tell(Json.toJson(new UserActivedata(user.getName(), online)), ActorRef.noSender());
        }
    }

    private List<String> getPublicRoom(){
        List<String> roomList = new ArrayList<>();
        for (Map.Entry<String, Room> entry : rooms.entrySet())
        {
            if(entry.getValue().isPublic()) roomList.add(entry.getKey());
        }
        return roomList;
    }

    private void notifyRoomChange(){
        List<String> roomList = getPublicRoom();
        for (Map.Entry<String, User> entry : websockets.entrySet())
        {
            entry.getValue().getOut().tell(Json.toJson(new RoomListData(roomList)), ActorRef.noSender());
        }
    }
}
