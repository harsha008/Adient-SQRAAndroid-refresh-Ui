package com.adient.mobility.sqra.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class MySingleTon {

    public static Map<String, Map<String, Map<String, Map<String, String>>>> sqrcData;
    //private static Map<String, Map<String, Map<String, String>>> yearData;
    private static Map<String, Map<String, ArrayList<String>>> modelData;
    //private static Map<String, ArrayList<String>> yearData;
    private static ArrayList<String> componentData;
    private static Map<String, Map<String, Map<String, String>>> yearData;
    private static Map<String, Map<String, String>> detailsData;
    private static Map<String, String> detailsFlowData;
    //public static ArrayList<String> yrDetailArrayList;
    private static Map<String, String> res_detail_yr;
    public static boolean rememberMe;
    public static String smKey;
    public static String json;
    public static  boolean firstLogin = false;

    public static Map<String, Map<String, Map<String, String>>> parseJsonYear(String rowValue) {
        yearData = sqrcData.get(rowValue);
        return yearData;
    }

    public static Map<String, Map<String, String>> parseJsonYearDetail(String data) {
        detailsData=yearData.get(data);
        return detailsData;
    }

    public static Map<String, String> parseJsonYearFlowDetail(String rowValueSticky, String rowValueYr, String rowValue) {

        yearData = sqrcData.get(rowValueSticky);
        detailsData=yearData.get(rowValueYr);
//Log.v("test1",detailsData.toString());

       for (Map.Entry<String, Map<String, String>> entry : detailsData.entrySet()) {
         //   Log.v("test 123", String.valueOf(entry.getValue()));
           String key = entry.getKey();
           if (rowValue.equals(key)) {
               res_detail_yr= entry.getValue();
           }

       /*    if (detailsData.containsKey(rowValue)) {
               res_detail_yr= entry.getValue();
           }*/
            /*if (entry.getKey().equals(rowValue)) {
                Log.v("test 123456", String.valueOf(entry.getValue()));
                res_detail_yr= entry.getValue();
            }*/

        }

        return res_detail_yr;
    }



    public static boolean isNetworkOnline(Context context) {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;

    }

    public static void smAuthenticate(String user, String password) {
        OkHttpClient client = new OkHttpClient();

        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        formEncodingBuilder.add(MyAppConstants.USER, user);
        formEncodingBuilder.add(MyAppConstants.PASSWORD, password);
        RequestBody formBody = formEncodingBuilder.build();

        Request request = new Request.Builder()
                .url(MyAppConstants.PRE_LOGIN_URL)
                .post(formBody)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            smKey = response.priorResponse().priorResponse().header(MyAppConstants.SET_COOKIE);
        } catch (IOException e) {
            Log.d("UtilHelper:", "Fail in smAuthenticate: " + e.getMessage());
        }
    }

    public static void retrieveRegions() {
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .header("Cookie", smKey)
                .url(MyAppConstants.PRE_LOGIN_URL_1)
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            json = response.body().string();
        } catch (IOException e) {
            Log.d("UtilHelper:", "Fail in retrieveModels: " + e.getMessage());
        }
    }
}