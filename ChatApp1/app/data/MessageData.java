package data;

public class MessageData {
    public String cmd;
    public String msg;

    public MessageData(String cmd, String msg) {
        this.cmd = cmd;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return cmd + " - " + msg;
    }
}
