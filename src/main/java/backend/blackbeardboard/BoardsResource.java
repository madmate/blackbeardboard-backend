package backend.blackbeardboard;

import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/boards")
public class BoardsResource {

    @Inject
    BoardController boardController;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getBoards() {
        return "This will get your boards as list ... later";
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteBoards() {
        return "This will delete your boards ... later";
    }

}