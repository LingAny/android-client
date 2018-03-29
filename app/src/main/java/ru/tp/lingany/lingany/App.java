package ru.tp.lingany.lingany;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

/**
 * Created by anton on 22.03.18.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("App", "onCreate");

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
