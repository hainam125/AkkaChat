package messages.data;

import java.io.Serializable;

public class NewSelectionRequest implements Serializable {
    private final String url;
    private final String path;

    public NewSelectionRequest(String url, String path) {
        this.url = url;
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public String getPath() {
        return path;
    }
}