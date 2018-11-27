package messages;

public class Send {
    public enum Type {ALL, BROADCAST}
    private final long localId;
    private final String msg;
    private final Type type;
    private final String room;
    public Send(long localId, String msg, Type type, String room) {
        this.localId = localId;
        this.msg = msg;
        this.type = type;
        this.room = room;
    }

    public String getRoom(){
        return room;
    }

    public long getLocalId(){
        return localId;
    }

    public String getMsg(){
        return msg;
    }

    public boolean isAll(){
        return type == Type.ALL;
    }
}
