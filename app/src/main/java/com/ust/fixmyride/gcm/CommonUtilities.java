package com.ust.fixmyride.gcm;

import android.content.Context;
import android.content.Intent;


public final class CommonUtilities {

    // give your server registration url here
    //static final String SERVER_URL = "http://117.254.81.61/gcm_server_php/register.php";
	//static final String SERVER_URL = "http://192.168.0.114/gcm_server_php/register.php";

    // Google project id
    //static final String SENDER_ID = "238082438632";
	//static final String SENDER_ID = "471448409197";

    static final String SENDER_ID = "137920823933";
 
    /**
     * Tag used on log messages.
     */
    static final String TAG = "FMR_GCM";
 
    static final String DISPLAY_MESSAGE_ACTION = "com.ust.fixmyride.DISPLAY_MESSAGE";
 
    static final String EXTRA_MESSAGE = "message";
 
    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
    
    
    
    
}
