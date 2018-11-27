package messages.data;

import models.User;

import java.io.Serializable;

public class NewUserResponse implements Serializable {
    private final User user;
    private final String message;
    public NewUserResponse(User user, String message) {
        this.user = user;
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }
}
