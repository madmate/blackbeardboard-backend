package backend.blackbeardboard;

import jakarta.inject.Singleton;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class BoardController {

    private final Map<String, Board> boards;

    public BoardController() {
        boards = new HashMap<>();
    }

    public Board getBoard(String name) {
        return boards.get(name);
    }
    private final String TAG = getClass().getName();
    public JSONArray getBoardsJSON() {
        JSONArray jsonArray = new JSONArray();
        boards.forEach((k, v) -> jsonArray.put(v.toJSON()));
        return jsonArray;
    }

    public List<Board> getBoards() {
        List<Board> board_list = new ArrayList<>();
        for (String key : boards.keySet()) {
            board_list.add(boards.get(key));
        }
        return board_list;
    }

    public JSONArray getBoardsNamesJSON() {
        String[] names = boards.keySet().toArray(new String[0]);
        return new JSONArray(names);
    }

    public Board deleteBoard(String name) {
        Logger.log(TAG,"Deleting board name=\""+name+"\"");
        return boards.remove(name);
    }

    public void addBoard(Board board) {
        Logger.log(TAG,"Adding board name=\""+board.getName()+"\"");
        boards.put(board.getName(), board);
    }

    public boolean noBoards() {
        return boards.isEmpty();
    }

    public boolean containsBoard(String name) {
        return boards.containsKey(name);
    }

    public void deleteBoards() {
        Logger.log(TAG,"Clearing all boards");
        boards.clear();
    }
}
