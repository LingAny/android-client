package ru.tp.lingany.lingany.fragments;


import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.TokenResponse;
import net.orange_box.storebox.StoreBox;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.Settings;


public class AuthFragment extends Fragment {

    private Button googleButton;

    Settings settings;
    AuthorizationService authorizationService;
    AuthState authState;

    AppCompatTextView mGivenName;
    AppCompatTextView mFamilyName;
    AppCompatTextView mFullName;
    ImageView mProfileView;

    private static final String USED_INTENT = "USED_INTENT";
    public static final String LOG_TAG = "AppAuthSample";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

        mGivenName = getView().findViewById(R.id.givenName);
        mFamilyName = getView().findViewById(R.id.familyName);
        mFullName = getView().findViewById(R.id.fullName);
        mProfileView = getView().findViewById(R.id.profileImage);

        settings = StoreBox.create(this.getActivity(), Settings.class);
        authorizationService = new AuthorizationService(view.getContext());
    }

    private void processGoogleAuthorization(View view) {
        googleButton.setClickable(false);
        enablePostAuthorizationFlows();

        AuthorizationServiceConfiguration serviceConfiguration = new AuthorizationServiceConfiguration(
                Uri.parse("https://accounts.google.com/o/oauth2/v2/auth") /* auth endpoint */,
                Uri.parse("https://www.googleapis.com/oauth2/v4/token") /* token endpoint */
        );
        String clientId = "289270915788-o0fpcfjdofn305bt1lpntlu37433mkpt.apps.googleusercontent.com";
        Uri redirectUri = Uri.parse("ru.tp.lingany.lingany:/oauth2callback");
        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
                serviceConfiguration,
                clientId,
                AuthorizationRequest.RESPONSE_TYPE_CODE,
                redirectUri
        );
        builder.setScopes("profile");

        AuthorizationRequest request = builder.build();

        String action = "com.google.codelabs.appauth.HANDLE_AUTHORIZATION_RESPONSE";
        Intent postAuthorizationIntent = new Intent(action);
        PendingIntent pendingIntent = PendingIntent.getActivity(view.getContext(), request.hashCode(), postAuthorizationIntent, 0);
        authorizationService.performAuthorizationRequest(request, pendingIntent);
    }

    private void enablePostAuthorizationFlows() {
        authState = restoreAuthState();
        if (authState != null && authState.isAuthorized()) {
            makeApiCall(authState);
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

    private void makeApiCall(@NonNull AuthState authState) {
        final Context context = this.getActivity();
        authState.performActionWithFreshTokens(authorizationService, new AuthState.AuthStateAction() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void execute(@Nullable String accessToken, @Nullable String idToken, @Nullable AuthorizationException exception) {
                new AsyncTask<String, Void, JSONObject>() {
                    @Override
                    protected JSONObject doInBackground(String... tokens) {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url("https://www.googleapis.com/oauth2/v3/userinfo")
                                .addHeader("Authorization", String.format("Bearer %s", tokens[0]))
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                            String jsonBody = response.body().string();
                            Log.i(LOG_TAG, String.format("User Info Response %s", jsonBody));
                            return new JSONObject(jsonBody);
                        } catch (Exception exception) {
                            Log.w(LOG_TAG, exception);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(JSONObject userInfo) {
                        if (userInfo != null) {
                            String fullName = userInfo.optString("name", null);
                            String givenName = userInfo.optString("given_name", null);
                            String familyName = userInfo.optString("family_name", null);
                            String imageUrl = userInfo.optString("picture", null);
                            if (!TextUtils.isEmpty(imageUrl)) {
                                Picasso.get().load(imageUrl).placeholder(R.drawable.ic_account_circle_black_48dp).into(mProfileView);
                            }
                            if (!TextUtils.isEmpty(fullName)) {
                                mFullName.setText(fullName);
                            }
                            if (!TextUtils.isEmpty(givenName)) {
                                mGivenName.setText(givenName);
                            }
                            if (!TextUtils.isEmpty(familyName)) {
                                mFamilyName.setText(familyName);
                            }

                            String message;
                            if (userInfo.has("error")) {
                                message = String.format("%s [%s]", context.getString(R.string.requestFailed), userInfo.optString("error_description", "No description"));
                            } else {
                                message = context.getString(R.string.requestComplete);
                            }
                            Snackbar.make(mProfileView, message, Snackbar.LENGTH_SHORT)
                                    .show();
                        }
                    }
                }.execute(accessToken);
            }
        });
        clearAuthState();
    }

    private void clearAuthState() {
        settings.setAuthIdentityString(null);
    }
}
