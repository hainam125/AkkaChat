package data;

import java.util.ArrayList;
import java.util.List;

public class UserActivedata {
    public String cmd;
    public List<String> users;
    public boolean online;

    public UserActivedata(List<String> users, boolean online) {
        this.cmd = CmdCode.userActiveCmd;
        this.users = users;
        this.online = online;
    }

    public UserActivedata(String user, boolean online) {
        this.cmd = CmdCode.userActiveCmd;
        this.users = new ArrayList<String>(){{ add(user);}};
        this.online = online;
    }

    @Override
    public String toString() {
        return cmd + " - " + users.size() + " - " + online;
    }
}
