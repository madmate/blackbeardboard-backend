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
    @Produces(MediaType.TEXT_PLAIN)
    public String getBoard(@QueryParam("name") String name) {
        return "This will get your board ... later";
    }

    @GET
    @Path("/boards")
    @Produces(MediaType.APPLICATION_JSON)
    public String getBoards() {
        return "This will get your boards as list ... later";
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response createBoard(@QueryParam("name") String name, @DefaultValue("100") @QueryParam("deprecationTime") String deprecationTime) {
        if (boardController.containsBoard(name)) {
            return Response.status(Response.Status.CONFLICT).entity("board \"" + name + "\" already exists").build();
        }
        if (name == null || name.equals("")) {
            return Response.status(Response.Status.BAD_REQUEST).entity("specify name of board to create").build();
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
    @Produces(MediaType.APPLICATION_JSON)
    public String updateBoard(@QueryParam("name") String name, @QueryParam("message") String message) {
        return "This will update your board ... later";
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteBoard(@QueryParam("name") String name) {
        return "This will delete your board ... later";
    }

    @DELETE
    @Path("/boards")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteBoards() {
        return "This will delete your boards ... later";
    }

}