import akka.actor.AbstractActor;
import messages.data.SetUser;
import models.User;

import java.util.HashMap;
import java.util.Map;

public class AkkaDb extends AbstractActor {
    private final Map<String, User> users = new HashMap<>();
    @Override
    public Receive createReceive() {
        return receiveBuilder().match(SetUser.class, msg ->{
            User user = msg.getUser();
        }).build();
    }
}
