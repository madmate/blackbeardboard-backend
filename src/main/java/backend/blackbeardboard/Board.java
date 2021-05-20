package backend.blackbeardboard;

public class Board {
    private String name;
    private Message message;
    private int deprecationTime;

    public Board(String name, int deprecationTime) {
        this.name = name;
        this.deprecationTime = deprecationTime;
    }

    public Board(String name) {
        this(name, 100);
    }

    public String getName() {
        return name;
    }

    public Message getMessage() {
        return message;
    }

    public int getDeprecationTime() {
        return deprecationTime;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void setDeprecationTime(int deprecationTime) {
        this.deprecationTime = deprecationTime;
    }
}
