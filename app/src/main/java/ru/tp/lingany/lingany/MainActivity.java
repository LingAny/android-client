package ru.tp.lingany.lingany;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        boolean isInitRef = prefs.getBoolean(getString(R.string.isInitRef), false);

        if (!isInitRef) {
            startActivity(new Intent(MainActivity.this, ChooseNativeLangActivity.class));
        } else {

        }

        Log.i(TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
//        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
//        editor.putBoolean(IS_INIT_REF_KEY, checkBox.isChecked());
//        editor.apply();
    }
}
