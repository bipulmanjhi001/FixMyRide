package com.ust.fixmyride.profile;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ust.fixmyride.R;
import com.ust.fixmyride.camera.RunJson;
import com.ust.fixmyride.home.HomeActivity;
import com.ust.fixmyride.model.ConnectionDetector;
import com.ust.fixmyride.model.ProfileDrawerFragment;

/**
 * Created by Bipul on 20-09-2016.
 */
public class Profile extends AppCompatActivity {
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "Profile";
    com.ust.fixmyride.model.CircularImageView camera1,profilepic;
    Bitmap bmp;
    String imagurl;
    com.ust.fixmyride.model.CircularImageView brandfab;
    TextView getfname,getlname,getaddress,getphone,getemail,setemailid,setname;

    private Boolean isInternetPresent = false;

    private ConnectionDetector cd;
    boolean gpsEnabled;
    CoordinatorLayout coordinatorlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        coordinatorlayout=(CoordinatorLayout)findViewById(R.id.profile_coordinatorlayout);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!isInternetPresent && !gpsEnabled) {
            // Ask user to connect to Internet
            Snackbar snackbar = Snackbar
                    .make(coordinatorlayout, "Enable GPS and Internet!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    });
            TextView snackbarActionTextView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_action);
            snackbarActionTextView.setTextSize(16);

            snackbarActionTextView.setTextColor(Color.RED);
            snackbarActionTextView.setTypeface(snackbarActionTextView.getTypeface(), Typeface.BOLD);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setMaxLines(1);
            textView.setTextSize(14);
            textView.setSingleLine(true);
            textView.setTypeface(null, Typeface.BOLD);
            snackbar.show();
        }
        profilepic=(com.ust.fixmyride.model.CircularImageView)findViewById(R.id.profilepicture);
        brandfab=(com.ust.fixmyride.model.CircularImageView)findViewById(R.id.profilefab);

        try {
            Bundle extras = getIntent().getExtras();
            bmp = (Bitmap) extras.getParcelable("imagebitmap");
            imagurl=extras.getString("imagepath");
            Log.d("imagurl",imagurl);

        }catch (NullPointerException e){
            e.printStackTrace();
        }
         profilepic.setImageBitmap(bmp);
         brandfab.setImageBitmap(bmp);

        camera1=(com.ust.fixmyride.model.CircularImageView)findViewById(R.id.cameraclick);
        getaddress=(TextView)findViewById(R.id.getaddress);
        getemail=(TextView)findViewById(R.id.getemail);
        getfname=(TextView)findViewById(R.id.getfname);
        getlname=(TextView)findViewById(R.id.getlname);
        getphone=(TextView)findViewById(R.id.getpnumber);
        setemailid=(TextView)findViewById(R.id.useremail);
        setname=(TextView)findViewById(R.id.username);
        camera1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               callnew();
           }
       });

    }
    public void callnew(){
        ViewDialog alert = new ViewDialog();
        alert.showDialog(Profile.this, "Ready for take snap..");
    }
    public class ViewDialog  {

        public void showDialog(Activity activity, String msg){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.edit_profile_dailog);

            final Button Button = (Button) dialog.findViewById(R.id.opencamera);
            Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Profile.this, RunJson.class);
                    startActivity(intent);
                }
            });

            final Button Button2 = (Button) dialog.findViewById(R.id.opengallery);
                  Button2.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          Toast.makeText(getApplicationContext(), "Gallery opening..", Toast.LENGTH_SHORT).show();
                          openImageChooser();
                      }
                  });

            Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText fname=(EditText)dialog.findViewById(R.id.setfname);
                    EditText lname=(EditText)dialog.findViewById(R.id.setlname);
                    EditText address=(EditText)dialog.findViewById(R.id.setaddress);

                    boolean cancel = false;
                    View focusView = null;

                    if(TextUtils.isEmpty(fname.getText().toString()))
                    {
                        fname.setError("Required field!");
                        focusView = fname;
                        cancel = true;
                    }
                    else if(TextUtils.isEmpty(lname.getText().toString()))
                    {
                        lname.setError("Required field!");
                        focusView = lname;
                        cancel = true;
                    }
                    else if(TextUtils.isEmpty(address.getText().toString()))
                    {
                        address.setError("Required field!");
                        focusView = address;
                        cancel = true;
                    }

                    if(cancel){

                        focusView.requestFocus();
                    }
                    else
                    {
                        if(isInternetPresent && gpsEnabled) {
                            String setfname = fname.getText().toString();
                            getfname.setText(setfname);
                            setname.setText(setfname);
                            String setlname = lname.getText().toString();
                            getlname.setText(setlname);
                            String setaddress = address.getText().toString();
                            getaddress.setText(setaddress);
                        }else{
                            ShowAlert();
                        }
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();
        }
    }

    /* Choose an image from Gallery */
    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                // Get the url from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    String path = getPathFromURI(selectedImageUri);
                    Log.i(TAG, "Image Path : " + path);
                    // Set the image in ImageView
                   profilepic.setImageURI(selectedImageUri);
                   brandfab.setImageURI(selectedImageUri);

                }
            }
        }
    }

    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Profile.this, HomeActivity.class);
        startActivity(intent);
        Profile.this.finish();
    }
    private void ShowAlert(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Whoops! Its seems you don't have internet connection, please try again later!")
                .setTitle("No Internet Connection")
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

    }
}
