package messages.data;

import java.io.Serializable;
import java.util.List;

public class GetHistoryResponse implements Serializable {
    private final List<String> history;
    public GetHistoryResponse(List<String> history) {
        this.history = history;
    }

    public List<String> getHistory() {
        return history;
    }
}
