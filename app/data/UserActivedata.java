package data;

public class UserActivedata {
    public String cmd;
    public String[] users;
    public boolean online;

    public UserActivedata(String[] users, boolean online) {
        this.cmd = CmdCode.userActiveCmd;
        this.users = users;
        this.online = online;
    }

    public UserActivedata(String user, boolean online) {
        this.cmd = CmdCode.userActiveCmd;
        this.users = new String[]{user};
        this.online = online;
    }

    @Override
    public String toString() {
        return cmd + " - " + users.length + " - " + online;
    }
}
