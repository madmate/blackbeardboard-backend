package backend.blackbeardboard;

import org.json.JSONObject;

public class Board {
    private String name;
    private Message message;
    private int deprecationTime;

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

    public JSONObject getMessageJSON() {
        if (getMessage() == null) {
            return null;
        } else {
            return getMessage().toJSON();
        }
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

    public JSONObject toJSON() {
        JSONObject object = new JSONObject();
        object.put("name", getName());
        object.put("deprecationTime", getDeprecationTime());
        object.put("message", getMessageJSON());
        return object;
    }
}
