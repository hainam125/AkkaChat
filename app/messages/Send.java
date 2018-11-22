package messages;

import data.User;

public class Send {
    public enum Type {ALL, BROADCAST}
    private final User user;
    private final String msg;
    private final Type type;
    private final String room;
    public Send(User user, String msg, Type type, String room) {
        this.user = user;
        this.msg = msg;
        this.type = type;
        this.room = room;
    }

    public String getRoom(){
        return room;
    }

    public User getUser(){
        return user;
    }

    public String getMsg(){
        return msg;
    }

    public boolean isAll(){
        return type == Type.ALL;
    }
}
