package ru.tp.lingany.lingany.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.fragments.AuthFragment;


public class AuthActivity extends AppCompatActivity {

    private AuthFragment authFragment;

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
        authFragment.checkIntent(intent);
    }
}
