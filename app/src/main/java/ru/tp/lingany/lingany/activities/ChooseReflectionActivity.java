package ru.tp.lingany.lingany.activities;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import ru.tp.lingany.lingany.R;

public class ChooseReflectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_reflection);

        FragmentManager fragmentManager = getSupportFragmentManager();

    }
}
