package messages.data;

import java.io.Serializable;

public class LogoutRequest implements Serializable {
    private final Long userId;
    private final String room;
    public LogoutRequest(Long userId, String room) {
        this.userId = userId;
        this.room = room;
    }
    public Long getUserId() {
        return userId;
    }

    public String getRoom() {
        return room;
    }
}
