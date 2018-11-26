import akka.actor.AbstractActor;
import akka.actor.ActorRef;
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

    @Override
    public void preStart() throws Exception{
        super.preStart();
        rooms.put(defaultRoom, new Room(defaultRoom, null, true));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(NewUserRequest.class, data ->{
            long localId = data.getLocalId();
            String server = data.getServer();
            long id = currentId++;
            User user = new User(String.valueOf(id), server, id, localId);
            users.put(id, user);
            sender().tell(new NewUserResponse(localId, id, user.getName()), ActorRef.noSender());
        }).match(GetRoomRequest.class, data -> {
            List<String> roomList = new ArrayList<>();
            List<Boolean> statusList = new ArrayList<>();
            for (Map.Entry<String, Room> entry : rooms.entrySet())
            {
                roomList.add(entry.getKey());
                statusList.add(entry.getValue().isPublic());
            }
            sender().tell(new GetRoomResponse(roomList, statusList), ActorRef.noSender());
        }).match(CreateRoomRequest.class, data -> {
            //Room room = new Room
        }).build();
    }
}
