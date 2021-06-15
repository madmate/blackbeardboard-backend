package backend.blackbeardboard;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/locking")
public class LockingResource {
    @Inject
    BoardController boardController;
    @Inject
    private HttpServletRequest request;
    private final String TAG = getClass().getName();
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLocking(@QueryParam("name") String name) {
        Logger.log(TAG,"GET(getLocking) from: "+request.getRemoteAddr()+" asked if locked name=\""+name+"\"");
        if (name == null || name.equals("")) {
            Logger.log(TAG,"GET(getLocking) from: "+request.getRemoteAddr()+" No name specified");
            return Response.status(Response.Status.BAD_REQUEST).entity("specify name of board to create").build();
        }
        if (!boardController.containsBoard(name)) {
            Logger.log(TAG,"GET(getLocking) from: "+request.getRemoteAddr()+" Board not found");
            return Response.status(Response.Status.NOT_FOUND).entity("board \"" + name + "\" does not exists").build();
        }
        Board board = boardController.getBoard(name);
        if (board.getLocked()) {
            Logger.log(TAG,"GET(getLocking) from: "+request.getRemoteAddr()+" GET successful: board "+name+"locked");
            return Response.status(Response.Status.FORBIDDEN).entity("board \"" + name + "\" already locked").build();
        }
        board.setLocked(true);
        Logger.log(TAG,"GET(getLocking) from: "+request.getRemoteAddr()+ " successful: board"+name+"not locked");
        return Response.status(Response.Status.OK).entity("board \"" + name + "\" is locked now").build();
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response postLocking(@QueryParam("name") String name) {
        Logger.log(TAG,"POST(postLocking) from "+request.getRemoteAddr()+" release lock of board name=\""+name+"\"");
        if (name == null || name.equals("")) {
            Logger.log(TAG,"POST(postLocking) from "+request.getRemoteAddr()+" No name specified");
            return Response.status(Response.Status.BAD_REQUEST).entity("specify name of board to create").build();
        }
        if (!boardController.containsBoard(name)) {
            Logger.log(TAG,"POST(postLocking) from "+request.getRemoteAddr()+" Board not found");
            return Response.status(Response.Status.NOT_FOUND).entity("board \"" + name + "\" does not exists").build();
        }
        boardController.getBoard(name).setLocked(false);
        Logger.log(TAG,"POST(postLocking) from "+request.getRemoteAddr()+" successful, board"+name+"unlocked");
        return Response.status(Response.Status.OK).entity("board \"" + name + "\" unlocked").build();
    }

}