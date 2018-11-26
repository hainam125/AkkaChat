import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import messages.data.*;
import models.Room;
import models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AkkaDb extends AbstractActor {
    private final String defaultRoom = "lobby";
    private static long currentId = 1;
    private final Map<Long, User> users = new HashMap<>();
    private final Map<String, Room> rooms = new HashMap<>();
    private final Map<String, ActorSelection> selections = new HashMap<>();

    @Override
    public void preStart() throws Exception{
        super.preStart();
        rooms.put(defaultRoom, new Room(-1, defaultRoom, null, true));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(NewSelectionRequest.class, data->{
            ActorSelection dataSelection = getContext().actorSelection(data.getPath());
            selections.put(data.getUrl(), dataSelection);
        }).match(NewUserRequest.class, data ->{
            long localId = data.getLocalId();
            String server = data.getServer();
            long id = currentId++;
            User user = new User(String.valueOf(id), server, id, localId);
            users.put(id, user);
            sender().tell(new NewUserResponse(localId, id, user.getName()), ActorRef.noSender());
        }).match(GetRoomRequest.class, data -> {
            sender().tell(new GetRoomResponse(new ArrayList<Room>(rooms.values())), ActorRef.noSender());
        }).match(GetUserRequest.class, data -> {
            sender().tell(new GetUserResponse(new ArrayList<>(users.values())), ActorRef.noSender());
        }).match(CreateRoomRequest.class, data -> {
            Room room = data.getRoom();
            rooms.put(room.getName(), room);
            //sender().tell(new CreateRoomResponse(room, true), ActorRef.noSender());
            for (Map.Entry<String, ActorSelection> entry : selections.entrySet())
            {
                entry.getValue().tell(new CreateRoomResponse(room, true), ActorRef.noSender());
            }
        }).match(SendMessageRequest.class, data -> {
            for (Map.Entry<String, ActorSelection> entry : selections.entrySet())
            {
                entry.getValue().tell(data, ActorRef.noSender());
            }
        }).match(JoinRoomRequest.class, data -> {
            /*for (Map.Entry<String, ActorSelection> entry : selections.entrySet())
            {
                entry.getValue().tell(data, ActorRef.noSender());
            }*/
            sender().tell(data, ActorRef.noSender());
        }).match(LogoutRequest.class, data -> {
            /*for (Map.Entry<String, ActorSelection> entry : selections.entrySet())
            {
                entry.getValue().tell(data, ActorRef.noSender());
            }*/
            sender().tell(data, ActorRef.noSender());
        }).build();
    }
}
