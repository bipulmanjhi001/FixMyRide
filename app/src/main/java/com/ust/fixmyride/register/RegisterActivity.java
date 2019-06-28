package com.ust.fixmyride.register;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.ust.fixmyride.home.HomeActivity;
import com.ust.fixmyride.login.CustomVolleyRequest;
import com.ust.fixmyride.model.CommonUtils;
import com.ust.fixmyride.model.ConnectionDetector;
import com.ust.fixmyride.model.SmoothCheckBox;
import com.ust.fixmyride.validate.Validate_Registration;

public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener  {
    //Signin button
    private SignInButton signInButton;
    //Signing Options
    private GoogleSignInOptions gso;
    //google api client
    private GoogleApiClient mGoogleApiClient;
    //Signin constant to check the activity result
    private int RC_SIGN_IN = 100;
    SharedPreferences pref;
    private Boolean isInternetPresent = false;
    // Connection detector class
    private ConnectionDetector cd;
    EditText registerfirstname,registerlastname,
            registeraddress,registerphoneno,
            registeremailaddress,registerpassword,registerconfirmpassword;
    private String fname = null,acceptterm=null;
    private String lname = null;
    private String mob_number = null;
    private String paddress = null;
    private String email=null;
    private String password=null;
    private String confirmpassword = null;
    private Context applicationContext;
    private String purposetype="sign_up";
    private String MobilePattern = "[0-9]{10}";
    boolean gpsEnabled;
    ImageView registerbutton;
    CoordinatorLayout coordinatorlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        coordinatorlayout=(CoordinatorLayout)findViewById(R.id.registercoordinatorlayout);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        acceptterm="Y";
        registerbutton=(ImageView)findViewById(R.id.registerbutton);

        /**********************************************************************/
        registerfirstname=(EditText)findViewById(R.id.registerfirstname);
        registerlastname=(EditText)findViewById(R.id.registerlastname);
        registeraddress=(EditText)findViewById(R.id.registeraddress);
        registerphoneno=(EditText)findViewById(R.id.registerphoneno);
        registeremailaddress=(EditText)findViewById(R.id.registeremailaddress);
        registerpassword=(EditText)findViewById(R.id.registerpassword);
        registerconfirmpassword=(EditText)findViewById(R.id.registerconfirmpassword);
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        /*********************************************************************/
        final SmoothCheckBox scb = (SmoothCheckBox) findViewById(R.id.registercheckbox);
        try {
            scb.setChecked(true);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        scb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                Log.d("SmoothCheckBox", String.valueOf(isChecked));
                if(!checkBox.isChecked()){
                    //display in short period of time
                    acceptterm="N";
                    Toast.makeText(getApplicationContext(), "Accept the terms & conditions", Toast.LENGTH_SHORT).show();
                }
                else{
                    acceptterm="Y";
                }
            }
        });
        //Initializing google signin option
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        //Initializing signinbutton
        signInButton = (SignInButton) findViewById(R.id.sign_in_button2);
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
            pref = getApplicationContext().getSharedPreferences("registergmail", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("registergmailname", getname);  // Saving string
            editor.putString("registergmailemailid",email);
            editor.apply();

            Toast.makeText(this," Welcome  "+getname,Toast.LENGTH_LONG).show();

            Intent intent=new Intent(RegisterActivity.this,HomeActivity.class);
            startActivity(intent);
            RegisterActivity.this.finish();

        } else {
            //If login fails
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

        if(TextUtils.isEmpty(registerfirstname.getText().toString()))
        {

            registerfirstname.setError("Required field!");
            focusView = registerfirstname;
            cancel = true;
        }
        else if(TextUtils.isEmpty(registerlastname.getText().toString()))
        {

            registerlastname.setError("Required field!");
            focusView = registerlastname;
            cancel = true;

        }
        else if(TextUtils.isEmpty(registeraddress.getText().toString()))
        {

            registeraddress.setError("Required field!");
            focusView = registeraddress;
            cancel = true;

        }
        else if(TextUtils.isEmpty(registerphoneno.getText().toString()))
        {
            registerphoneno.setError("Required field!");
            focusView = registerphoneno;
            cancel = true;

        }
        else if(TextUtils.isEmpty(registeremailaddress.getText().toString()))
        {

            registeremailaddress.setError("Required field!");
            focusView = registeremailaddress;
            cancel = true;

        }
        else if(TextUtils.isEmpty(registerpassword.getText().toString()))
        {

            registerpassword.setError("Required field!");
            focusView = registerpassword;
            cancel = true;

        }
        else if(TextUtils.isEmpty(registerconfirmpassword.getText().toString()))
        {
            registerconfirmpassword.setError("Required field!");
            focusView = registerconfirmpassword;
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

        fname = registerfirstname.getText().toString();
        lname = registerlastname.getText().toString();
        paddress = registeraddress.getText().toString();
        mob_number = registerphoneno.getText().toString();
        paddress = registeraddress.getText().toString();
        password = registerpassword.getText().toString();
        confirmpassword=registerconfirmpassword.getText().toString();

        if(CommonUtils.mobileNumberPatternMatcher(mob_number) && password.equals(confirmpassword)){
            // check for Internet status
            if (isInternetPresent && gpsEnabled) {
                UserAuthenticated();
            } else {
                // Internet connection is not present
                // Ask user to connect to Internet
                Snackbar snackbar = Snackbar
                        .make(coordinatorlayout, "Enable GPS and Internet!", Snackbar.LENGTH_LONG)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(Settings.ACTION_SETTINGS));
                            }
                        });
                TextView snackbarActionTextView = (TextView) snackbar.getView().findViewById( android.support.design.R.id.snackbar_action );
                snackbarActionTextView.setTextSize(14);
                // Changing message text color
                snackbarActionTextView.setTextColor(Color.RED);
                snackbarActionTextView.setTypeface(snackbarActionTextView.getTypeface(), Typeface.BOLD);
                // Changing action button text color
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
        else{
            Toast.makeText(this, "Please Enter a correct Mobile number or check password!", Toast.LENGTH_SHORT).show();
        }
    }
    private void UserAuthenticated(){
        Intent intent=new Intent(RegisterActivity.this,Validate_Registration.class);
        startActivity(intent);
        RegisterActivity.this.finish();
    }
//-----------------------------------------------------------------------------------------------------//
}
