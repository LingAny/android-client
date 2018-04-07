package ru.tp.lingany.lingany;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;


public class App extends Application {

    private static final String IS_INIT_REF_KEY = "isInitRefKey";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("App", "onCreate");

        AndroidNetworking.initialize(getApplicationContext());
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
