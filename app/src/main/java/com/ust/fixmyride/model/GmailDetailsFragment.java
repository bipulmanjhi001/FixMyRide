package com.ust.fixmyride.model;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ust.fixmyride.R;
import com.ust.fixmyride.bookingconfirmation.Car_Wash_Booking_Confirmation;

/**
 * Created by Bipul on 24-11-2016.
 */
public class GmailDetailsFragment extends DialogFragment {
    LinearLayout detailschecked;
    EditText filladdress,fillphoneno,fillpassword,fillconfirmpassword;
    String address,phoneno,password,confirmpassword;
    private Boolean isInternetPresent = false;
    // Connection detector class
    private ConnectionDetector cd;
    boolean gpsEnabled;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MY_DIALOG2);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_gmailfragment, container, false);
        cd = new ConnectionDetector(getActivity().getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        filladdress=(EditText)root.findViewById(R.id.filladdress2);
        fillphoneno=(EditText)root.findViewById(R.id.fillphoneno2);
        fillpassword=(EditText)root.findViewById(R.id.fillpassword2);
        fillconfirmpassword=(EditText)root.findViewById(R.id.fillconfirmpassword2);

        detailschecked=(LinearLayout)root.findViewById(R.id.detailschecked2);
        detailschecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkdetails();
            }
        });
        return root;
    }
    private void checkdetails(){
        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(filladdress.getText().toString()))
        {

            filladdress.setError("Required field!");
            focusView = filladdress;
            cancel = true;

        }
        else if(TextUtils.isEmpty(fillphoneno.getText().toString()))
        {
            fillphoneno.setError("Required field!");
            focusView = fillphoneno;
            cancel = true;
        }
        else if(TextUtils.isEmpty(fillpassword.getText().toString()))
        {
            fillpassword.setError("Required field!");
            focusView = fillpassword;
            cancel = true;
        }
        else if(TextUtils.isEmpty(fillconfirmpassword.getText().toString()))
        {
            fillconfirmpassword.setError("Required field!");
            focusView = fillconfirmpassword;
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
    private void getTextValues() {
        address=filladdress.getText().toString();
        phoneno=fillphoneno.getText().toString();
        password = fillpassword.getText().toString();
        confirmpassword=fillconfirmpassword.getText().toString();
        if (isInternetPresent && gpsEnabled){
            if(CommonUtils.mobileNumberPatternMatcher(phoneno)&& password.equals(confirmpassword)) {
                Intent intent = new Intent(getActivity(), Car_Wash_Booking_Confirmation.class);
                startActivity(intent);
                dismiss();
            }else{
                Toast.makeText(getActivity(), "Please Enter a correct Mobile number or check password!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "Enable your Internet and GPS ! ", Toast.LENGTH_SHORT).show();
        }
    }
}