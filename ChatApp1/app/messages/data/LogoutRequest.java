package messages.data;

import java.io.Serializable;

public class LogoutRequest implements Serializable {
    private final Long userId;
    public LogoutRequest(Long userId) {
        this.userId = userId;
    }
    public Long getUserId() {
        return userId;
    }
}
