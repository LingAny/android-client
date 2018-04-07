package ru.tp.lingany.lingany.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.UUID;

import ru.tp.lingany.lingany.R;


public class CategoryActivity extends AppCompatActivity {

    public static final String EXTRA_REFLECTION = "EXTRA_REFLECTION";

    private UUID reflectionId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);

        Intent intent = getIntent();
        reflectionId = UUID.fromString(intent.getStringExtra(EXTRA_REFLECTION));

        TextView title = findViewById(R.id.title);
        title.setText(reflectionId.toString());
    }
}
