package co.tinab.darchin.controller.tools;

import android.content.Context;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by A.S.R on 1/17/2018.
 */

public class PicassoClientBuilder {
    public static OkHttpClient createClient(Context context) {
        File httpCacheDirectory = new File(context.getCacheDir(), "picasso-cache");
        Cache cache = new Cache(httpCacheDirectory, 200 * 1024 * 1024);

        return new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .followRedirects(false)
                .followSslRedirects(false)
                .retryOnConnectionFailure(true)
                .cache(cache)
                .build();
    }
}
