package backend.blackbeardboard;

import org.json.JSONObject;

public class Board {
    private final String name;
    private Message message;
    private int deprecationTime;
    private boolean locked;

    public Board(String name, int deprecationTime) {
        this.name = name;
        this.deprecationTime = deprecationTime;
    }

    public String getName() {
        return name;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public JSONObject getMessageJSON() {
        if (getMessage() == null) {
            return null;
        } else {
            return getMessage().toJSON();
        }
    }

    public boolean getLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public int getDeprecationTime() {
        return deprecationTime;
    }

    public void setDeprecationTime(int deprecationTime) {
        this.deprecationTime = deprecationTime;
    }

    public JSONObject toJSON() {
        JSONObject object = new JSONObject();
        object.put("name", getName());
        object.put("deprecationTime", getDeprecationTime());
        object.put("message", getMessageJSON());
        return object;
    }
}
