package com.ust.fixmyride.carrepair;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ust.fixmyride.R;
import com.ust.fixmyride.brand.Pick_Your_Brand;
import com.ust.fixmyride.home.HomeActivity;
import com.ust.fixmyride.model.ConnectionDetector;
import com.ust.fixmyride.model.ForAllDrawerFragment;


/**
 * Created by Bipul on 19-09-2016.
 */
public class Car_Repair extends AppCompatActivity {
    com.ust.fixmyride.model.FooterBarLayout submit;
    RelativeLayout tyrelayout,towlinglayout,carginglayout,partsreparinglayout,partsreplacementlayout,carservicelayout;
    ImageView tyreimage,towingimage,chargingimage,repairingimage,bodyrepairimage,serviceimage;
    ImageView tyrechargingcheck,vechicletowingcheck,batterychargingcheck,partsrepaircheck,bodypartsreplacementcheck,carservicecheck;
    com.ust.fixmyride.model.CircularImageView carrepairfab;
    CoordinatorLayout car_repair_coordinatorlayout;
    private Boolean a,b,c,d,e,f;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    boolean gpsEnabled;
    String car_repair;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_repair);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        car_repair=getIntent().getExtras().getString("car_repair");
        pref = getApplicationContext().getSharedPreferences("getservicevalues", MODE_PRIVATE);
        editor = pref.edit();
        car_repair_coordinatorlayout=(CoordinatorLayout)findViewById(R.id.car_repair_coordinatorlayout);
        a=false;b=false;c=false;d=false;e=false;f=false;
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // check for Internet status
        if (!isInternetPresent && !gpsEnabled) {
            // Ask user to connect to Internet
            Snackbar snackbar = Snackbar
                    .make(car_repair_coordinatorlayout, "Enable GPS and Internet!", Snackbar.LENGTH_LONG)
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
        /////////////////////////////////////////////Call footer//////////////////////////////////////////////////////////

        submit=(com.ust.fixmyride.model.FooterBarLayout)findViewById(R.id.carrepaircontinue);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a == true || b == true || c == true || d == true || e == true || f == true) {
                    //pref
                    Intent intent = new Intent(Car_Repair.this, Pick_Your_Brand.class);
                    startActivity(intent);
                    Car_Repair.this.finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Choose atleast one service.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        /////////////////////////////////////////////////Call check images////////////////////////////////////////////////

        tyrechargingcheck=(ImageView)findViewById(R.id.tyrechargingcheck);
        vechicletowingcheck=(ImageView)findViewById(R.id.vechicletowingcheck);
        batterychargingcheck=(ImageView)findViewById(R.id.batterychargingcheck);
        partsrepaircheck=(ImageView)findViewById(R.id.partsrepaircheck);
        bodypartsreplacementcheck=(ImageView)findViewById(R.id.bodypartsreplacementcheck);
        carservicecheck=(ImageView)findViewById(R.id.carservicecheck);

        ////////////////////////////////////////////////Call outer layout//////////////////////////////////////////////////

       tyrelayout=(RelativeLayout)findViewById(R.id.tyrelayout);
        tyrelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tyrelayout.setBackgroundResource(0);
                tyrechargingcheck.setVisibility(View.GONE);
                a=false;
                editor.remove("Tyre_Charging&Repair").apply();
            }
        });
        towlinglayout=(RelativeLayout)findViewById(R.id.towlinglayout);
        towlinglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                towlinglayout.setBackgroundResource(0);
                vechicletowingcheck.setVisibility(View.GONE);
                b=false;
                editor.remove("Vehicle_Towing").apply();
            }
        });
        carginglayout=(RelativeLayout)findViewById(R.id.carginglayout);
        carginglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carginglayout.setBackgroundResource(0);
               batterychargingcheck.setVisibility(View.GONE);
                c=false;
                editor.remove("Battery_Charging").apply();
            }
        });
        partsreparinglayout=(RelativeLayout)findViewById(R.id.partsreparinglayout);
        partsreparinglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partsreparinglayout.setBackgroundResource(0);
                partsrepaircheck.setVisibility(View.GONE);
                d=false;
                editor.remove("Mechanical_Parts_Repairing").apply();
            }
        });
        partsreplacementlayout=(RelativeLayout)findViewById(R.id.partsreplacementlayout);
        partsreplacementlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partsreplacementlayout.setBackgroundResource(0);
               bodypartsreplacementcheck.setVisibility(View.GONE);
                 e=false;
                editor.remove("Body_Parts_Replacement").apply();
            }
        });
        carservicelayout=(RelativeLayout)findViewById(R.id.carservicelayout);
        carservicelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carservicelayout.setBackgroundResource(0);
                carservicecheck.setVisibility(View.GONE);
                f=false;
                editor.remove("Car_Service").apply();
            }
        });

        /////////////////////////////////////////////Call logo images///////////////////////////////////////////////////////////
        tyreimage=(ImageView)findViewById(R.id.tyreimage);
        tyreimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tyrechargingcheck.getVisibility()== View.GONE) {
                    tyrechargingcheck.setVisibility(View.VISIBLE);
                    tyrelayout.setBackgroundResource(R.color.colorPrimaryDark);
                    a=true;
                    editor.putString("Tyre_Charging&Repair","Tyre_Charging&Repair").apply();
                }
            }
        });

        towingimage=(ImageView)findViewById(R.id.towingimage);
        towingimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vechicletowingcheck.getVisibility()==View.GONE) {
                    vechicletowingcheck.setVisibility(View.VISIBLE);
                    towlinglayout.setBackgroundResource(R.color.colorPrimaryDark);
                    b=true;
                    editor.putString("Vehicle_Towing","Vehicle_Towing").apply();
                }
            }
        });
        chargingimage=(ImageView)findViewById(R.id.chargingimage);
        chargingimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(batterychargingcheck.getVisibility()== View.GONE) {
                    batterychargingcheck.setVisibility(View.VISIBLE);
                    carginglayout.setBackgroundResource(R.color.colorPrimaryDark);
                    c=true;
                    editor.putString("Battery_Charging","Battery_Charging").apply();
                }
            }
        });
        repairingimage=(ImageView)findViewById(R.id.repairingimage);
        repairingimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(partsrepaircheck.getVisibility()== View.GONE) {
                    partsrepaircheck.setVisibility(View.VISIBLE);
                    partsreparinglayout.setBackgroundResource(R.color.colorPrimaryDark);
                    d=true;
                    editor.putString("Mechanical_Parts_Repairing","Mechanical_Parts_Repairing").apply();
                }
            }
        });
        bodyrepairimage=(ImageView)findViewById(R.id.bodyrepairimage);
        bodyrepairimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bodypartsreplacementcheck.getVisibility()== View.GONE) {
                    bodypartsreplacementcheck.setVisibility(View.VISIBLE);
                    partsreplacementlayout.setBackgroundResource(R.color.colorPrimaryDark);
                    e=true;
                    editor.putString("Body_Parts_Replacement","Body_Parts_Replacement").apply();
                }
            }
        });
        serviceimage=(ImageView)findViewById(R.id.serviceimage);
        serviceimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(carservicecheck.getVisibility()==View.GONE) {
                    carservicecheck.setVisibility(View.VISIBLE);
                    carservicelayout.setBackgroundResource(R.color.colorPrimaryDark);
                    f=true;
                    editor.putString("Car_Service","Car_Service").apply();
                }
            }
        });
      ////////////////////////////////////////////////////Navigation Drawer/////////////////////////////////////////////////////

        carrepairfab=(com.ust.fixmyride.model.CircularImageView)findViewById(R.id.carrepairfab);
        carrepairfab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ForAllDrawerFragment frag = new ForAllDrawerFragment();
                frag.show(ft, "txn_tag");
                return false;
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this, HomeActivity.class);
        startActivity(intent);
        Car_Repair.this.finish();
    }

}
