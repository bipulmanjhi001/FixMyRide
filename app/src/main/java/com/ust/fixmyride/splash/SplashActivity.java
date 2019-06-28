package com.ust.fixmyride.splash;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ust.fixmyride.home.HomeActivity;
import com.ust.fixmyride.introslider.WelcomeActivity;
import com.ust.fixmyride.login.LoginActivity;
import com.ust.fixmyride.R;
import com.ust.fixmyride.model.ConnectionDetector;

public class SplashActivity extends AppCompatActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    private Boolean isInternetPresent = false;

    private ConnectionDetector cd;
    SharedPreferences prefs,fbprefs;
    private CoordinatorLayout coordinatorlayout;
    boolean gpsEnabled;
    SharedPreferences pref,registerpref;
    SharedPreferences admin;
    String id,pass;
    String gmailname,emailid,registergmailname,registeremailid;
    String fbname,registerfbname;
    ImageView tv_logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        coordinatorlayout=(CoordinatorLayout)findViewById(R.id.coordinatorlayout);
        cd = new ConnectionDetector(getApplicationContext());

        pref = getApplicationContext().getSharedPreferences("logingmail", MODE_PRIVATE);
        gmailname = pref.getString("logingmailname","");
        emailid= pref.getString("logingmailemailid","");

        prefs=getApplicationContext().getSharedPreferences("loginfb",MODE_PRIVATE);
        fbname= prefs.getString("loginfbname","");

        registerpref=getApplicationContext().getSharedPreferences("registergmail", MODE_PRIVATE);
        registergmailname=registerpref.getString("registergmailname","");
        registeremailid=registerpref.getString("registergmailemailid","");

        fbprefs=getApplicationContext().getSharedPreferences("registerfblogin",MODE_PRIVATE);
        registerfbname=fbprefs.getString("registerfbloginname","");

        admin=getApplicationContext().getSharedPreferences("login",MODE_PRIVATE);
        id=admin.getString("id","");
        pass=admin.getString("pass","");
        tv_logo=(ImageView)findViewById(R.id.animatelogo);
    }

    @Override
    public void onBackPressed() {
        Intent i = getBaseContext().getPackageManager().
                getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Always call the superclass method first
        // The activity is either being restarted or started for the first time
        // so this is where we should make sure that GPS is enabled
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // check for Internet status
        if (isInternetPresent && gpsEnabled) {
            // Internet Connection is Present
            new Handler().postDelayed(new Runnable() {
                /*
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */
                @Override
                public void run() {
                    // This method will be executed once the timer is over
                  if ((!gmailname.isEmpty()&& !emailid.isEmpty())||
                          (!fbname.isEmpty())||(!registeremailid.isEmpty() && !registergmailname.isEmpty())||
                          (!registerfbname.isEmpty())||(!id.isEmpty() &&(!pass.isEmpty()))) {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                      startActivity(intent);
                      finish();
                }else {
                      Intent i = new Intent(SplashActivity.this, WelcomeActivity.class);
                      startActivity(i);
                      finish();
                  }
                }
            }, SPLASH_TIME_OUT);
        }

        else {
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

    @Override
    protected void onRestart() {
        super.onRestart();
        // Always call the superclass method first
        // Activity being restarted from stopped state
    }

}


