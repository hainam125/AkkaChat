package messages.data;

import models.Room;

import java.io.Serializable;

public class CreateRoomResponse implements Serializable {
    private final boolean success;
    private final Room room;

    public CreateRoomResponse(Room room, boolean success) {
        this.room = room;
        this.success = success;
    }

    public Room getRoom() {
        return room;
    }

    public boolean isSuccess() {
        return success;
    }
}
