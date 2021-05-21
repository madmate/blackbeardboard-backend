package backend.blackbeardboard;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/board")
public class BoardResource {

    @Inject
    BoardController boardController;

    @GET
    public Response getBoard(@QueryParam("name") String name) {
        if (name == null) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("name is mandatory").build();
        }
        Board board = boardController.getBoard(name);
        if (board == null) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("board not found").build();
        }
        return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(board.toJSON().toString()).build();
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response createBoard(@QueryParam("name") String name, @DefaultValue("100") @QueryParam("deprecationTime") String deprecationTime) {
        if (name == null || name.equals("")) {
            return Response.status(Response.Status.BAD_REQUEST).entity("specify name of board to create").build();
        }
        if (boardController.containsBoard(name)) {
            return Response.status(Response.Status.CONFLICT).entity("board \"" + name + "\" already exists").build();
        }
        int deprecationTimeInt;
        try {
            deprecationTimeInt = Integer.parseInt(deprecationTime);
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("specify deprecation time").build();
        }
        Board board = new Board(name, deprecationTimeInt);
        boardController.addBoard(board);
        return Response.status(Response.Status.CREATED).entity("successfully created board \"" + name + "\"").build();
    }

    @PUT
    public Response updateBoard(@QueryParam("name") String name, @QueryParam("message") String message) {
        if (name == null || name.equals("")) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("specify name of board to create").build();
        }
        if (message == null) {
            boardController.getBoard(name).setMessage(null);
        } else {
            boardController.getBoard(name).setMessage(new Message(message));
        }
        return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(boardController.getBoard(name).toJSON().toString()).build();
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteBoard(@QueryParam("name") String name) {
        if (name == null || name.equals("")) {
            return Response.status(Response.Status.BAD_REQUEST).entity("specify name of board to create").build();
        }
        Board board = boardController.deleteBoard(name);
        if (board == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("no board to delete").build();
        } else {
            return Response.status(Response.Status.OK).entity("board deleted").build();
        }
    }
}