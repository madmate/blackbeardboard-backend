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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBoards() {
        JSONArray boards = boardController.getBoardsJSON();
        return Response.status(Response.Status.OK).entity(boards.toString()).build();
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteBoards() {
        if (boardController.noBoards()) {
            return Response.status(Response.Status.NOT_FOUND).entity("no boards available").build();
        }

        List<Board> boards = boardController.getBoards();
        Board[] board_array = new Board[boards.size()];
        board_array = boards.toArray(board_array);
        sseHandler.sendBoardsDeleted(board_array);
        boardController.deleteBoards();
        return Response.status(Response.Status.OK).entity("all boards deleted").build();
    }

}