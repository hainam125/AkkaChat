package messages;

public class PrivateChat {
    private final Long from;
    private final String name;

    public PrivateChat(String name, Long from) {
        this.name = name;
        this.from = from;
    }

    public Long getFrom() {
        return from;
    }

    public String getName() {
        return name;
    }
}
