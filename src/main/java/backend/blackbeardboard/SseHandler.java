package backend.blackbeardboard;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.sse.OutboundSseEvent;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;
import jakarta.ws.rs.sse.SseBroadcaster;


@Singleton
@Path("/")
public class SseHandler {
    private Sse sse;
    private SseBroadcaster broadcaster;
    private int lastEventId = 1;
    @Context
    public void setSse(Sse sse) {
        this.sse = sse;
        this.broadcaster = sse.newBroadcaster();
    }
    public void sendBoardAdded(Board board) {
        final OutboundSseEvent event = sse.newEventBuilder()
                .name("board_added")
                .id(Integer.toString(lastEventId))
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(board.toJSON().toString())
                .build();
        this.broadcaster.broadcast(event);
        lastEventId++;
    }
    public void sendBoardChanged(Board board) {
        final OutboundSseEvent event = sse.newEventBuilder()
                .name("board_changed")
                .id(Integer.toString(lastEventId))
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(board.toJSON().toString())
                .build();
        this.broadcaster.broadcast(event);
        lastEventId++;
    }
    public void sendBoardDeleted(Board board) {
        final OutboundSseEvent event = sse.newEventBuilder()
                .name("board_deleted")
                .id(Integer.toString(lastEventId))
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(board.toJSON().toString())
                .build();
        this.broadcaster.broadcast(event);
        lastEventId++;
    }
    public void sendAllBoardsDeleted() {
        final OutboundSseEvent event = sse.newEventBuilder()
                .name("all_boards_deleted")
                .id(Integer.toString(lastEventId))
                .mediaType(MediaType.TEXT_PLAIN_TYPE)
                .data("Deleted")
                .build();
        this.broadcaster.broadcast(event);
        lastEventId++;
    }

    @GET
    @Path("listen")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void listenToBroadcast(@Context SseEventSink eventSink) {
        this.broadcaster.register(eventSink);
    }
}