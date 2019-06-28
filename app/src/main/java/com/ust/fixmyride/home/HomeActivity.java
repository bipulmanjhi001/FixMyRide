package com.ust.fixmyride.home;


import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ust.fixmyride.carrepair.Car_Repair;
import com.ust.fixmyride.carwash.Car_Wash_Service;
import com.ust.fixmyride.R;
import com.ust.fixmyride.model.ConnectionDetector;
import com.ust.fixmyride.model.HomeDrawerFragment;
import com.ust.fixmyride.tutorialscreens.Hometutorial;

public class HomeActivity extends AppCompatActivity {
    LinearLayout carservice, repair;
    LinearLayout financeandinsuranceservice, roadsideassistanceservice, caraccessoriesservice, inhomeservices;
    private CoordinatorLayout coordinatorlayout;

    private Boolean isInternetPresent = false;

    private ConnectionDetector cd;
    boolean gpsEnabled;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

            coordinatorlayout = (CoordinatorLayout) findViewById(R.id.homecoordinatorlayout);
            cd = new ConnectionDetector(getApplicationContext());
            isInternetPresent = cd.isConnectingToInternet();
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        sharedPreferences = getSharedPreferences("ShaPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        boolean  firstTime=sharedPreferences.getBoolean("first", true);
        if(firstTime) {
            editor.putBoolean("first", false);
            editor.apply();
            TutorialScreen();
        }
            // check for Internet status
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
            ImageView fab = (ImageView) findViewById(R.id.homefab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    HomeDrawerFragment frag = new HomeDrawerFragment();
                    frag.show(ft, "txn_tag");
                }
            });
            carservice = (LinearLayout) findViewById(R.id.carwashimage);
            carservice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, Car_Wash_Service.class);
                    intent.putExtra("car_wash","car_wash");
                    startActivity(intent);
                }
            });
            repair = (LinearLayout) findViewById(R.id.carrepairclick);
            repair.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, Car_Repair.class);
                    intent.putExtra("car_repair","car_repair");
                    startActivity(intent);
                }
            });
            financeandinsuranceservice = (LinearLayout) findViewById(R.id.financeandinsuranceservice);
            financeandinsuranceservice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewDialog alert = new ViewDialog();
                    alert.showDialog(HomeActivity.this);
                }
            });

            roadsideassistanceservice = (LinearLayout) findViewById(R.id.roadsideassistanceservice);
            roadsideassistanceservice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewDialog alert = new ViewDialog();
                    alert.showDialog(HomeActivity.this);
                }
            });
            caraccessoriesservice = (LinearLayout) findViewById(R.id.caraccessoriesservice);
            caraccessoriesservice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewDialog alert = new ViewDialog();
                    alert.showDialog(HomeActivity.this);
                }
            });
            inhomeservices = (LinearLayout) findViewById(R.id.inhomeservices);
            inhomeservices.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewDialog alert = new ViewDialog();
                    alert.showDialog(HomeActivity.this);
                }
            });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public class ViewDialog {
        public void showDialog(Activity activity) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.dialog);
            LinearLayout layoutcancel = (LinearLayout) dialog.findViewById(R.id.layoutcancel);
            RelativeLayout relativeLayout = (RelativeLayout) dialog.findViewById(R.id.layoutdismiss);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            layoutcancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
    private void TutorialScreen() {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Hometutorial frag = new Hometutorial();
            frag.show(ft, "txn_tag");
        }

    }


