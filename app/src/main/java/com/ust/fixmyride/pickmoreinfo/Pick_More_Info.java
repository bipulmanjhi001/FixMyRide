package com.ust.fixmyride.pickmoreinfo;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.ust.fixmyride.R;
import com.ust.fixmyride.brand.Pick_Your_Brand;
import com.ust.fixmyride.carwashbooking.Car_wash_Booking;
import com.ust.fixmyride.model.ConnectionDetector;
import com.ust.fixmyride.model.ForAllDrawerFragment;
import com.ust.fixmyride.model.SmoothCheckBox;

import java.util.Calendar;


/**
 * Created by Bipul on 15-09-2016.
 */
public class Pick_More_Info extends AppCompatActivity {
    com.ust.fixmyride.model.FooterBarLayout moreinfo;
    com.ust.fixmyride.model.CircularImageView pickmoreinfofab;
    /**
     * Private members of the class
     */
    private TextView pDisplayDate, displaypicker;
    private LinearLayout pPickDate, yearpicker;
    private int pYear;
    private int myear;
    private int mmonth;
    private int mday;
    int checkyear;
    static final int DATE_DIALOG_ID = 999;
    EditText kilometer,carnumber;
    private CoordinatorLayout coordinatorlayout;

    private Boolean isInternetPresent = false;

    private ConnectionDetector cd;
    boolean gpsEnabled;
    LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_more_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        coordinatorlayout=(CoordinatorLayout)findViewById(R.id.pick_more_info_coordinatorlayout);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
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

        moreinfo = (com.ust.fixmyride.model.FooterBarLayout) findViewById(R.id.addcarmoreinfo);

        kilometer=(EditText)findViewById(R.id.kilometer);
        carnumber=(EditText)findViewById(R.id.carnumber);

        /** Capture our View elements */
        pDisplayDate = (TextView) findViewById(R.id.displayDate);
        pPickDate = (LinearLayout) findViewById(R.id.pickDate);

        /* add popup*/
        displaypicker = (TextView) findViewById(R.id.displayyear);
        yearpicker = (LinearLayout) findViewById(R.id.yearpicker);


        moreinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pDisplayDate.getText().toString().length()>0 && displaypicker.getText().toString().length()>0
                        && kilometer.getText().toString().length()>2 && carnumber.getText().toString().length()>2 ) {
                    Intent intent = new Intent(Pick_More_Info.this, Car_wash_Booking.class);
                    startActivity(intent);
                    Pick_More_Info.this.finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "All fields are mandatory.Please check it.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /** Listener for click event of the button */
        pPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        yearpicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                MyDialogFragment dialog = new MyDialogFragment();
                dialog.show(manager, "dialog");
            }
        });

       //** Get the current date *//*
        final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);

        final Calendar c = Calendar.getInstance();
        myear = c.get(Calendar.YEAR);
        mmonth = c.get(Calendar.MONTH);
        mday = c.get(Calendar.DAY_OF_MONTH);

        pickmoreinfofab=(com.ust.fixmyride.model.CircularImageView)findViewById(R.id.pickmoreinfofab);
        pickmoreinfofab.setOnTouchListener(new View.OnTouchListener() {
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
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                DatePickerDialog _date =   new DatePickerDialog(this, datePickerListener, myear,mmonth,
                        mday){
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        if (year > myear)
                            view.updateDate(myear, mmonth, mday);

                        if (monthOfYear > mmonth && year == myear)
                            view.updateDate(myear, mmonth, mday);

                        if (dayOfMonth > mday && year == myear && monthOfYear == mmonth)
                            view.updateDate(myear, mmonth, mday);
                    }
                };
                return _date;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            myear = selectedYear;
            mmonth = selectedMonth;
            mday = selectedDay;
           if(TextUtils.isEmpty(displaypicker.getText().toString())){
               //display in short period of time
               Toast.makeText(getApplicationContext(), "Select manufacture year first.", Toast.LENGTH_SHORT).show();
           }
            else {
               checkyear = Integer.parseInt(displaypicker.getText().toString());
               if(checkyear <= myear) {
                   // set selected date into textview
                   pDisplayDate.setText(new StringBuilder()
                           .append(mday)
                           .append("-").append(mmonth + 1)
                           .append("-").append(myear)
                           .append(" "));
               }
               else {
                   //display in short period of time
                   Toast.makeText(getApplicationContext(), "Select valid date.", Toast.LENGTH_SHORT).show();

               }
           }

        }
    };
    /****************************************** Year Picker ******************************************************/

    @SuppressLint("ValidFragment")
    class MyDialogFragment extends DialogFragment implements
            AdapterView.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener {
        ListView mylist;
        SwipeRefreshLayout swipeRefreshLayout;
        int manufactureyear1 = pYear - 15;
        int manufactureyear2 = pYear - 14;
        int manufactureyear3 = pYear - 13;
        int manufactureyear4 = pYear - 12;
        int manufactureyear5 = pYear - 11;
        int manufactureyear6 = pYear - 10;
        int manufactureyear7 = pYear - 9;
        int manufactureyear8 = pYear - 8;
        int manufactureyear9 = pYear - 7;
        int manufactureyear10 = pYear - 6;
        int manufactureyear11 = pYear - 5;
        int manufactureyear12 = pYear - 4;
        int manufactureyear13 = pYear - 3;
        int manufactureyear14 = pYear - 2;
        int manufactureyear15 = pYear - 1;
        Integer[] listitems = {pYear, manufactureyear15, manufactureyear14, manufactureyear13,
                manufactureyear12, manufactureyear11, manufactureyear10,
                manufactureyear9, manufactureyear8, manufactureyear7,
                manufactureyear6, manufactureyear5, manufactureyear4,
                manufactureyear3, manufactureyear2, manufactureyear1};

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.year_picker_dialog, null, false);
            mylist = (ListView) view.findViewById(R.id.yearpickerlist);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().setCanceledOnTouchOutside(false);
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout_year);
            return view;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {

            super.onActivityCreated(savedInstanceState);
            ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getActivity(),
                    R.layout.yearlist, R.id.textitem, listitems);

            mylist.setAdapter(adapter);
            mylist.setDivider(null);
            mylist.setOnItemClickListener(this);

        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            dismiss();
            Object item = parent.getItemAtPosition(position);
            String str = item.toString();
            displaypicker.setText(str);
        }

        @Override
        public void onRefresh() {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new Dialog(getActivity(), getTheme()) {
                @Override
                public void onBackPressed() {

                }
            };
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this, Pick_Your_Brand.class);
        startActivity(intent);
        finish();
    }
}
