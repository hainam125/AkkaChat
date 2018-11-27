package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import com.typesafe.config.Config;
import data.*;
import messages.*;
import messages.data.*;
import models.User;
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
    private Map<Long, UserRef> localRefUsers;
    private Map<Long, UserRef> globalRefUsers;
    private List<String> history;
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
        history = new ArrayList<>();
        String pathSelection = "akka.tcp://" + getContext().getSystem().name() + "@" + urlSelection + "/user/" + self().path().name();
        dataSelection.tell(new NewSelectionRequest(urlSelection, pathSelection), ActorRef.noSender());
        dataSelection.tell(new GetUserRequest(), self());
        dataSelection.tell(new GetHistoryRequest(), self());
    }

    public static long getCurrentId(){
        return currentId++;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(GetUserResponse.class, data -> {
            List<User> users = data.getUsers();
            for(int i = 0; i < users.size(); i++){
                UserRef userRef = new UserRef(null);
                userRef.setUser(users.get(i));
                globalRefUsers.put(users.get(i).getId(), userRef);
            }
        }).match(GetHistoryResponse.class, data -> {
            history = data.getHistory();
        }).match(LogIn.class, data->{
            UserRef userRef = localRefUsers.get(data.getLocalId());
            userRef.setIn(sender());
        }).match(AddUser.class, data -> {
            long localId = data.getLocalId();
            dataSelection.tell(new NewUserRequest(localId, urlSelection), self());
            localRefUsers.put(localId, data.getActorRef());
        }).match(NewUserResponse.class, data -> {
            User user = data.getUser();
            if(user.getServer().equals(urlSelection)){
                UserRef userRef = localRefUsers.get(user.getLocalId());
                userRef.setUser(user);
                ActorRef out = userRef.getOut();
                globalRefUsers.put(user.getId(), userRef);
                out.tell(Json.toJson(new UserInfo(user.getName())), ActorRef.noSender());
                out.tell(Json.toJson(new UserActivedata(getUserList(), true)), ActorRef.noSender());
                out.tell(Json.toJson(new NewRoomData(CmdCode.joinRoomCmd, history)), ActorRef.noSender());
            }
            else {
                UserRef userRef = new UserRef(null);
                userRef.setUser(user);
                globalRefUsers.put(user.getId(), userRef);
            }
            broadcast(new MessageData(CmdCode.chatCmd, data.getMessage()), user.getId());
            notifyUserActivity(user.getName(), user.getId(), true);
        }).match(Send.class, data -> {
            UserRef userRef = localRefUsers.get(data.getLocalId());
            String fullMessage = userRef.getUser().getName() + ": " + data.getMsg();
            dataSelection.tell(new SendMessageRequest(data.getRoom(), fullMessage, userRef.getUser().getId()), ActorRef.noSender());
        }).match(SendMessageRequest.class, data -> {
            sendMessage(data);
        }).match(Logout.class, data -> {
            UserRef userRef = localRefUsers.get(data.getLocalId());
            dataSelection.tell(new LogoutRequest(userRef.getUser().getId()), self());
        }).match(LogoutResponse.class, data -> {
            UserRef userRef = globalRefUsers.get(data.getUserId());
            User user = userRef.getUser();
            broadcast(new MessageData(CmdCode.chatCmd, data.getMessage()), user.getId());
            globalRefUsers.remove(user.getId());
            if(user.getServer().equals(urlSelection)) localRefUsers.remove(user.getLocalId());
            notifyUserActivity(user.getName(), user.getId(), false);
        }).build();
    }

    private void notifyUserActivity(String name, long globalId, boolean online){

        for (Map.Entry<Long, UserRef> entry : localRefUsers.entrySet())
        {
            if(entry.getValue().getUser().getId() != globalId) entry.getValue().getOut().tell(Json.toJson(new UserActivedata(name, online)), ActorRef.noSender());
        }
    }

    private List<String> getUserList() {
        List<String> userList = new ArrayList<>();
        for (Map.Entry<Long, UserRef> entry : globalRefUsers.entrySet())
        {
            userList.add(entry.getValue().getUser().getName());
        }
        return userList;
    }

    private void sendMessage(SendMessageRequest data) {
        MessageData msg = new MessageData(CmdCode.chatCmd, data.getMsg());
        send(msg);
    }

    private void send(MessageData msg) {
        for (Map.Entry<Long, UserRef> entry : localRefUsers.entrySet())
        {
            entry.getValue().getOut().tell(Json.toJson(msg), ActorRef.noSender());
        }
        history.add(msg.msg);
    }

    private void broadcast(MessageData msg, long globalId) {
        for (Map.Entry<Long, UserRef> entry : localRefUsers.entrySet())
        {
            if(entry.getValue().getUser().getId() != globalId) entry.getValue().getOut().tell(Json.toJson(msg), ActorRef.noSender());
        }
        history.add(msg.msg);
    }
}
