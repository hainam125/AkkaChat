package messages;

import models.UserRef;

public class NewRoom {
    private final String newRoom;
    private final String oldRoom;
    private final UserRef userRef;
    public NewRoom(UserRef userRef, String newRoom, String oldRoom) {
        this.userRef = userRef;
        this.newRoom = newRoom;
        this.oldRoom = oldRoom;
    }

    public String getOldRoom() {
        return oldRoom;
    }

    public String getNewRoom() {
        return newRoom;
    }

    public UserRef getUserRef() {
        return userRef;
    }
}
