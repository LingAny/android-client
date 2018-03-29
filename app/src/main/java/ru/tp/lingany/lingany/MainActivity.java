package ru.tp.lingany.lingany;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ru.tp.lingany.lingany.activities.LanguagesActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button categoryButton = findViewById(R.id.languages_btn);
        categoryButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.languages_btn:
                Intent intent = new Intent(this, LanguagesActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
