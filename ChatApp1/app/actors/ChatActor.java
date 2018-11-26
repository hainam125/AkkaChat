package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import data.*;
import messages.*;
import messages.data.*;
import models.Room;
import models.RoomRef;
import models.UserRef;
import play.libs.Json;
import play.libs.akka.InjectedActorSupport;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActor extends AbstractActor  implements InjectedActorSupport {
    private final String urlSelection;
    private final String defaultRoom = "lobby";
    private Map<Long, UserRef> localRefUsers;
    private Map<Long, UserRef> globalRefUsers;
    private Map<String, RoomRef> rooms;
    private ActorSelection dataSelection;

    @Inject
    public ChatActor(Config config) {
        urlSelection = config.getString("default.selectionUrl");
    }
    @Override
    public void preStart() throws Exception{
        super.preStart();
        dataSelection = getContext().actorSelection("akka.tcp://akka@127.0.0.1:3000/user/akka-db");
        localRefUsers = new HashMap<>();
        globalRefUsers = new HashMap<>();
        rooms = new HashMap<>();
        String pathSelection = "akka.tcp://" + getContext().getSystem().name() + "@" + urlSelection + "/user/" + self().path().name();
        dataSelection.tell(new NewSelectionRequest(urlSelection, pathSelection), ActorRef.noSender());
        dataSelection.tell(new GetRoomRequest(), self());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(GetRoomResponse.class, data ->{
            for(int i = 0; i < data.getRooms().size(); i++){
                createNewLocalRoom(data.getRooms().get(i));
            }
        }).match(AddUser.class, data -> {
            UserRef userRef = data.getUserRef();
            dataSelection.tell(new NewUserRequest(userRef.getLocalId(), urlSelection), self());
            localRefUsers.put(userRef.getLocalId(), data.getUserRef());
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
            //rooms.get(data.getRoom()).getActorRef().forward(data, getContext());
            dataSelection.tell(new SendMessageRequest(data.getRoom(), data.getMsg(), urlSelection, data.getUserRef().getId()), ActorRef.noSender());
        }).match(SendMessageRequest.class, data -> {
            System.out.println(data.getMsg() + " --------msg");
            rooms.get(data.getRoom()).getActorRef().forward(new Send(globalRefUsers.get(data.getSenderId()), data.getMsg(), Send.Type.ALL, data.getRoom()), getContext());
        }).match(NewRoom.class, data -> {
            String roomNewName = data.getNewRoom();
            Room room = new Room(data.getUserRef().getId(), roomNewName, urlSelection, true);
            dataSelection.tell(new CreateRoomRequest(room), self());
        }).match(CreateRoomResponse.class, data -> {
            Room room = data.getRoom();
            createNewLocalRoom(room);
            notifyRoomChange();
            globalRefUsers.get(room.getOwnerId()).getIn().tell(Json.toJson(new CommandData(CmdCode.joinRoomCmd, room.getName(), null)), ActorRef.noSender());
        }).match(JoinRoom.class, data -> {
            dataSelection.tell(new JoinRoomRequest(data.getNewRoom(), data.getOldRoom(), data.getUserRef().getId()), ActorRef.noSender());
        }).match(JoinRoomRequest.class, data -> {
            RoomRef newRoomRef = rooms.get(data.getNewRoom());
            RoomRef oldRoomRef = rooms.get(data.getOldRoom());
            UserRef userRef = globalRefUsers.get(data.getUserId());
            if(oldRoomRef != null) oldRoomRef.getActorRef().tell(new LeaveRoom(userRef), self());
            newRoomRef.getActorRef().forward(new JoinRoom(userRef, data.getNewRoom(), data.getOldRoom()), getContext());
        }).match(Logout.class, data -> {
            dataSelection.tell(new LogoutRequest(data.getUserRef().getId(), data.getRoom()), ActorRef.noSender());
        }).match(LogoutRequest.class, data -> {
            UserRef userRef = globalRefUsers.get(data.getUserId());
            rooms.get(data.getRoom()).getActorRef().tell(new Logout(userRef, data.getRoom()), self());
            localRefUsers.remove(userRef.getLocalId());
            notifyUserActivity(userRef, false);
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
                Room room = new Room(data.getFrom(), roomNewName, "127.0.0.1:3001", false);
                createNewLocalRoom(room);
                globalRefUsers.get(data.getTo()).getIn().tell(jsonNode, ActorRef.noSender());
            }
            globalRefUsers.get(data.getFrom()).getIn().tell(jsonNode, ActorRef.noSender());
        }).build();
    }

    private void createNewLocalRoom(Room room){
        rooms.put(room.getName(), new RoomRef(room, getContext().actorOf(RoomActor.props(room.getName()), room.getName())));
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
            if(entry.getValue().getRoom().isPublic()) roomList.add(entry.getKey());
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
