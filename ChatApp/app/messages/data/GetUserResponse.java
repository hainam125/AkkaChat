package messages.data;

import models.Room;
import models.User;

import java.io.Serializable;
import java.util.List;

public class GetUserResponse implements Serializable {
    private final List<User> users;

    public GetUserResponse(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }
}

