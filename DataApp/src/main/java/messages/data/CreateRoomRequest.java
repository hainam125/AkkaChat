package messages.data;

import models.Room;

import java.io.Serializable;

public class CreateRoomRequest implements Serializable {
    private final Room room;

    public CreateRoomRequest(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }
}
