package backend.blackbeardboard;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/locking")
public class LockingResource {
    @Inject
    BoardController boardController;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLocking(@QueryParam("name") String name) {
        if (name == null || name.equals("")) {
            return Response.status(Response.Status.BAD_REQUEST).entity("specify name of board to create").build();
        }
        if (!boardController.containsBoard(name)) {
            return Response.status(Response.Status.NOT_FOUND).entity("board \"" + name + "\" does not exists").build();
        }
        Board board = boardController.getBoard(name);
        if (board.getLocked()) {
            return Response.status(Response.Status.FORBIDDEN).entity("board \"" + name + "\" already locked").build();
        }
        board.setLocked(true);
        return Response.status(Response.Status.OK).entity("board \"" + name + "\" is locked now").build();
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response postLocking(@QueryParam("name") String name) {
        if (name == null || name.equals("")) {
            return Response.status(Response.Status.BAD_REQUEST).entity("specify name of board to create").build();
        }
        if (!boardController.containsBoard(name)) {
            return Response.status(Response.Status.NOT_FOUND).entity("board \"" + name + "\" does not exists").build();
        }
        boardController.getBoard(name).setLocked(false);
        return Response.status(Response.Status.OK).entity("board \"" + name + "\" unlocked").build();
    }

}