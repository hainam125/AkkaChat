package messages;

import models.User;

public class AddUser {
    private final User user;
    public AddUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
