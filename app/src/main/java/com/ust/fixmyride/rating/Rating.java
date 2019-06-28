package com.ust.fixmyride.rating;

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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hedgehog.ratingbar.RatingBar;
import com.ust.fixmyride.R;
import com.ust.fixmyride.home.HomeActivity;
import com.ust.fixmyride.model.ConnectionDetector;
import com.ust.fixmyride.model.HistoryDrawerFragment;


/**
 * Created by Bipul on 20-09-2016.
 */
public class Rating extends AppCompatActivity {
   ImageView thubanimation,thubanimation2;
    com.ust.fixmyride.model.CircularImageView ratingfab;
    EditText feedbackedittext;
    com.ust.fixmyride.model.FooterBarLayout ratingcontinue;

    private Boolean isInternetPresent = false;

    private ConnectionDetector cd;
    boolean gpsEnabled;
    CoordinatorLayout ratingcoordinatorlayout;
    String rating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ratingcoordinatorlayout=(CoordinatorLayout)findViewById(R.id.ratingcoordinatorlayout);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        RatingBar mRatingBar = (RatingBar) findViewById(R.id.ratingbar);
        mRatingBar.setStarEmptyDrawable(getResources().getDrawable(R.mipmap.ic_star_empty));
        mRatingBar.setStarHalfDrawable(getResources().getDrawable(R.mipmap.star_halff));
        mRatingBar.setStarFillDrawable(getResources().getDrawable(R.mipmap.star_fully));
        mRatingBar.setStarCount(5);
        mRatingBar.setStar(4f);
        mRatingBar.halfStar(true);
        mRatingBar.setmClickable(true);
        feedbackedittext=(EditText)findViewById(R.id.feedbackedittext);

        thubanimation=(ImageView)findViewById(R.id.thubanimation);
        thubanimation2=(ImageView)findViewById(R.id.thubanimation2);
        mRatingBar.setOnRatingChangeListener(
                new RatingBar.OnRatingChangeListener() {
                    @Override
                    public void onRatingChange(float RatingCount) {

                        if(RatingCount<4){
                            thubanimation2.setVisibility(View.VISIBLE);
                            thubanimation.setVisibility(View.GONE);
                            rating=String.valueOf(RatingCount);
                        }
                        else if(RatingCount>=4){
                            thubanimation.setVisibility(View.VISIBLE);
                            thubanimation2.setVisibility(View.GONE);
                            rating=String.valueOf(RatingCount);
                        }
                    }
                }
        );
        ratingfab=(com.ust.fixmyride.model.CircularImageView)findViewById(R.id.ratingfab);
        ratingfab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                HistoryDrawerFragment frag = new HistoryDrawerFragment();
                frag.show(ft, "txn_tag");
                return false;
            }
        });
        ratingcontinue=(com.ust.fixmyride.model.FooterBarLayout)findViewById(R.id.ratingcontinue);
        ratingcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkdetails();
            }
        });
    }
    private void checkdetails(){
        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(feedbackedittext.getText().toString()))
        {
            feedbackedittext.setError("Required field!");
            focusView = feedbackedittext;
            cancel = true;
        }
        if(cancel){
            focusView.requestFocus();
        }
        else
        {
            getTextValues();
        }
    }

    private void getTextValues() {
        if (isInternetPresent && gpsEnabled) {
            Intent intent1  = new Intent(Rating.this,HomeActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            Rating.this.finish();
            startActivity(intent1);
            overridePendingTransition(0, 0);
        }else{
            // Ask user to connect to Internet
            Snackbar snackbar = Snackbar
                    .make(ratingcoordinatorlayout, "Enable GPS and Internet!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    });
            TextView snackbarActionTextView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_action);
            snackbarActionTextView.setTextSize(16);

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
    public void onBackPressed() {
        Intent intent = new Intent(Rating.this, HomeActivity.class);
        startActivity(intent);
        Rating.this.finish();
    }
}
