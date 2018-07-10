package ru.tp.lingany.lingany.activities;


import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.fragments.AuthFragment;


public class AuthActivity extends AppCompatActivity {

    private AuthFragment authFragment;
    public static final String LOG_TAG = "AppAuthSample";
//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        if (supportedLanguages != null) {
//            savedInstanceState.putSerializable(SUPPORTED_LANGUAGES, (Serializable) supportedLanguages);
//        }
//        super.onSaveInstanceState(savedInstanceState);
//    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        authFragment = new AuthFragment();

        if (savedInstanceState != null) {
//            supportedLanguages = (List<Language>) savedInstanceState.getSerializable(SUPPORTED_LANGUAGES);
//            if (supportedLanguages != null) {
//                inflateNativeLangFragment(getResources().getInteger(R.integer.delayInflateAfterLoading));
//                return;
//            }
        }

        inflateAuthFragment();
    }


    @Override
    protected void onStart() {
        super.onStart();
        authFragment.checkIntent(getIntent());
    }
    private void inflateAuthFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, authFragment)
                .commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        System.out.println("NEW INTENT");
        System.out.println("NEW INTENT");
        System.out.println("NEW INTENT");
        System.out.println("NEW INTENT");
        System.out.println("NEW INTENT");
        authFragment.checkIntent(intent);
    }


}
