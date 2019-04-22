package com.example.pecmart;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.ArrayList;
import java.util.List;

public class ApplicationClass extends Application
{
    public static final String APPLICATION_ID = "E2D9D645-E2D7-C841-FFFC-0F56AF709800";
    public static final String API_KEY = "FE066018-69D1-4E7A-B293-EDA9F54331F9";
    public static final String SERVER_URL = "https://api.backendless.com";

    public static BackendlessUser user;
    public static List<items> Item;
    public static List<user> Users ;
    public static List<requests> Request;
    public static List<Completed> completeds;

    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl( SERVER_URL );
        Backendless.initApp( getApplicationContext(),
                APPLICATION_ID,
                API_KEY );

    }
}
