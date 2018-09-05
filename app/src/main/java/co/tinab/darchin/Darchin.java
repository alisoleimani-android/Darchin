package co.tinab.darchin;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.webkit.WebView;

import com.crashlytics.android.Crashlytics;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import co.ronash.pushe.Pushe;
import co.tinab.darchin.controller.tools.PicassoClientBuilder;

/**
 * Created by A.S.R on 1/17/2018.
 */

public class Darchin extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // this line will disable web chrome client changing localization
        try {
            new WebView(this).destroy();
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("webview")) {
                Crashlytics.log(e.getMessage());
            }
        }

        // Picasso
        picassoBuilder();

        //for Enable Notification
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .init();
        // clear notification shade
        OneSignal.clearOneSignalNotifications();

        // Pushe notification
        Pushe.initialize(getApplicationContext(),false);
    }

    private void picassoBuilder(){
        Picasso.Builder picassoBuilder =  new Picasso.Builder(this);
        picassoBuilder.downloader(new OkHttp3Downloader(PicassoClientBuilder.createClient(getApplicationContext())));
        Picasso picasso = picassoBuilder.build();
        Picasso.setSingletonInstance(picasso);
    }

    public static boolean isRtl(Context context){
        return context.getResources().getConfiguration()
                .getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }
}
