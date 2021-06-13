package backend.blackbeardboard;

import java.text.SimpleDateFormat;

//Called upon logging
//Can be extended to different kinds or locations of logging
public class Logger {
    public static void log(String TAG,String message){
        //Glassfish logs provide a timestamp on its own
        //Optional if e.g. extended to file log
        //String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());

        String log_message = TAG + ": "+ message;
        System.out.println(log_message);
    }
}
