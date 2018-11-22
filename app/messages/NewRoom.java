package messages;

import data.User;

public class NewRoom {
    private final String newRoom;
    private final String oldRoom;
    private final User user;
    public NewRoom(User user, String newRoom, String oldRoom) {
        this.user = user;
        this.newRoom = newRoom;
        this.oldRoom = oldRoom;
    }

    public String getOldRoom() {
        return oldRoom;
    }

    public String getNewRoom() {
        return newRoom;
    }

    public User getUser() {
        return user;
    }
}
