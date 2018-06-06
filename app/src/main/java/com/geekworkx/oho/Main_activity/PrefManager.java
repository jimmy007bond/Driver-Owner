package com.geekworkx.oho.Main_activity;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by parag on 12/01/17.
 */
public class PrefManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "Contri";


    private static final String NEW_VERSION = "Newversion";
    private static final String KEY_IS_WAITING_FOR_SMS = "IsWaitingForSms";
    private static final String SERVICE_STATES = "Isservicestate";
    private static final String KEY_IS_FIRST_NOTIFICATION = "Isfirstnotification";
    private static final String KEY_IS_FORUM_NOTIFICATION = "Isforumnotification";
    private static final String KEY_MOBILE_NUMBER = "mobile_number";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_WHO = "who";
    public static final String KEY_MOBILE = "nothing";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_FIRST_TIME_IMAGEARRAY = "IsFirstTimeImages";
    private static final String IS_FIRST_DATA_LAUNCH = "IsFirstDataLaunch";
    private  String IS_FIRST_CONFIRM_LAUNCH= "IsFirstConfirmLaunch";
    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setIsWaitingForSms(boolean isWaiting) {
        editor.putBoolean(KEY_IS_WAITING_FOR_SMS, isWaiting);
        editor.commit();
    }

    public void setFirstNotification(boolean isfirst) {
        editor.putBoolean(KEY_IS_FIRST_NOTIFICATION, isfirst);
        editor.commit();
    }


    public boolean isWaitingForSms() {
        return pref.getBoolean(KEY_IS_WAITING_FOR_SMS, false);
    }
    public boolean FirstNotification() {
        return pref.getBoolean(KEY_IS_FIRST_NOTIFICATION, false);
    }


    public void setMobileNumber(String mobileNumber) {
        editor.putString(KEY_MOBILE_NUMBER, mobileNumber);
        editor.commit();
    }


    public String getMobileNumber() {
        return pref.getString(KEY_MOBILE_NUMBER, null);
    }

    public void createLogin( String name,String phone,String WHO) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_MOBILE, phone);
        editor.putString(KEY_WHO, WHO);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }

    public void logout(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();


    }

    public void setNewVersion(String version) {
        editor.putString(NEW_VERSION, version);
        editor.commit();
    }

    public String getNewVersion() {
        return pref.getString(NEW_VERSION, null);
    }


    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put(KEY_NAME, pref.getString(KEY_NAME, null));
        profile.put(KEY_MOBILE, pref.getString(KEY_MOBILE, null));
        profile.put(KEY_WHO, pref.getString(KEY_WHO, null));
        return profile;
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setForumNotification(boolean isfirst) {
        editor.putBoolean(KEY_IS_FORUM_NOTIFICATION, isfirst);
        editor.commit();
    }

    public boolean isForumNotification() {
        return pref.getBoolean(KEY_IS_FORUM_NOTIFICATION, true);
    }

    public void setFirstTimeImagearray(boolean isFirstImage) {
        editor.putBoolean(IS_FIRST_TIME_IMAGEARRAY, isFirstImage);
        editor.commit();
    }

    public boolean isFirstTimeImagearray() {
        return pref.getBoolean(IS_FIRST_TIME_IMAGEARRAY, true);
    }

    public void setServiceState(boolean isFirstTime) {
        editor.putBoolean(SERVICE_STATES, isFirstTime);
        editor.commit();
    }

    public boolean getServiceState() {
        return pref.getBoolean(SERVICE_STATES, true);
    }

    public void setFirstDataLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_DATA_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstDataLaunch() {
        return pref.getBoolean(IS_FIRST_DATA_LAUNCH, true);
    }

    public void setFirstConfirmation(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_CONFIRM_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstConfirmation() {
        return pref.getBoolean(IS_FIRST_CONFIRM_LAUNCH, true);
    }

    public void deleteState() {
        editor.clear().commit();
    }

}