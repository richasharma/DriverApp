package com.ekaaksh.driverapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ekaaksh.driverapp.Model.Beans.LoginBean;

public class AppPrefrences {
    // App preference Name
    public static final String Prefsname = "FoodonaRestaurent";
    public static final String KEY_ID = "id";
    public static final String KEY_RES_ID = "res_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_EMAIL_ID = "email";
    public static final String KEY_PASSWORD = "res_id";
    public static final String KEY_VEHICLE_NO = "vehicle_no";
    public static final String KEY_VEHICLE_TYPE = "vehicle_type";
    public static final String KEY_ATTENDANCE = "attendance";
    public static final String KEY_CREATED_AT = "created_at";
    public static final String KEY_DEVICE_TYPE = "device_type";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_IS_ACTIVE = "is_active";
    public static final String KEY_LAT = "latitude";
    public static final String KEY_LONG = "longitude";

    public static SharedPreferences.Editor editor;

    public static void SaveUserdetail(Context ctx, LoginBean loginBean) {

        SharedPreferences prefs = ctx.getSharedPreferences(Prefsname,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_ID, loginBean.getId());
        editor.putString(KEY_RES_ID, loginBean.getRes_id());
        editor.putString(KEY_NAME, loginBean.getName());
        editor.putString(KEY_PHONE, loginBean.getPhone());
        editor.putString(KEY_EMAIL_ID, loginBean.getEmail());
        editor.putString(KEY_PASSWORD, loginBean.getPassword());
        editor.putString(KEY_VEHICLE_NO, loginBean.getVehicle_no());
        editor.putString(KEY_VEHICLE_TYPE, loginBean.getVehicle_type());
        editor.putString(KEY_ATTENDANCE, loginBean.getAttendance());
        editor.putString(KEY_CREATED_AT, loginBean.getCreated_at());
        editor.putString(KEY_DEVICE_TYPE, loginBean.getDevice_type());
        editor.putString(KEY_TOKEN, loginBean.getToken());
        editor.putString(KEY_IS_ACTIVE, loginBean.getIsactive());
        editor.putString(KEY_LAT, loginBean.getLat());
        editor.putString(KEY_LONG, loginBean.getLng());



        Log.d("nnnnn", loginBean.getEmail() + loginBean.getEmail() + loginBean.getId() + "HHH" + loginBean.getId());
        editor.commit();
    }


    public static LoginBean getSavedUser(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(Prefsname,
                Context.MODE_PRIVATE);
        LoginBean modal = new LoginBean();
        modal.setId(prefs.getString(KEY_ID, "-1"));
        modal.setRes_id(prefs.getString(KEY_RES_ID, "-1"));
        modal.setName(prefs.getString(KEY_NAME, "-1"));
        modal.setPhone(prefs.getString(KEY_PHONE, "-1"));
        modal.setEmail(prefs.getString(KEY_EMAIL_ID, "-1"));
        modal.setPassword(prefs.getString(KEY_PASSWORD, "-1"));
        modal.setVehicle_no(prefs.getString(KEY_VEHICLE_NO, "-1"));
        modal.setVehicle_type(prefs.getString(KEY_VEHICLE_TYPE, "-1"));
        modal.setAttendance(prefs.getString(KEY_ATTENDANCE, "-1"));
        modal.setCreated_at(prefs.getString(KEY_CREATED_AT, "-1"));
        modal.setDevice_type(prefs.getString(KEY_DEVICE_TYPE, "-1"));
        modal.setToken(prefs.getString(KEY_TOKEN, "-1"));
        modal.setIsactive(prefs.getString(KEY_IS_ACTIVE, "-1"));
        modal.setLat(prefs.getString(KEY_LAT, "-1"));
        modal.setLng(prefs.getString(KEY_LONG, "-1"));





        return modal;

    }

    public static void clearPrefsdata(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(Prefsname,
                Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.clear().commit();

    }
}
