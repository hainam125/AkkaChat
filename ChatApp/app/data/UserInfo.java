package data;

public class UserInfo {
    public String cmd;
    public String username;

    public UserInfo(String username) {
        this.cmd = CmdCode.userInfoCmd;
        this.username = username;
    }
}
