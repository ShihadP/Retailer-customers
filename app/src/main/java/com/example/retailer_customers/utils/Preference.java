package com.example.retailer_customers.utils;


import android.content.Context;
import android.content.SharedPreferences;

import com.example.retailer_customers.R;


public class Preference {
    private static final String APP_SETTINGS = "APP_SETTINGS";

    private static SharedPreferences getSharedAppPrefs() {
        return AppController.getInstance().getSharedPreferences(AppController.getInstance().getString(R.string.app_name), Context.MODE_PRIVATE);
    }
    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
    }

    public static void setnotFirstStart(Context context) {
        getSharedAppPrefs().edit().putBoolean("IsFirstStart", false).commit();
    }

    public static boolean getnotFirstStart(Context context) {
        return getSharedAppPrefs().getBoolean("IsFirstStart", true);
    }



    public static void setLoggedMemberId( int userId) {

        getSharedAppPrefs().edit().putInt("userId", userId).apply();

    }

    public static int  getLoggedMemberId( ) {

        return getSharedAppPrefs().getInt("userId", 0);

    }


}
