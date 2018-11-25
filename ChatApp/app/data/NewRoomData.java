package data;

import java.util.List;

public class NewRoomData {
    public String cmd;
    public String room;
    public List<String> history;

    public NewRoomData(String cmd, String room, List<String> history) {
        this.cmd = cmd;
        this.history = history;
        this.room = room;
    }

    @Override
    public String toString() {
        return cmd + " - " + room + " - " + history;
    }
}
