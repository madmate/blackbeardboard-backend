package backend.blackbeardboard;

import com.google.gson.Gson;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    public void sendBoardsAdded(Board[] boards) {
        String jsonString = "[";
        for (Board board : boards){
            jsonString = jsonString + board.getName() + ",";
        }
        jsonString = jsonString.substring(0,jsonString.length()-1) + "]";
        final OutboundSseEvent event = sse.newEventBuilder()
                .name("boards_added")
                .id(Integer.toString(lastEventId))
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(jsonString)
                .build();
        this.broadcaster.broadcast(event);
        lastEventId++;
    }
    public void sendBoardsChanged(Board[] boards) {
        String jsonString = "[";
        for (Board board : boards){
            jsonString = jsonString + board.toJSON().toString() + ",";
        }
        jsonString = jsonString.substring(0,jsonString.length()-1) + "]";
        final OutboundSseEvent event = sse.newEventBuilder()
                .name("boards_changed")
                .id(Integer.toString(lastEventId))
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(jsonString)
                .build();
        this.broadcaster.broadcast(event);
        lastEventId++;
    }
    public void sendBoardsDeleted(Board[] boards) {
        String jsonString = "[";
        for (Board board : boards){
            jsonString = jsonString + board.getName() + ",";
        }
        jsonString = jsonString.substring(0,jsonString.length()-1) + "]";
        final OutboundSseEvent event = sse.newEventBuilder()
                .name("boards_deleted")
                .id(Integer.toString(lastEventId))
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(jsonString)
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