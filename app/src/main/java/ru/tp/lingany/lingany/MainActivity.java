package ru.tp.lingany.lingany;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.UUID;

import ru.tp.lingany.lingany.activities.CategoryActivity;
import ru.tp.lingany.lingany.activities.ChooseNativeLangActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

//        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isInitRef = prefs.getBoolean(getString(R.string.isInitRef), false);

        if (!isInitRef) {
            startActivity(new Intent(MainActivity.this, ChooseNativeLangActivity.class));
        } else {
            String refIdKey = getString(R.string.reflectionId);
            String reflectionId = prefs.getString(refIdKey, UUID.randomUUID().toString());
            Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
            intent.putExtra(CategoryActivity.EXTRA_REFLECTION, reflectionId);
            startActivity(intent);
        }

        Log.i(TAG, "onStart");
    }
}
