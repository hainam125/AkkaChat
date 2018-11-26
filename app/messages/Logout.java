package messages;

import models.User;

public class Logout {
    private final User user;
    private final String room;
    public Logout(User user, String room) {
        this.user = user;
        this.room = room;
    }
    public User getUser() {
        return user;
    }

    public String getRoom() {
        return room;
    }
}
