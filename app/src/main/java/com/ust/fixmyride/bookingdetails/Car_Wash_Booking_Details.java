package com.ust.fixmyride.bookingdetails;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
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

import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.squareup.timessquare.CalendarPickerView;
import com.ust.fixmyride.R;
import com.ust.fixmyride.bookingconfirmation.Car_Wash_Booking_Confirmation;
import com.ust.fixmyride.model.ConnectionDetector;
import com.ust.fixmyride.model.FacebookDetailsFragment;
import com.ust.fixmyride.model.GmailDetailsFragment;
import com.ust.fixmyride.model.ForAllDrawerFragment;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by Bipul on 20-09-2016.
 */
public class Car_Wash_Booking_Details extends AppCompatActivity {

    com.ust.fixmyride.model.FooterBarLayout continuebookingdetails;
    private LinearLayout pickdateforbookinglayout, bookingtimelayout;
    private TextView displaybookingdate, displaybookingtime;
    com.ust.fixmyride.model.CircularImageView carwashbookingfab;
    /*****************************************************************************************/
    SharedPreferences preferences,preferences2,preferences4,preferences5;
    String gmailname,fbname,gmailname2,fbname2;

    private CoordinatorLayout coordinatorlayout;

    private Boolean isInternetPresent = false;

    private ConnectionDetector cd;
    boolean gpsEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_wash_booking_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        coordinatorlayout=(CoordinatorLayout)findViewById(R.id.car_wash_booking_details_coordinatorlayout);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        continuebookingdetails = (com.ust.fixmyride.model.FooterBarLayout) findViewById(R.id.continuebookingdetails);

        preferences=getSharedPreferences("logingmail",MODE_PRIVATE);
        gmailname= preferences.getString("logingmailname","");

        preferences2=getSharedPreferences("loginfb",MODE_PRIVATE);
        fbname=preferences2.getString("loginfbname","");

        preferences4=getSharedPreferences("registergmail",MODE_PRIVATE);
        gmailname2= preferences4.getString("registergmailname","");

        preferences5=getSharedPreferences("registerfblogin",MODE_PRIVATE);
        fbname2=preferences5.getString("registerfbloginname","");

        continuebookingdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent && gpsEnabled) {
                    if (!fbname.isEmpty()|| !fbname2.isEmpty() && !displaybookingdate.getText().toString().isEmpty() && !displaybookingtime.getText().toString().isEmpty()) {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        FacebookDetailsFragment frag = new FacebookDetailsFragment();
                        frag.show(ft, "txn_tag");

                    } else if(!gmailname.isEmpty() || !gmailname2.isEmpty() && !displaybookingdate.getText().toString().isEmpty() && !displaybookingtime.getText().toString().isEmpty()){
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        GmailDetailsFragment frag = new GmailDetailsFragment();
                        frag.show(ft, "txn_tag");
                    }

                    else if (!displaybookingdate.getText().toString().isEmpty() && !displaybookingtime.getText().toString().isEmpty()) {
                        Intent intent = new Intent(Car_Wash_Booking_Details.this, Car_Wash_Booking_Confirmation.class);
                        startActivity(intent);
                        Car_Wash_Booking_Details.this.finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "All fields are mandatory.", Toast.LENGTH_SHORT).show();
                    }
                }else {
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
        });
        /** Capture our View elements */
        displaybookingdate = (TextView) findViewById(R.id.displaybookingdate);

        pickdateforbookinglayout = (LinearLayout) findViewById(R.id.pickdateforbookinglayout);

        /* add popup*/
        displaybookingtime = (TextView) findViewById(R.id.displaybookingtime);
        bookingtimelayout = (LinearLayout) findViewById(R.id.bookingtimelayout);

        /** Listener for click event of the button */
        pickdateforbookinglayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               //add one month calendar
                DatePickerDialog();
            }
        });
        bookingtimelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize a new time picker dialog fragment
                DialogFragment dFragment = new TimePickerFragment();
                // Show the time picker dialog fragment
                dFragment.show(getFragmentManager(),"Time Picker");
            }
        });

        carwashbookingfab=(com.ust.fixmyride.model.CircularImageView)findViewById(R.id.carwashbookingdetailsfab);

        carwashbookingfab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ForAllDrawerFragment frag = new ForAllDrawerFragment();
                frag.show(ft, "txn_tag");
                return false;
            }
        });
    }
    /********************************** Clock Picker *******************************************/

    @SuppressLint("ValidFragment")
    public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get a Calendar instance
            final Calendar calendar = Calendar.getInstance();
            // Get the current hour and minute
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // TimePickerDialog Theme : THEME_HOLO_LIGHT
            TimePickerDialog tpd = new TimePickerDialog(getActivity(),
                    AlertDialog.THEME_HOLO_LIGHT, this, hour, minute, false);
            // Return the TimePickerDialog
            return tpd;
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Set a variable to hold the current time AM PM Status
            // Initially we set the variable value to AM
            String status = "AM";

            if (hourOfDay > 11) {
                // If the hour is greater than or equal to 12
                // Then the current AM PM status is PM
                status = "PM";
            }

            // Initialize a new variable to hold 12 hour format hour value
            int hour_of_12_hour_format;

            if (hourOfDay > 11) {

                // If the hour is greater than or equal to 12
                // Then we subtract 12 from the hour to make it 12 hour format time
                hour_of_12_hour_format = hourOfDay - 12;
            } else {
                hour_of_12_hour_format = hourOfDay;
            }

            // Display the 12 hour format time in app interface
            displaybookingtime.setText(hour_of_12_hour_format + " : " + minute + " : " + status);
        }

    }
    /***************************************Calander datepicker********************************************************/
    private void DatePickerDialog() {
        final Dialog dialog1;
        dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow();
        Window window = dialog1.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT);
        dialog1.setTitle(null);
        window.setAttributes(wlp);
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setContentView(R.layout.custom_datepicker);
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.DAY_OF_WEEK_IN_MONTH, 1);

        final CalendarPickerView calendar = (CalendarPickerView)dialog1.findViewById(R.id.calendar_view);
        Date today = new Date();
        calendar.init(today, nextYear.getTime())
                .withSelectedDate(today);
        dialog1.findViewById(R.id.done_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toast = "Selected: " + calendar.getSelectedDate();
                String date=calendar.getSelectedDate().toString();
                displaybookingdate.setText(date);
                Toast.makeText(Car_Wash_Booking_Details.this, toast, Toast.LENGTH_SHORT).show();
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this, Car_Wash_Booking_Details.class);
        startActivity(intent);
        finish();
    }
}

