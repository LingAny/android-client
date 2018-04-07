package ru.tp.lingany.lingany.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.sdk.Api;
import ru.tp.lingany.lingany.sdk.languages.Language;
import ru.tp.lingany.lingany.sdk.reflections.Reflection;


public class GetReflectionActivity extends AppCompatActivity {

    private ProgressBar progress;

    public static final String EXTRA_NATIVE_LANG = "EXTRA_NATIVE_LANG";
    public static final String EXTRA_FOREIGN_LANG = "EXTRA_FOREIGN_LANG";

    private Language nativeLang;

    private Language foreignLang;

    private final ParsedRequestListener<Reflection> getByLangListener = new ParsedRequestListener<Reflection>() {
        @Override
        public void onResponse(Reflection languages) {
            Log.i("tag", "onResponse");
        }

        @Override
        public void onError(ANError anError) {
            Log.e("tag", anError.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_reflection);

        Intent intent = getIntent();
        nativeLang = (Language) intent.getSerializableExtra(EXTRA_NATIVE_LANG);
        foreignLang = (Language) intent.getSerializableExtra(EXTRA_FOREIGN_LANG);

        TextView title = findViewById(R.id.title);
        title.setText(getString(R.string.pleaseWait));

        progress = findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);

        Api.getInstance().reflections().getByLanguages(nativeLang, foreignLang, getByLangListener);
    }
}
