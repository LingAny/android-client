package ru.tp.lingany.lingany;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anton on 22.03.18.
 */

public class ReflectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflection);

        List<String> items = new ArrayList<>();
        fillItems(items);

        String size = String.valueOf(items.size());
        String msg = "len: " + size;
        Log.i("tag", msg);

        RecyclerView recyclerView = findViewById(R.id.native_lang_rv_id);
        ReflectionAdapter adapter = new ReflectionAdapter(items);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


    }

    private void fillItems(List<String> items) {
        items.add("Russian");
        items.add("English");
        items.add("German");
        items.add("Spanish");
        items.add("French");
        items.add("Italian");
    }
}
