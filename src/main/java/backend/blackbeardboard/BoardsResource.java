package backend.blackbeardboard;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;

@Path("/boards")
public class BoardsResource {

    @Inject
    BoardController boardController;

    @Inject
    SseResource sseHandler;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBoards() {
        JSONArray boards = boardController.getBoards();
        return Response.status(Response.Status.OK).entity(boards.toString()).build();
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteBoards() {
        if (boardController.noBoards()) {
            return Response.status(Response.Status.NOT_FOUND).entity("no boards available").build();
        }
        sseHandler.sendAllBoardsDeleted();
        boardController.deleteBoards();
        return Response.status(Response.Status.OK).entity("all boards deleted").build();
    }

}