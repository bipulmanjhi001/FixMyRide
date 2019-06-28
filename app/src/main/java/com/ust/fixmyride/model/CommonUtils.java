package com.ust.fixmyride.model;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CommonUtils{

	//Internet Connection checker
	public static boolean isInternelAvailable(Context mContext) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
	//End
	
	public static boolean mobileNumberPatternMatcher(String mobNumber)	
	  {
		  
		  String str = "^[2-9][0-9]{9}$";	  
		  Pattern pttrn = Pattern.compile(str);
		  Matcher mtch = pttrn.matcher(mobNumber);
		  return mtch.matches();		  
		  
	  }
	
	public static int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}

	
	
	
}//Main Class


