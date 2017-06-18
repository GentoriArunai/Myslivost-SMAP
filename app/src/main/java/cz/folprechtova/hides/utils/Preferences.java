package cz.folprechtova.hides.utils;


import android.content.Context;

import cz.folprechtova.hides.HidesApp;


public class Preferences {

    private static final String PREFS = "PREFS";
    private static final String USERNAME = "USERNAME";

    public static String getUserName() {
        return HidesApp.getContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(USERNAME, "");
    }

    public static void setUserName(String username) {
        HidesApp.getContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putString(USERNAME, username).apply();
    }
}

