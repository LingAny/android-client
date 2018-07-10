package ru.tp.lingany.lingany.fragments;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.orange_box.storebox.StoreBox;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.Settings;


public class AuthFragment extends Fragment {

    private Button googleButton;
    private LayoutInflater inflater;
    // state
    AuthState mAuthState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        return inflater.inflate(R.layout.fragment_auth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        googleButton = getView().findViewById(R.id.googleAuthButton);
        googleButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        processGoogleAuthorization(v);
                    }
                });
    }

    private void processGoogleAuthorization(View view) {
        googleButton.setClickable(false);
        enablePostAuthorizationFlows();

        AuthorizationServiceConfiguration serviceConfiguration = new AuthorizationServiceConfiguration(
                Uri.parse("https://accounts.google.com/o/oauth2/v2/auth") /* auth endpoint */,
                Uri.parse("https://www.googleapis.com/oauth2/v4/token") /* token endpoint */
        );
        AuthorizationService authorizationService = new AuthorizationService(view.getContext());
        String clientId = "511828570984-fuprh0cm7665emlne3rnf9pk34kkn86s.apps.googleusercontent.com";
        Uri redirectUri = Uri.parse("com.google.codelabs.appauth:/oauth2callback");
        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
                serviceConfiguration,
                clientId,
                AuthorizationRequest.RESPONSE_TYPE_CODE,
                redirectUri
        );
        builder.setScopes("profile");

//        if(mMainActivity.getLoginHint() != null){
//            Map loginHintMap = new HashMap<String, String>();
//            loginHintMap.put(LOGIN_HINT,mMainActivity.getLoginHint());
//            builder.setAdditionalParameters(loginHintMap);
//
//            Log.i(LOG_TAG, String.format("login_hint: %s", mMainActivity.getLoginHint()));
//        }

        AuthorizationRequest request = builder.build();
        String action = "com.google.codelabs.appauth.HANDLE_AUTHORIZATION_RESPONSE";
        Intent postAuthorizationIntent = new Intent(action);
        PendingIntent pendingIntent = PendingIntent.getActivity(view.getContext(), request.hashCode(), postAuthorizationIntent, 0);
        authorizationService.performAuthorizationRequest(request, pendingIntent);
    }

    private void enablePostAuthorizationFlows() {
        mAuthState = restoreAuthState();
//        if (mAuthState != null && mAuthState.isAuthorized()) {
//            if (mMakeApiCall.getVisibility() == View.GONE) {
//                mMakeApiCall.setVisibility(View.VISIBLE);
//                mMakeApiCall.setOnClickListener(new MakeApiCallListener(this, mAuthState, new AuthorizationService(this)));
//            }
//            if (mSignOut.getVisibility() == View.GONE) {
//                mSignOut.setVisibility(View.VISIBLE);
//                mSignOut.setOnClickListener(new SignOutListener(this));
//            }
//        } else {
//            mMakeApiCall.setVisibility(View.GONE);
//            mSignOut.setVisibility(View.GONE);
//        }
    }

    @Nullable
    private AuthState restoreAuthState() {
        Settings settings = StoreBox.create(this.getActivity(), Settings.class);
        String authIdentityString = settings.getAuthIdentityString();

        if (!TextUtils.isEmpty(authIdentityString)) {
            try {
                return AuthState.fromJson(authIdentityString);
            } catch (JSONException jsonException) {
                // should never happen
            }
        }
        return null;
    }

    private void getAppRestrictions() {
    }
}
