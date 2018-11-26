package messages;

import models.UserRef;

public class Send {
    public enum Type {ALL, BROADCAST}
    private final UserRef userRef;
    private final String msg;
    private final Type type;
    private final String room;
    public Send(UserRef userRef, String msg, Type type, String room) {
        this.userRef = userRef;
        this.msg = msg;
        this.type = type;
        this.room = room;
    }

    public String getRoom(){
        return room;
    }

    public UserRef getUserRef(){
        return userRef;
    }

    public String getMsg(){
        return msg;
    }

    public boolean isAll(){
        return type == Type.ALL;
    }
}
