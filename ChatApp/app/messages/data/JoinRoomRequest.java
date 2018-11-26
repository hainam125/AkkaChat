package messages.data;

import java.io.Serializable;

public class JoinRoomRequest implements Serializable {
    private final String newRoom;
    private final String oldRoom;
    private final Long userId;

    public JoinRoomRequest(String newRoom, String oldRoom, Long userId) {
        this.newRoom = newRoom;
        this.oldRoom = oldRoom;
        this.userId = userId;
    }

    public String getNewRoom() {
        return newRoom;
    }

    public String getOldRoom() {
        return oldRoom;
    }

    public Long getUserId() {
        return userId;
    }
}
