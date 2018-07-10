package ru.tp.lingany.lingany.fragments;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.TokenResponse;
import net.orange_box.storebox.StoreBox;

import org.json.JSONException;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.Settings;


public class AuthFragment extends Fragment {

    private Button googleButton;
    private LayoutInflater inflater;
    Settings settings;
    // state
    AuthState mAuthState;
    private static final String USED_INTENT = "USED_INTENT";
    public static final String LOG_TAG = "AppAuthSample";

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
        settings = StoreBox.create(this.getActivity(), Settings.class);
    }

    private void processGoogleAuthorization(View view) {
        googleButton.setClickable(false);
        enablePostAuthorizationFlows();

        AuthorizationServiceConfiguration serviceConfiguration = new AuthorizationServiceConfiguration(
                Uri.parse("https://accounts.google.com/o/oauth2/v2/auth") /* auth endpoint */,
                Uri.parse("https://www.googleapis.com/oauth2/v4/token") /* token endpoint */
        );
        String clientId = "511828570984-fuprh0cm7665emlne3rnf9pk34kkn86s.apps.googleusercontent.com";
        Uri redirectUri = Uri.parse("com.google.codelabs.appauth:/oauth2callback");
        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
                serviceConfiguration,
                clientId,
                AuthorizationRequest.RESPONSE_TYPE_CODE,
                redirectUri
        );
        builder.setScopes("profile");

        AuthorizationRequest request = builder.build();

        AuthorizationService authorizationService = new AuthorizationService(view.getContext());

        String action = "com.google.codelabs.appauth.HANDLE_AUTHORIZATION_RESPONSE";
        Intent postAuthorizationIntent = new Intent(action);
        PendingIntent pendingIntent = PendingIntent.getActivity(view.getContext(), request.hashCode(), postAuthorizationIntent, 0);
        authorizationService.performAuthorizationRequest(request, pendingIntent);
    }

    private void enablePostAuthorizationFlows() {
        mAuthState = restoreAuthState();
        if (mAuthState != null && mAuthState.isAuthorized()) {
            Integer i = 1;
        }
    }

    @Nullable
    private AuthState restoreAuthState() {
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

    public void checkIntent(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            googleButton.setVisibility(View.GONE);
            switch (action) {
                case "com.google.codelabs.appauth.HANDLE_AUTHORIZATION_RESPONSE":
                    if (!intent.hasExtra(USED_INTENT)) {
                        handleAuthorizationResponse(intent);
                        intent.putExtra(USED_INTENT, true);
                    }
                    break;
                default:
                    // do nothing
            }
        }
    }

    private void handleAuthorizationResponse(@NonNull Intent intent) {
        AuthorizationResponse response = AuthorizationResponse.fromIntent(intent);
        AuthorizationException error = AuthorizationException.fromIntent(intent);
        final AuthState authState = new AuthState(response, error);
        if (response != null) {
            Log.i(LOG_TAG, String.format("Handled Authorization Response %s ", authState.toJsonString()));
            AuthorizationService service = new AuthorizationService(this.getActivity());
            service.performTokenRequest(response.createTokenExchangeRequest(), new AuthorizationService.TokenResponseCallback() {
                @Override
                public void onTokenRequestCompleted(@Nullable TokenResponse tokenResponse, @Nullable AuthorizationException exception) {
                    if (exception != null) {
                        Log.w(LOG_TAG, "Token Exchange failed", exception);
                    } else {
                        if (tokenResponse != null) {
                            authState.update(tokenResponse, exception);
                            persistAuthState(authState);
                            Log.i(LOG_TAG, String.format("Token Response [ Access Token: %s, ID Token: %s ]", tokenResponse.accessToken, tokenResponse.idToken));
                        }
                    }
                }
            });
        }
    }

    private void persistAuthState(@NonNull AuthState authState) {
        settings.setAuthIdentityString(authState.toJsonString());
        enablePostAuthorizationFlows();
    }

    private void clearAuthState() {
        settings.setAuthIdentityString(null);
    }
}
