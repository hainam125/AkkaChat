package messages.data;

import java.io.Serializable;
import java.util.List;

public class GetRoomResponse implements Serializable {
    private final List<String> rooms;
    private final List<Boolean> status;

    public GetRoomResponse(List<String> rooms, List<Boolean> status) {
        this.rooms = rooms;
        this.status = status;
    }

    public List<String> getRooms() {
        return rooms;
    }

    public List<Boolean> getStatus() {
        return status;
    }
}
