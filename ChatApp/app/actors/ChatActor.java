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
    private static long currentId = 1;
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
        dataSelection.tell(new GetUserRequest(), self());
    }

    public static long getCurrentId(){
        return currentId++;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(GetRoomResponse.class, data ->{
            for(int i = 0; i < data.getRooms().size(); i++){
                createNewLocalRoom(data.getRooms().get(i));
            }
        }).match(AddUser.class, data -> {
            long localId = data.getLocalId();
            dataSelection.tell(new NewUserRequest(localId, urlSelection), self());
            localRefUsers.put(localId, data.getActorRef());
        }).match(AddIn.class, data->{
            UserRef userRef = localRefUsers.get(data.getLocalId());
            userRef.setIn(sender());
        }).match(NewUserResponse.class, data -> {
            UserRef userRef = localRefUsers.get(data.getLocalId());
            userRef.setName(data.getName());
            userRef.setId(data.getId());
            globalRefUsers.put(userRef.getId(), userRef);
            ActorRef out = userRef.getOut();
            ActorRef in = userRef.getIn();
            out.tell(Json.toJson(new UserInfo(userRef.getName())), ActorRef.noSender());
            out.tell(Json.toJson(new RoomListData(getPublicRoom())), ActorRef.noSender());
            out.tell(Json.toJson(new UserActivedata(getUserList(), true)), ActorRef.noSender());
            in.tell(Json.toJson(new CommandData(CmdCode.joinRoomCmd, defaultRoom, null)), ActorRef.noSender());
            notifyUserActivity(userRef, true);
        }).match(Send.class, data -> {
            UserRef userRef = localRefUsers.get(data.getLocalId());
            dataSelection.tell(new SendMessageRequest(data.getRoom(), data.getMsg(), urlSelection, userRef.getId()), ActorRef.noSender());
        }).match(SendMessageRequest.class, data -> {
            rooms.get(data.getRoom()).getActorRef().forward(new Send(globalRefUsers.get(data.getSenderId()).getLocalId(), data.getMsg(), Send.Type.ALL, data.getRoom()), getContext());
        }).match(NewRoom.class, data -> {
            UserRef userRef = localRefUsers.get(data.getLocalId());
            String roomNewName = data.getNewRoom();
            Room room = new Room(userRef.getId(), roomNewName, urlSelection, true);
            dataSelection.tell(new CreateRoomRequest(room), self());
        }).match(CreateRoomResponse.class, data -> {
            Room room = data.getRoom();
            createNewLocalRoom(room);
            notifyRoomChange();
            UserRef owner = globalRefUsers.get(room.getOwnerId());
            if(owner != null) owner.getIn().tell(Json.toJson(new CommandData(CmdCode.joinRoomCmd, room.getName(), null)), ActorRef.noSender());
        }).match(JoinRoom.class, data -> {
            UserRef userRef = localRefUsers.get(data.getLocalId());
            dataSelection.tell(new JoinRoomRequest(data.getNewRoom(), data.getOldRoom(), userRef.getId()), self());
        }).match(JoinRoomRequest.class, data -> {
            RoomRef newRoomRef = rooms.get(data.getNewRoom());
            RoomRef oldRoomRef = rooms.get(data.getOldRoom());
            UserRef userRef = globalRefUsers.get(data.getUserId());
            if(oldRoomRef != null) oldRoomRef.getActorRef().tell(new LeaveRoom(userRef), self());
            newRoomRef.getActorRef().forward(new EnterRoom(userRef), getContext());
        }).match(Logout.class, data -> {
            UserRef userRef = localRefUsers.get(data.getLocalId());
            dataSelection.tell(new LogoutRequest(userRef.getId(), data.getRoom()), ActorRef.noSender());
        }).match(LogoutRequest.class, data -> {
            UserRef userRef = globalRefUsers.get(data.getUserId());
            rooms.get(data.getRoom()).getActorRef().tell(new Logout(userRef.getLocalId(), data.getRoom()), self());
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
            UserRef userRef = localRefUsers.get(data.getFrom());
            long globalFrom = userRef.getId();
            String roomNewName = data.getName();
            String[] names = roomNewName.split("-");

            long globalTo = Long.parseLong(String.valueOf(globalFrom).equals(names[0]) ? names[1] : names[0]);

            JsonNode jsonNode = Json.toJson(new CommandData(CmdCode.joinRoomCmd, roomNewName, null));
            if(rooms.get(roomNewName) == null) {
                Room room = new Room(data.getFrom(), roomNewName, urlSelection, false);
                createNewLocalRoom(room);
                globalRefUsers.get(globalTo).getIn().tell(jsonNode, ActorRef.noSender());
            }
            userRef.getIn().tell(jsonNode, ActorRef.noSender());
        }).build();
    }

    private void createNewLocalRoom(Room room){
        rooms.put(room.getName(), new RoomRef(room, getContext().actorOf(RoomActor.props(room.getName()), room.getName())));
    }

    private void notifyUserActivity(UserRef userRef, boolean online){
        for (Map.Entry<Long, UserRef> entry : localRefUsers.entrySet())
        {
            System.out.println(entry.getKey() + " - " + userRef.getLocalId());
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
