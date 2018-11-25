package messages.data;

import models.User;

public class SetUser {
    private final User user;
    public SetUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
