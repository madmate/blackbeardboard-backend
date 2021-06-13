package backend.blackbeardboard;

import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONArray;

import java.util.List;

@Path("/boards")
public class BoardsResource {

    @Inject
    BoardController boardController;

    @Inject
    SseHandler sseHandler;
    private final String TAG = getClass().getName();
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBoards() {
        Logger.log(TAG,"GET");
        JSONArray boards = boardController.getBoardsNamesJSON();
        Logger.log(TAG,"GET successful");
        return Response.status(Response.Status.OK).entity(boards.toString()).build();
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteBoards() {
        Logger.log(TAG,"DELETE");
        if (boardController.noBoards()) {
            Logger.log(TAG,"No boards to delete");
            return Response.status(Response.Status.NOT_FOUND).entity("no boards available").build();
        }
    
        List<Board> boards = boardController.getBoards();
        Board[] board_array = new Board[boards.size()];
        board_array = boards.toArray(board_array);
        sseHandler.sendBoardsDeleted(board_array);
        boardController.deleteBoards();
        Logger.log(TAG,"DELETE: successful");
        return Response.status(Response.Status.OK).entity("all boards deleted").build();
    }

}