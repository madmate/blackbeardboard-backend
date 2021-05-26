package backend.blackbeardboard;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;

@Path("/")
@Singleton
public class SseResource {

    @Context
    private Sse sse;

    private volatile SseBroadcaster broadcaster;
    private int lastEventId = 1;

    @PostConstruct
    public void init() {
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