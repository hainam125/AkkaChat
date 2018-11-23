package messages;

public class PrivateChat {
    private final String from;
    private final String to;
    private final String name;

    public PrivateChat(String name, String from) {
        String[] names = name.split("-");
        this.name = name;
        this.from = from;
        this.to = from.equals(names[0]) ? names[1] : names[0];
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getName() {
        return name;
    }
}
