package messages;

import models.UserRef;

public class EnterRoom {
    private final UserRef userRef;
    public EnterRoom(UserRef userRef) {
        this.userRef = userRef;
    }

    public UserRef getUserRef() {
        return userRef;
    }
}
