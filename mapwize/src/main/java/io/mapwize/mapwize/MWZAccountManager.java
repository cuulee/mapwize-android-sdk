package io.mapwize.mapwize;


import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Account manager will store account informations
 */
public class MWZAccountManager {

    private String mApiKey;
    private Context mContext;
    private String mServerUrl;

    private static MWZAccountManager sInstance = null;

    public static MWZAccountManager start(@NonNull Context ctx, @NonNull String apiKey) {
        if (sInstance == null) {
            sInstance = new MWZAccountManager(ctx, apiKey);
        }
        return sInstance;
    }

    public static MWZAccountManager start(@NonNull Context ctx, @NonNull String apiKey, @NonNull String serverUrl) {
        if (sInstance == null) {
            sInstance = new MWZAccountManager(ctx, apiKey, serverUrl);
        }
        return sInstance;
    }

    private MWZAccountManager(Context ctx, String apiKey) {
        this.mContext = ctx;
        this.mApiKey = apiKey;
        this.mServerUrl = "https://api.mapwize.io";
    }

    private MWZAccountManager(Context ctx, String apiKey, String serverUrl) {
        this.mContext = ctx;
        this.mApiKey = apiKey;
        this.mServerUrl = serverUrl;
    }

    static MWZAccountManager getInstance() {
        return sInstance;
    }

    String getApiKey() {
        return mApiKey;
    }

    Context getContext() {
        return mContext;
    }

    public String getServerUrl() {
        return mServerUrl;
    }
}
