package data;

public class RoomListData {
    public String cmd;
    public String[] rooms;

    public RoomListData(String cmd, String[] rooms) {
        this.cmd = cmd;
        this.rooms = rooms;
    }

    @Override
    public String toString() {
        return cmd + " - " + rooms.length;
    }
}
