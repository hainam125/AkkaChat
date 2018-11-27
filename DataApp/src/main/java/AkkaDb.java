import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import messages.data.*;
import models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AkkaDb extends AbstractActor {
    private static long currentId = 1;
    private final Map<Long, User> users = new HashMap<>();
    private final Map<String, ActorSelection> selections = new HashMap<>();
    private List<String> history = new ArrayList<>();

    @Override
    public void preStart() throws Exception{
        super.preStart();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(NewSelectionRequest.class, data->{
            ActorSelection dataSelection = getContext().actorSelection(data.getPath());
            selections.put(data.getUrl(), dataSelection);
        }).match(GetUserRequest.class, data -> {
            sender().tell(new GetUserResponse(new ArrayList<>(users.values())), ActorRef.noSender());
        }).match(GetHistoryRequest.class, data -> {
            sender().tell(new GetHistoryResponse(history), ActorRef.noSender());
        }).match(NewUserRequest.class, data ->{
            long localId = data.getLocalId();
            String server = data.getServer();
            long id = currentId++;
            User user = new User(String.valueOf(id), server, id, localId);
            users.put(id, user);
            String message = user.getName() + " has been joined";
            for (Map.Entry<String, ActorSelection> entry : selections.entrySet())
            {
                entry.getValue().tell(new NewUserResponse(user, message), ActorRef.noSender());
            }
            history.add(message);
        }).match(SendMessageRequest.class, data -> {
            history.add(data.getMsg());
            for (Map.Entry<String, ActorSelection> entry : selections.entrySet())
            {
                entry.getValue().tell(data, ActorRef.noSender());
            }
        }).match(LogoutRequest.class, data -> {
            long userId = data.getUserId();
            String message = users.get(userId).getName() + " has been disconnected";
            history.add(message);
            users.remove(userId);
            for (Map.Entry<String, ActorSelection> entry : selections.entrySet())
            {
                entry.getValue().tell(new LogoutResponse(userId, message), ActorRef.noSender());
            }
        }).build();
    }
}
