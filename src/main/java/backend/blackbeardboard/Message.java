package backend.blackbeardboard;

import org.json.JSONObject;

import java.time.Instant;

public class Message {
    private String context;
    private Instant timestamp;

    public Message(String context, Instant timestamp) {
        this.context = context;
        this.timestamp = timestamp;
    }

    public Message(String context) {
        this(context, Instant.now());
    }

    public String getContext() {
        return context;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public JSONObject toJSON() {
        JSONObject object = new JSONObject();
        object.put("content", getContext());
        object.put("timestamp", getTimestamp().getEpochSecond());
        return object;
    }
}
