package com.ust.fixmyride.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ust.fixmyride.R;
import com.ust.fixmyride.constants.Constant;
import com.ust.fixmyride.home.HomeActivity;
import com.ust.fixmyride.model.ConnectionDetector;
import com.ust.fixmyride.model.SmoothCheckBox;
import com.ust.fixmyride.register.RegisterActivity;
import com.ust.fixmyride.forgotpassword.ForgotActivity;
import com.ust.fixmyride.model.CircularImageView;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class LoginActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    TextView registertext,forgottext;
    ImageView login;
    TextView eye;
    //Signin button
    private SignInButton signInButton;
    //Signing Options
    private GoogleSignInOptions gso;
    //google api client
    private GoogleApiClient mGoogleApiClient;
    //Signin constant to check the activity result
    private int RC_SIGN_IN = 100;
    SharedPreferences pref;
    String userid,password;
    String id,pass;
    EditText loginpageuserEdittextID,loginpagepasswordEdittextID;
    private CoordinatorLayout coordinatorlayout;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    boolean gpsEnabled;
    String getuserid,getpassword;
    SharedPreferences.Editor editor;
    String saveid,savepass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        coordinatorlayout=(CoordinatorLayout)findViewById(R.id.logincoordinatorlayout);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        loginpagepasswordEdittextID=(EditText)findViewById(R.id.loginpagepasswordEdittextID);
        loginpageuserEdittextID=(EditText)findViewById(R.id.loginpageuserEdittextID);
         eye=(TextView)findViewById(R.id.showpassword);
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginpagepasswordEdittextID.setInputType(InputType.TYPE_CLASS_TEXT);
                eye.setVisibility(View.GONE);
            }
        });
        registertext=(TextView)findViewById(R.id.registertext);
        registertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        forgottext=(TextView)findViewById(R.id.forgottext);
        forgottext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,ForgotActivity.class);
                startActivity(intent);
            }
        });
        login=(ImageView)findViewById(R.id.loginbutton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        final SmoothCheckBox scb = (SmoothCheckBox) findViewById(R.id.logincheckbox);

        scb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if(checkBox.isChecked()) {
                        pref = getSharedPreferences("rememberme", Context.MODE_PRIVATE);
                        editor = pref.edit();
                        final boolean firstcheck = pref.getBoolean("remember", true);
                        if (firstcheck) {
                            saveid = loginpageuserEdittextID.getText().toString();
                            savepass = loginpagepasswordEdittextID.getText().toString();
                            editor.putBoolean("remember", false);
                            editor.putString("saveuserid", saveid);
                            editor.putString("savepassword", savepass);
                            editor.apply();

                        }else {
                            getuserid = pref.getString("saveuserid", "");
                            getpassword = pref.getString("savepassword", "");
                            loginpageuserEdittextID.setText(getuserid);
                            loginpagepasswordEdittextID.setText(getpassword);

                    }
                }

            }
        });

        //Initializing google signin option
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        //Initializing signinbutton
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setScopes(gso.getScopeArray());

        //Initializing google api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        //Setting onclick listener to signing button
        signInButton.setOnClickListener(this);
    }
    //This function will option signing intent
    private void signIn() {
        //Creating an intent
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        //Starting intent for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If signin
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Calling a new function to handle signin
            handleSignInResult(result);
        }
    }

    //After the signing we are calling this function
    private void handleSignInResult(GoogleSignInResult result) {

        //If the login succeed
        if (result.isSuccess()) {
            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();
            //Displaying name and email
            String getname=acct.getDisplayName();
            String email=acct.getEmail();

            pref = getApplicationContext().getSharedPreferences("logingmail", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("logingmailname", getname);
            editor.putString("logingmailemailid",email);
            editor.apply();

            Toast.makeText(this," Welcome  "+getname,Toast.LENGTH_LONG).show();

            Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == signInButton) {
            //Calling signin
            signIn();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
    }

    //Validate Data locally(Checks whether the fields are empty or not)
    private void validateData() {

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(loginpageuserEdittextID.getText().toString()))
        {

            loginpageuserEdittextID.setError("Required field!");
            focusView = loginpageuserEdittextID;
            cancel = true;

        }
        else if(TextUtils.isEmpty(loginpagepasswordEdittextID.getText().toString()))
        {
            loginpagepasswordEdittextID.setError("Required field!");
            focusView = loginpagepasswordEdittextID;
            cancel = true;
        }

        if(cancel){

            focusView.requestFocus();
        }
        else
        {
            getTextValues();

        }
    }//validateData

    //Get the values from EditText
    private void getTextValues() {
        userid = loginpageuserEdittextID.getText().toString();
        password = loginpagepasswordEdittextID.getText().toString();
        id="admin";
        pass="admin";
        pref = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
       // check for Internet status
        if (isInternetPresent && gpsEnabled) {

            if (userid.equals(id) && password.equals(pass)) {
                showSuccessDialog();
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("id", id);
                editor.putString("pass", pass);
                editor.apply();

            } else {
                showFailureDialog();
            }
        }
            else{
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
                snackbarActionTextView.setTextSize(14);

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
    }

    //Show Success Dialog
    private void showSuccessDialog(){
        Intent intent1  = new Intent(LoginActivity.this,HomeActivity.class);
        startActivity(intent1);
        finish();
        Toast.makeText(LoginActivity.this, "Welcome "+ userid, Toast.LENGTH_SHORT).show();
    }

    //Show failure Dialog
    private void showFailureDialog(){
        Toast.makeText(LoginActivity.this, "Enter Valid User name and Password.", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
