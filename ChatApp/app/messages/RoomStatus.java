package messages;

public class RoomStatus {
    private final String name;
    private final int member;
    public RoomStatus(String name, int member) {
        this.name = name;
        this.member = member;
    }

    public String getName() {
        return name;
    }

    public int getMember() {
        return member;
    }
}
