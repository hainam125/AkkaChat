package messages;

import models.UserRef;

public class AddUser {
    private final UserRef userRef;
    public AddUser(UserRef userRef) {
        this.userRef = userRef;
    }

    public UserRef getUserRef() {
        return userRef;
    }
}
