package com.ust.fixmyride.model;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
 * Created by Bipul on 27-10-2016.
 */
public class FacebookDetailsFragment extends DialogFragment {
    LinearLayout detailschecked;
    EditText filladdress,fillphoneno,fillemailaddress,fillpassword,fillconfirmpassword;
    String address,phoneno,emailaddress,password,confirmpassword;
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
        View root = inflater.inflate(R.layout.activity_facebookfragment, container, false);
        cd = new ConnectionDetector(getActivity().getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        filladdress=(EditText)root.findViewById(R.id.filladdress);
        fillphoneno=(EditText)root.findViewById(R.id.fillphoneno);
        fillemailaddress=(EditText)root.findViewById(R.id.fillemailaddress);
        fillpassword=(EditText)root.findViewById(R.id.fillpassword);
        fillconfirmpassword=(EditText)root.findViewById(R.id.fillconfirmpassword);

        detailschecked=(LinearLayout)root.findViewById(R.id.detailschecked);
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
        else if(TextUtils.isEmpty(fillemailaddress.getText().toString()))
        {
            fillemailaddress.setError("Required field!");
            focusView = fillemailaddress;
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
        emailaddress=fillemailaddress.getText().toString();
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