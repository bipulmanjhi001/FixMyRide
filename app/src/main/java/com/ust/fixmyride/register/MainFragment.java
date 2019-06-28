package com.ust.fixmyride.register;

/**
 * Created by Bipul on 07-08-2016.
 */


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.ust.fixmyride.R;
import com.ust.fixmyride.home.HomeActivity;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {

    private CallbackManager callbackManager;
    SharedPreferences pref;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    SharedPreferences.Editor editor;
    String firstname,name,lastname,id;
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            displayMessage(profile);

            pref = getActivity().getSharedPreferences("registerfblogin", Context.MODE_PRIVATE);
            editor = pref.edit();
            editor.putString("registerfbloginname", name);
            editor.putString("registerfblastname",lastname);

            // Save the changes in SharedPreferences
            editor.apply(); // commit changes

            Toast.makeText(getActivity()," Welcome  "+name,Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);

            //added for logout from facebook
            LoginManager.getInstance().logOut();
            getActivity().finish();
        }

        @Override
        public void onCancel() {
            Toast.makeText(getActivity(),"Login Fail",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(FacebookException e) {
            Toast.makeText(getActivity(),"Retry again",Toast.LENGTH_SHORT).show();
        }
    };

    public MainFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        //new added
        AppEventsLogger.activateApp(this.getActivity());
        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker= new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayMessage(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_facebook, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button2);

        loginButton.setReadPermissions("user_friends");
        loginButton.setReadPermissions("email");
        loginButton.setReadPermissions("public_profile");
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void displayMessage(Profile profile){
        if(profile != null){
             name=profile.getName();
             firstname=profile.getFirstName();
             lastname=profile.getLastName();
             id=profile.getId();
             Uri uri=profile.getProfilePictureUri(50,50);
             Log.d("regfbimg",uri.toString());

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);
    }
}