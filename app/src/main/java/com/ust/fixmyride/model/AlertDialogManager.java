package com.ust.fixmyride.model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.ust.fixmyride.R;


public class AlertDialogManager {

    public void showAlertDialog(final Context context, String title, String message,
                                Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        if(status != null)
            // Setting alert dialog icon
            alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                context.startActivity(callGPSSettingIntent);
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
