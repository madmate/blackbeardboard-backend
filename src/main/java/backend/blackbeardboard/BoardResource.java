package backend.blackbeardboard;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/board")
public class BoardResource {

    @Inject
    BoardController boardController;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getBoard(@QueryParam("name") String name) {
        return "This will get your board ... later" + boardController.count();
    }

    @GET
    @Path("/boards")
    @Produces(MediaType.APPLICATION_JSON)
    public String getBoards() {
        return "This will get your boards as list ... later";
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String createBoard(@QueryParam("name") String name, @QueryParam("deprecationTime") String deprecationTime) {
        return "This will create your board ... later " + name + " " + deprecationTime;
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