package messages;

import models.UserRef;

public class LeaveRoom {
    private final UserRef userRef;
    public LeaveRoom(UserRef userRef) {
        this.userRef = userRef;
    }
    public UserRef getUserRef() {
        return userRef;
    }
}
