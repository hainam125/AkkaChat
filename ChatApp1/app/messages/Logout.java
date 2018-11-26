package messages;

import models.UserRef;

public class Logout {
    private final UserRef userRef;
    private final String room;
    public Logout(UserRef userRef, String room) {
        this.userRef = userRef;
        this.room = room;
    }
    public UserRef getUserRef() {
        return userRef;
    }

    public String getRoom() {
        return room;
    }
}
