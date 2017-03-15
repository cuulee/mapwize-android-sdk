package io.mapwize.example;

import android.app.Application;

import io.mapwize.mapwize.MWZAccountManager;

public class ExampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MWZAccountManager.start(this, "1f04d780dc30b774c0c10f53e3c7d4ea"); // PASTE YOU API KEY HERE !!! This is a demo key. It is not allowed to use it for production. The key might change at any time without notice.
    }

}
