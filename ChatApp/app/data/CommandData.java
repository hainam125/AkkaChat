package data;

public class CommandData {
    public String cmd;
    public String room;
    public String msg;

    public CommandData() {

    }

    public CommandData(String cmd, String room, String msg) {
        this.cmd = cmd;
        this.msg = msg;
        this.room = room;
    }

    @Override
    public String toString() {
        return cmd + " - " + room + " - " + msg;
    }
}
