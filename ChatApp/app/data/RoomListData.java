package data;

import java.util.List;

public class RoomListData {
    public String cmd;
    public List<String> rooms;

    public RoomListData(List<String> rooms) {
        this.cmd = CmdCode.newRoomCmd;
        this.rooms = rooms;
    }

    @Override
    public String toString() {
        return cmd + " - " + rooms.size();
    }
}
