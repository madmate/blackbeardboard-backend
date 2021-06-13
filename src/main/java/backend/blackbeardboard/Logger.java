package backend.blackbeardboard;

import java.text.SimpleDateFormat;

public class Logger {
    public static void log(String TAG,String message){
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        String log_message = timeStamp+": "+ TAG + ": "+ message;
        System.out.println(log_message);
    }
}
