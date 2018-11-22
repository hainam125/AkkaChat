package messages;

import data.User;

public class LeaveRoom {
    private final User user;
    public LeaveRoom(User user) {
        this.user = user;
    }
    public User getUser() {
        return user;
    }
}
