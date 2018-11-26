package messages.data;

import models.Room;

import java.io.Serializable;
import java.util.List;

public class GetRoomResponse implements Serializable {
    private final List<Room> rooms;

    public GetRoomResponse(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Room> getRooms() {
        return rooms;
    }
}
