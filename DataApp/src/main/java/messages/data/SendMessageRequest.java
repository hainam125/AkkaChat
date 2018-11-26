package messages.data;

import java.io.Serializable;

public class SendMessageRequest implements Serializable {
    private final String room;
    private final String msg;
    private final String server;
    private final Long senderId;

    public SendMessageRequest(String room, String msg, String server, Long senderId) {
        this.room = room;
        this.msg = msg;
        this.senderId = senderId;
        this.server = server;
    }

    public String getRoom() {
        return room;
    }

    public String getMsg() {
        return msg;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getServer() {
        return server;
    }
}
