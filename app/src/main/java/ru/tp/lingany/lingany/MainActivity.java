package ru.tp.lingany.lingany;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import ru.tp.lingany.lingany.activities.LanguagesActivity;

public class MainActivity extends AppCompatActivity {

    private CheckBox checkBox;
    private Button categoryButton;
    private static final String IS_INIT_REF_KEY = "isInitRefKey";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkBox = findViewById(R.id.checkBox);

        categoryButton = findViewById(R.id.languages_btn);

        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LanguagesActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        checkBox.setChecked(prefs.getBoolean(IS_INIT_REF_KEY, false));
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putBoolean(IS_INIT_REF_KEY, checkBox.isChecked());
        editor.apply();
    }
}
