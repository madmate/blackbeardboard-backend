package backend.blackbeardboard;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/board")
public class BoardResource {
    @Inject
    BoardController boardController;
    @Inject
    SseHandler sseHandler;
    String TAG = getClass().getName();
    @GET
    public Response getBoard(@QueryParam("name") String name) {
        Logger.log(TAG,"GET: name=\"" + name + "\"");
        if (name == null) {
            Logger.log(TAG,"No name specified");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("name is mandatory").build();
        }
        Board board = boardController.getBoard(name);
        if (board == null) {
            Logger.log(TAG,"Board not found");
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("board not found").build();
        }
        Logger.log(TAG,"GET: successful");
        return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(board.toJSON().toString()).build();
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response createBoard(@QueryParam("name") String name, @DefaultValue("100") @QueryParam("deprecationTime") String deprecationTime) {
        Logger.log(TAG,"POST: create Board \""+name+"\" with deprecationTime: "+ deprecationTime);
        if (name == null || name.equals("")) {
            Logger.log(TAG,"No name specified");
            return Response.status(Response.Status.BAD_REQUEST).entity("specify name of board to create").build();
        }
        if (boardController.containsBoard(name)) {
            Logger.log(TAG,"Board already exists");
            return Response.status(Response.Status.CONFLICT).entity("board \"" + name + "\" already exists").build();
        }
        int deprecationTimeInt;
        try {
            deprecationTimeInt = Integer.parseInt(deprecationTime);
        } catch (NumberFormatException e) {
            Logger.log(TAG,"Bad deprecation time");
            return Response.status(Response.Status.BAD_REQUEST).entity("specify deprecation time").build();
        }
        Board board = new Board(name, deprecationTimeInt);
        boardController.addBoard(board);
        sseHandler.sendBoardsAdded(new Board[]{board});
        Logger.log(TAG,"POST: successful");
        return Response.status(Response.Status.CREATED).entity("successfully created board \"" + name + "\"").build();
    }

    @PUT
    public Response updateBoard(@QueryParam("name") String name, @QueryParam("message") String message) {
        Logger.log(TAG,"PUT: name=\""+name+"\" message=\""+message+"\"");
        if (name == null || name.equals("")) {
            Logger.log(TAG,"No name specified");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("specify name of board to create").build();
        }
        if (boardController.getBoard(name) == null) {
            Logger.log(TAG,"Board not found");
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("board not found").build();
        }

        if (message == null) {
            boardController.getBoard(name).setMessage(null);
        } else {
            boardController.getBoard(name).setMessage(new Message(message));
        }
        sseHandler.sendBoardsChanged(new Board[]{boardController.getBoard(name)});
        Logger.log(TAG,"PUT: successful");
        return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(boardController.getBoard(name).toJSON().toString()).build();
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteBoard(@QueryParam("name") String name) {
        Logger.log(TAG,"DELETE: name=\""+name+"\"");
        if (name == null || name.equals("")) {
            Logger.log(TAG,"Name not specified");
            return Response.status(Response.Status.BAD_REQUEST).entity("specify name of board to create").build();
        }
        Board board = boardController.deleteBoard(name);
        if (board == null) {
            Logger.log(TAG,"No Board found");
            return Response.status(Response.Status.NOT_FOUND).entity("no board to delete").build();
        } else {
            sseHandler.sendBoardsDeleted(new Board[]{board});
            Logger.log(TAG,"DELETE: successful");
            return Response.status(Response.Status.OK).entity("board deleted").build();
        }
    }
}