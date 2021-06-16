package backend.blackbeardboard;

import org.json.JSONObject;

public class Board {
    private final String name;
    private Message message;
    private int deprecationTime;
    private boolean locked;

    /**
     * Instanziiere ein Board
     * @param name Name
     * @param deprecationTime Sekunden bis Message des Boards abläuft
     */
    public Board(String name, int deprecationTime) {
        this.name = name;
        this.deprecationTime = deprecationTime;
    }

    /**
     * Gibt den Namen des Boards zurück
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt die Message des Boards zurück
     * @return Message
     */
    public Message getMessage() {
        return message;
    }

    /**
     * Setzt die Message des Boards
     * @param message zu setzende Message
     */
    public void setMessage(Message message) {
        this.message = message;
    }

    /**
     * JSON Repräsentation des Boards
     * @return JSONObject oder null
     */
    public JSONObject getMessageJSON() {
        if (getMessage() == null) {
            return null;
        } else {
            return getMessage().toJSON();
        }
    }

    /**
     * Gibt zurück ob Board gesperrt oder nicht
     * @return true wenn gespeert
     */
    public boolean getLocked() {
        return locked;
    }

    /**
     * Sperrt oder entsperrt das Board
     * @param locked true oder false
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     * Gibt die Ablaufzeit der Messages dieses Boards zurück
     * @return Sekunden
     */
    public int getDeprecationTime() {
        return deprecationTime;
    }

    /**
     * Setzt die Ablaufzeit der Messages dieses Boards
     * @param deprecationTime in Sekunden
     */
    public void setDeprecationTime(int deprecationTime) {
        this.deprecationTime = deprecationTime;
    }

    /**
     * Gibt JSON Objekt dieses Boards zurück
     * @return JSONObject
     */
    public JSONObject toJSON() {
        JSONObject object = new JSONObject();
        object.put("name", getName());
        object.put("deprecationTime", getDeprecationTime());
        object.put("message", getMessageJSON());
        return object;
    }
}
