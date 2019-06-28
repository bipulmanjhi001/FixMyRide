package com.ust.fixmyride.model;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ust.fixmyride.R;
import com.ust.fixmyride.currentbooking.Current_Booking;
import com.ust.fixmyride.home.HomeActivity;
import com.ust.fixmyride.login.LoginActivity;
import com.ust.fixmyride.profile.Profile;
import com.ust.fixmyride.rating.Rating;

/**
 * Created by Bipul on 14-10-2016.
 */
public class HistoryDrawerFragment extends DialogFragment {
    LinearLayout currentoders,services,profile,logoutfromaccount,historyofbooking;
    SlidingPaneLayout startanimation;
    SharedPreferences admin,fbregister,fblogin,googlelogin,googleregister;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MY_DIALOG);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d!=null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.navigation_drawer_menu, container, false);

        admin=this.getActivity().getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        fbregister=this.getActivity().getApplicationContext().getSharedPreferences("registerfblogin", Context.MODE_PRIVATE);
        googleregister=this.getActivity().getApplicationContext().getSharedPreferences("registergmail", Context.MODE_PRIVATE);
        googlelogin=this.getActivity().getApplicationContext().getSharedPreferences("logingmail", Context.MODE_PRIVATE);
        fblogin=this.getActivity().getApplicationContext().getSharedPreferences("loginfb", Context.MODE_PRIVATE);
        startanimation=(SlidingPaneLayout)root.findViewById(R.id.startanimation);

        currentoders=(LinearLayout)root.findViewById(R.id.currentorders);
        currentoders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Current_Booking.class);
                startActivity(intent);
                dismiss();
            }
        });
        services=(LinearLayout)root.findViewById(R.id.services);
        services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                dismiss();
            }
        });
        profile=(LinearLayout)root.findViewById(R.id.profileaccount);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Profile.class);
                startActivity(intent);
                dismiss();
            }
        });
        logoutfromaccount=(LinearLayout)root.findViewById(R.id.logoutfromaccount);
        logoutfromaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin.edit().remove("id").apply();
                admin.edit().remove("pass").apply();
                googleregister.edit().remove("registergmailname").apply();
                googleregister.edit().remove("registergmailemailid").apply();
                fbregister.edit().remove("registerfbloginname").apply();
                fbregister.edit().remove("registerfblastname").apply();

                googlelogin.edit().remove("logingmailname").apply();
                googlelogin.edit().remove("logingmailemailid").apply();
                fblogin.edit().remove("loginfbname").apply();
                fblogin.edit().remove("loginfblastname").apply();
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                dismiss();
            }
        });
        historyofbooking=(LinearLayout)root.findViewById(R.id.historyofbooking);
        historyofbooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        startanimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return root;
    }

}

