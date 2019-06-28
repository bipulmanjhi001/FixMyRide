package com.ust.fixmyride.gcm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import static com.ust.fixmyride.gcm.CommonUtilities.SENDER_ID;


public class GCMIntentService extends GCMBaseIntentService {
	 
    private static final String TAG = "GCMIntentService-FixMyRide";

    public GCMIntentService() {
        super(SENDER_ID);
    }
 
    /**
     * Method called on device registered
     **/
    @SuppressLint("LongLogTag")
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: GCM regId = " + registrationId);
        Log.i(TAG, "Your device is registred with GCM");

    }
 
    /**
     * Method called on device unregistered
     * */
    @SuppressLint("LongLogTag")
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        Log.i(TAG, "Your device is unregistred with GCM");
    }
 
    /**
     * Method called on Receiving a new message
     * */
    @SuppressLint("LongLogTag")
    @Override
    protected void onMessage(Context context, Intent intent) { //Works
        Log.i(TAG, "GCM message Received ");
        String message = intent.getExtras().getString("message");
        String data[] = intent.getExtras().getStringArray("data");
        Log.i(TAG, "GCM message Received: "+message); 
        Log.i(TAG, "GCM data Received: "+data);

    }
    /**
     * Method called on receiving a deleted message
     * */
    @SuppressLint("LongLogTag")
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");

    }
 
    /**
     * Method called on Error
     * */
    @SuppressLint("LongLogTag")
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
    }

    @SuppressLint("LongLogTag")
    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);

        return true;
    }

}
