package backend.blackbeardboard;

import jakarta.inject.Singleton;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class BoardController {

    private Map<String, Board> boards;

    public BoardController() {
        boards = new HashMap<>();
    }

    public Board getBoard(String name) {
        return boards.get(name);
    }

    public JSONArray getBoards() {
        JSONArray jsonArray = new JSONArray();
        boards.forEach((k, v) -> jsonArray.put(v.toJSON()));
        return jsonArray;
    }

    public Board deleteBoard(String name) {
        return boards.remove(name);
    }

    public void addBoard(Board board) {
        boards.put(board.getName(), board);
    }

    public boolean noBoards() {
        return boards.isEmpty();
    }

    public boolean containsBoard(String name) {
        return boards.containsKey(name);
    }

    public void deleteBoards() {
        boards.clear();
    }
}
