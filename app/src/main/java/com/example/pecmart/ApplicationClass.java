package com.example.pecmart;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.ArrayList;
import java.util.List;

public class ApplicationClass extends Application
{
    public static final String APPLICATION_ID = "XXXXXX";
    public static final String API_KEY = "XXXXX";
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
