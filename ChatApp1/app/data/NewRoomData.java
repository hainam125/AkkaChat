package data;

import java.util.List;

public class NewRoomData {
    public String cmd;
    public List<String> history;

    public NewRoomData(String cmd, List<String> history) {
        this.cmd = cmd;
        this.history = history;
    }

    @Override
    public String toString() {
        return cmd + " - " + history;
    }
}