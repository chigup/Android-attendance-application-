package com.example.akshay_agrawal.nssattendance;

/**
 * Created by Akshay_agrawal on 20-07-2017.
 */

import android.support.annotation.NonNull;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Controller {

    public static final String TAG = "TAG";
    public static final String WAURL = "YOUR APPSCRIPT GOES HERE";
    private static Response response;

    public static JSONObject insertData(String id, String day, String sheet) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(WAURL + "action=mark_attendance" + "&id=" + id + "&date=" + day + "&sheet_name=" + sheet)
                    .build();
            response = client.newCall(request).execute();
            String responseData = response.body().string();
            Log.e(TAG, "response from gs" + responseData);
            return new JSONObject(responseData);

        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "recieving null " + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject updateData(String id, String name, String sheet) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(WAURL + "action=insert_student&id=" + id + "&name=" + name + "&sheet_name=" + sheet)
                    .build();
            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());

        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "recieving null " + e.getLocalizedMessage());
        }
        return null;
    }
}
