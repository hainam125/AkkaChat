package messages;

import data.User;

public class Send {
    private final User user;
    private final String msg;
    private final MsgType type;
    private final String room;
    public Send(User user, String msg, MsgType type, String room) {
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
        return type == MsgType.ALL;
    }
}
