package backend.blackbeardboard;

import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.OutboundSseEvent;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseBroadcaster;
import jakarta.ws.rs.sse.SseEventSink;


//Handling register on the SSE endpoint
//Broadcast events to registered sinks
@Singleton
@Path("/")
public class SseHandler {
    private Sse sse;
    private SseBroadcaster broadcaster;

    //Keep track of events send, so clients can keep up, which events he has received already
    private int lastEventId = 1;

    //Injected from context
    //Save sse and create new sse - broadcaster
    @Context
    public void setSse(Sse sse) {
        this.sse = sse;
        this.broadcaster = sse.newBroadcaster();
    }
    //Class TAG for logging
    private final String TAG = getClass().getName();

    //Send new added boards to all listeners
    public void sendBoardsAdded(Board[] boards) {

        //Create list of boards
        String jsonString = "[";
        for (Board board : boards){
            jsonString = jsonString + board.getName() + ",";
        }
        jsonString = jsonString.substring(0, jsonString.length() - 1) + "]";
        Logger.log(TAG,"Sending added boards: "+jsonString);

        //Create sse event object
        final OutboundSseEvent event = sse.newEventBuilder()
                .name("boards_added") //Specified in interface definition
                .id(Integer.toString(lastEventId))
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(jsonString) //Pack data
                .build();
        this.broadcaster.broadcast(event);
        lastEventId++;
        Logger.log(TAG,"Sending added boards successful");
    }

    //Send changed boards to all listeners
    public void sendBoardsChanged(Board[] boards) {
        String jsonString = "[";
        for (Board board : boards) {
            jsonString = jsonString + board.toJSON().toString() + ",";
        }
        jsonString = jsonString.substring(0, jsonString.length() - 1) + "]";
        Logger.log(TAG,"Sending changed boards: "+jsonString);
        final OutboundSseEvent event = sse.newEventBuilder()
                .name("boards_changed")
                .id(Integer.toString(lastEventId))
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(jsonString)
                .build();
        this.broadcaster.broadcast(event);
        lastEventId++;
        Logger.log(TAG,"Sending changed boards successful");
    }
    //Send deleted boards to all listeners
    public void sendBoardsDeleted(Board[] boards) {
        String jsonString = "[";
        for (Board board : boards){
            jsonString = jsonString + board.getName() + ",";
        }
        jsonString = jsonString.substring(0, jsonString.length() - 1) + "]";
        Logger.log(TAG,"Sending deleted boards: "+jsonString);
        final OutboundSseEvent event = sse.newEventBuilder()
                .name("boards_deleted")
                .id(Integer.toString(lastEventId))
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(jsonString)
                .build();
        this.broadcaster.broadcast(event);
        lastEventId++;
        Logger.log(TAG,"Sending deleted boards successful");
    }

    //GET endpoint where a client can register himself
    //Also the connection through which the events can be received
    @GET
    @Path("listen")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void listenToBroadcast(@Context SseEventSink eventSink) {
        this.broadcaster.register(eventSink);
        Logger.log(TAG,"Registered new listener");
    }
}