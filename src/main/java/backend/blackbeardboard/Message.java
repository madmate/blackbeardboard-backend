package backend.blackbeardboard;

import org.json.JSONObject;

import java.time.Instant;

public class Message {
    private final String context;
    private final Instant timestamp;

    /**
     * Instanziiere Message mit definiertem Zeitstempel
     * @param context Nachrichtenninhalt
     * @param timestamp Zeitstempel
     */
    public Message(String context, Instant timestamp) {
        this.context = context;
        this.timestamp = timestamp;
    }

    /**
     * Instanziiere Message mit Zeit des Aufrufs als Zeitstempel
     * @param context Nachrichteninhalt
     */
    public Message(String context) {
        this(context, Instant.now());
    }

    /**
     * Gibt den Nachrichteninhalt zurück
     * @return
     */
    public String getContext() {
        return context;
    }

    /**
     * Gibt den Zeitstempel zurück
     * @return Zeitstempel
     */
    public Instant getTimestamp() {
        return timestamp;
    }

    /**
     * JSON Repräsentation von Message
     * @return
     */
    public JSONObject toJSON() {
        JSONObject object = new JSONObject();
        object.put("content", getContext());
        object.put("timestamp", getTimestamp().getEpochSecond());
        return object;
    }
}
