package backend.blackbeardboard;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

//Endpoint for handling action on a single board
@Path("/board")
public class BoardResource {
    //Connect to board controller actually holding and managing the boards
    @Inject
    BoardController boardController;
    //Connect to Server sent events handler
    @Inject
    SseHandler sseHandler;

    @Inject
    private HttpServletRequest request;
    //Class TAG for logging
    String TAG = getClass().getName();

    //GET return full json-object of board with specified name
    @GET
    public Response getBoard(@QueryParam("name") String name) {
        Logger.log(TAG,"GET(getBoard): from: "+ request.getRemoteAddr()+" name=\"" + name + "\"");
        //No name specified
        if (name == null || name.equals("")) {
            Logger.log(TAG,"No name specified");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("name is mandatory").build();
        }
        Board board = boardController.getBoard(name);
        //Board not found
        if (board == null) {
            Logger.log(TAG,"Board not found");
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("board not found").build();
        }
        Logger.log(TAG,"GET: successful");
        return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(board.toJSON().toString()).build();
    }

    //POST create new board with name and deprecation time (default =100)
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response createBoard(@QueryParam("name") String name, @DefaultValue("100") @QueryParam("deprecationTime") String deprecationTime) {
        Logger.log(TAG,"POST(createBoard): from: "+ request.getRemoteAddr()+" name=\""+name+"\" with deprecationTime: "+ deprecationTime);

        //No name specified
        if (name == null || name.equals("")) {
            Logger.log(TAG,"POST: No name specified");
            return Response.status(Response.Status.BAD_REQUEST).entity("specify name of board to create").build();
        }
        //Board with this name already exists
        if (boardController.containsBoard(name)) {
            Logger.log(TAG,"POST(createBoard): from: "+ request.getRemoteAddr()+"Board already exists");
            return Response.status(Response.Status.CONFLICT).entity("board \"" + name + "\" already exists").build();
        }

        //Parse deprecation time in int from string
        int deprecationTimeInt;
        try {
            deprecationTimeInt = Integer.parseInt(deprecationTime);
            //Couldnt parse deprecation time
        } catch (NumberFormatException e) {
            Logger.log(TAG,"POST(createBoard): from: "+ request.getRemoteAddr()+" Bad deprecation time");
            return Response.status(Response.Status.BAD_REQUEST).entity("specify deprecation time").build();
        }
        //Create new board and add it
        Board board = new Board(name, deprecationTimeInt);
        boardController.addBoard(board);

        //Broadcast new added board
        sseHandler.sendBoardsAdded(new Board[]{board});
        Logger.log(TAG,"POST(createBoard): from: "+ request.getRemoteAddr()+" successful: created board "+name+" with dep. time: "+deprecationTime);
        return Response.status(Response.Status.CREATED).entity("successfully created board \"" + name + "\"").build();
    }

    //PUT update an existing board with a message
    @PUT
    public Response updateBoard(@QueryParam("name") String name, @QueryParam("message") String message) {
        Logger.log(TAG,"PUT(updateBoard) from: "+request.getRemoteAddr()+" name=\""+name+"\" message=\""+message+"\"");

        //No name specified
        if (name == null || name.equals("")) {
            Logger.log(TAG,"PUT(updateBoard) from: "+request.getRemoteAddr()+" No name specified");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("specify name of board to create").build();
        }
        //Board not found
        if (boardController.getBoard(name) == null) {
            Logger.log(TAG,"PUT(updateBoard) from: "+request.getRemoteAddr()+" Board not found");
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("board not found").build();
        }
        //One can also clear the message on a board by setting it = null
        if (message == null) {
            boardController.getBoard(name).setMessage(null);
        } else {
            boardController.getBoard(name).setMessage(new Message(message));
        }
        //Broadcast changed board
        sseHandler.sendBoardsChanged(new Board[]{boardController.getBoard(name)});
        Logger.log(TAG,"PUT(updateBoard) from: "+request.getRemoteAddr()+" successful: updated board "+name+" with msg: "+message);
        return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(boardController.getBoard(name).toJSON().toString()).build();
    }

    //DELTE delete board with name= name
    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteBoard(@QueryParam("name") String name) {
        Logger.log(TAG,"DELETE(deleteBoard) from: " +request.getRemoteAddr()+"name=\""+name+"\"");
        //No name specified
        if (name == null || name.equals("")) {
            Logger.log(TAG,"DELETE(deleteBoard) from: " +request.getRemoteAddr()+" Name not specified");
            return Response.status(Response.Status.BAD_REQUEST).entity("specify name of board to create").build();
        }
        Board board = boardController.deleteBoard(name);
        //Board not found
        if (board == null) {
            Logger.log(TAG,"DELETE(deleteBoard) from: " +request.getRemoteAddr()+" No Board found");
            return Response.status(Response.Status.NOT_FOUND).entity("no board to delete").build();
        } else {
            //Broadcast deleted board info
            sseHandler.sendBoardsDeleted(new Board[]{board});
            Logger.log(TAG,"DELETE(deleteBoard) from: " +request.getRemoteAddr()+" successful: deleted board " + name);
            return Response.status(Response.Status.OK).entity("board deleted").build();
        }
    }
}