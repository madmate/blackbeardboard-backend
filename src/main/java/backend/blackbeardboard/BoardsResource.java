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

//Endpoint for receiving/deleting all boards at once
@Path("/boards")
public class BoardsResource {

    //Connect to board controller actually holding and managing the boards
    @Inject
    BoardController boardController;

    //Connect to Server sent events handler
    @Inject
    SseHandler sseHandler;
    //Class TAG for logging
    private final String TAG = getClass().getName();

    //GET for receiving the names of all boards (see interface specification)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBoards() {
        Logger.log(TAG,"GET");
        JSONArray boards = boardController.getBoardsNamesJSON();
        Logger.log(TAG,"GET successful");
        return Response.status(Response.Status.OK).entity(boards.toString()).build();
    }

    //DELETE for deleting all boards (see interface specification)
    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteBoards() {
        Logger.log(TAG,"DELETE");
        if (boardController.noBoards()) {
            Logger.log(TAG,"No boards to delete");
            return Response.status(Response.Status.NOT_FOUND).entity("no boards available").build();
        }

        //Create array of all boards
        List<Board> boards = boardController.getBoards();
        Board[] board_array = new Board[boards.size()];
        board_array = boards.toArray(board_array);
        //Send sse events with all boards
        sseHandler.sendBoardsDeleted(board_array);

        boardController.deleteBoards();
        Logger.log(TAG,"DELETE: successful");
        return Response.status(Response.Status.OK).entity("all boards deleted").build();
    }

}