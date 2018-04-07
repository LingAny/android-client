package ru.tp.lingany.lingany.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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


public class ReflectionActivity extends AppCompatActivity {

    public static final String EXTRA_NATIVE_LANG = "EXTRA_NATIVE_LANG";
    public static final String EXTRA_FOREIGN_LANG = "EXTRA_FOREIGN_LANG";

    private final ParsedRequestListener<Reflection> getByLangListener = new ParsedRequestListener<Reflection>() {
        @Override
        public void onResponse(Reflection reflection) {
            Log.i("tag", "onResponse");

//            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = prefs.edit();

            editor.putString(getString(R.string.reflectionId), reflection.getId().toString());
            editor.putBoolean(getString(R.string.isInitRef), true);
            editor.apply();

            Intent intent = new Intent(ReflectionActivity.this, CategoryActivity.class);
            intent.putExtra(CategoryActivity.EXTRA_REFLECTION, reflection.getId().toString());
            startActivity(intent);
        }

        @Override
        public void onError(ANError anError) {
            Log.e("tag", anError.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflection);

        Intent intent = getIntent();
        Language nativeLang = (Language) intent.getSerializableExtra(EXTRA_NATIVE_LANG);
        Language foreignLang = (Language) intent.getSerializableExtra(EXTRA_FOREIGN_LANG);

        TextView title = findViewById(R.id.title);
        title.setText(getString(R.string.pleaseWait));

        ProgressBar progress = findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);

        Api.getInstance().reflections().getByLanguages(nativeLang, foreignLang, getByLangListener);
    }
}
