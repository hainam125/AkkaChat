package messages;

import models.User;

public class JoinRoom {
    private final String room;
    private final User user;
    public JoinRoom(User user, String room) {
        this.user = user;
        this.room = room;
    }

    public String getRoom() {
        return room;
    }

    public User getUser() {
        return user;
    }
}
