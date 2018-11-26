package messages;

public class PrivateChat {
    private final Long from;
    private final Long to;
    private final String name;

    public PrivateChat(String name, Long from) {
        String[] names = name.split("-");
        this.name = name;
        this.from = from;
        this.to = Long.parseLong(String.valueOf(from).equals(names[0]) ? names[1] : names[0]);
    }

    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }

    public String getName() {
        return name;
    }
}
