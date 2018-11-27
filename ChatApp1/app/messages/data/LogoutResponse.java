package messages.data;

import java.io.Serializable;

public class LogoutResponse implements Serializable {
    private final Long userId;
    private final String message;
    public LogoutResponse(Long userId, String message) {
        this.userId = userId;
        this.message = message;
    }
    public Long getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }
}