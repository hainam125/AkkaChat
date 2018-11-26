package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import com.fasterxml.jackson.databind.JsonNode;
import data.*;
import messages.*;
import messages.data.GetRoomRequest;
import messages.data.GetRoomResponse;
import messages.data.NewUserRequest;
import messages.data.NewUserResponse;
import models.RoomRef;
import models.UserRef;
import play.libs.Json;
import play.libs.akka.InjectedActorSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActor extends AbstractActor  implements InjectedActorSupport {
    private final String defaultRoom = "lobby";
    private Map<Long, UserRef> localRefUsers;
    private Map<Long, UserRef> globalRefUsers;
    private Map<String, RoomRef> rooms;
    private ActorSelection dataSelection;

    @Override
    public void preStart() throws Exception{
        super.preStart();
        dataSelection = getContext().actorSelection("akka.tcp://akka@127.0.0.1:3000/user/akka-db");
        localRefUsers = new HashMap<>();
        globalRefUsers = new HashMap<>();
        rooms = new HashMap<>();
        //createNewRoom(defaultRoom, true);
        dataSelection.tell(new GetRoomRequest(), self());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(AddUser.class, data -> {
            UserRef userRef = data.getUserRef();
            dataSelection.tell(new NewUserRequest(userRef.getLocalId(), userRef.getServer()), self());
            localRefUsers.put(userRef.getLocalId(), data.getUserRef());
        }).match(GetRoomResponse.class, data ->{
            for(int i = 0; i < data.getRooms().size(); i++){
                createNewRoom(data.getRooms().get(i), data.getStatus().get(i));
            }
        }).match(NewUserResponse.class, data -> {
            UserRef userRef = localRefUsers.get(data.getLocalId());
            userRef.setName(data.getName());
            userRef.setId(data.getId());
            globalRefUsers.put(userRef.getId(), userRef);
            ActorRef out = userRef.getOut();
            out.tell(Json.toJson(new UserInfo(userRef.getName())), ActorRef.noSender());
            out.tell(Json.toJson(new RoomListData(getPublicRoom())), ActorRef.noSender());
            out.tell(Json.toJson(new UserActivedata(getUserList(), true)), ActorRef.noSender());
            userRef.getIn().tell(Json.toJson(new CommandData(CmdCode.joinRoomCmd, defaultRoom, null)), ActorRef.noSender());
            notifyUserActivity(userRef, true);
        }).match(Send.class, data -> {
            rooms.get(data.getRoom()).getActorRef().forward(data, getContext());
        }).match(NewRoom.class, data -> {
            String roomNewName = data.getNewRoom();
            createNewRoom(roomNewName, true);
            notifyRoomChange();
            data.getUserRef().getIn().tell(Json.toJson(new CommandData(CmdCode.joinRoomCmd, roomNewName, null)), ActorRef.noSender());
        }).match(JoinRoom.class, data -> {
            RoomRef newRoomRef = rooms.get(data.getNewRoom());
            RoomRef oldRoomRef = rooms.get(data.getOldRoom());
            if(oldRoomRef != null) oldRoomRef.getActorRef().tell(new LeaveRoom(data.getUserRef()), self());
            newRoomRef.getActorRef().forward(data, getContext());
        }).match(Logout.class, data -> {
            rooms.get(data.getRoom()).getActorRef().tell(data, self());
            localRefUsers.remove(data.getUserRef().getLocalId());
            notifyUserActivity(data.getUserRef(), false);
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
                globalRefUsers.get(data.getTo()).getIn().tell(jsonNode, ActorRef.noSender());
            }
            globalRefUsers.get(data.getFrom()).getIn().tell(jsonNode, ActorRef.noSender());
        }).build();
    }

    private void createNewRoom(String roomName, boolean isPublic){
        rooms.put(roomName, new RoomRef(isPublic, getContext().actorOf(RoomActor.props(roomName), roomName)));
    }

    private void notifyUserActivity(UserRef userRef, boolean online){
        for (Map.Entry<Long, UserRef> entry : localRefUsers.entrySet())
        {
            if(!entry.getKey().equals(userRef.getLocalId()))  entry.getValue().getOut().tell(Json.toJson(new UserActivedata(userRef.getName(), online)), ActorRef.noSender());
        }
    }

    private List<String> getUserList() {
        List<String> userList = new ArrayList<>();
        for (Map.Entry<Long, UserRef> entry : localRefUsers.entrySet())
        {
            userList.add(entry.getValue().getName());
        }
        return userList;
    }

    private List<String> getPublicRoom(){
        List<String> roomList = new ArrayList<>();
        for (Map.Entry<String, RoomRef> entry : rooms.entrySet())
        {
            if(entry.getValue().isPublic()) roomList.add(entry.getKey());
        }
        return roomList;
    }

    private void notifyRoomChange(){
        List<String> roomList = getPublicRoom();
        for (Map.Entry<Long, UserRef> entry : localRefUsers.entrySet())
        {
            entry.getValue().getOut().tell(Json.toJson(new RoomListData(roomList)), ActorRef.noSender());
        }
    }
}
