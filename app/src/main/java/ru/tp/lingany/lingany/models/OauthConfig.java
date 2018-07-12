package ru.tp.lingany.lingany.models;


public class OauthConfig {
    private final String authEndPoint = "https://accounts.google.com/o/oauth2/v2/auth";
    private final String tokenEndPoint = "https://www.googleapis.com/oauth2/v4/token";
    private final String clientId = "289270915788-o0fpcfjdofn305bt1lpntlu37433mkpt.apps.googleusercontent.com";
    private final String redirectUri = "ru.tp.lingany.lingany:/oauth2callback";
    private final String scope = "profile";

    public String getAuthEndPoint() {
        return authEndPoint;
    }

    public String getTokenEndPoint() {
        return tokenEndPoint;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getScope() {
        return scope;
    }
}
