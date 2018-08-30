package co.tinab.darchin.controller.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.Nullable;
import android.view.View;

import com.crashlytics.android.Crashlytics;

import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.fragment.MainFragment;
import co.tinab.darchin.controller.fragment.other.SupportFragment;
import co.tinab.darchin.controller.fragment.other.WebViewFragment;
import co.tinab.darchin.controller.fragment.store.StoreSuggestionFragment;
import co.tinab.darchin.controller.fragment.user.ProfileFragment;
import co.tinab.darchin.controller.fragment.user.credit.CreditFragment;
import co.tinab.darchin.controller.fragment.user.credit.FreeCreditFragment;
import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.network.resources.BasicResource;
import co.tinab.darchin.view.toolbox.MySnackbar;

/**
 * Created by A.S.R on 4/2/2018.
 */

public class FunctionHelper {
    public static void showMessages(View view, List<String> messages){
        try {
            if (view != null) {
                if (messages != null) {
                    if (!messages.isEmpty()) {
                        int lastIndex = messages.size()-1;
                        MySnackbar.make(view, MySnackbar.Failure,messages.get(lastIndex)).show();
                    }else {
                        MySnackbar.make(view, MySnackbar.Failure, R.string.request_failed).show();
                    }
                }else {
                    if (isConnected(view.getContext())) {
                        MySnackbar.make(view, MySnackbar.Failure, R.string.request_failed).show();
                    }else {
                        MySnackbar.make(view, MySnackbar.Alert, R.string.please_connect_to_network).show();
                    }
                }
            }
        }catch (Exception e){
            Crashlytics.log(e.getMessage());
        }
    }

    public static boolean isSuccess(@Nullable View view, BasicResource basicResource){
        if (basicResource.getStatus() != null) {
            if (basicResource.getStatus().equals(Constant.SUCCESS)) {
                if (basicResource.getMessage() != null && view != null) {
                    MySnackbar.make(view, MySnackbar.Success, basicResource.getMessage()).show();
                }
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }

    }

    // this method check user has connectivity or not
    public static boolean isConnected(Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm != null && cm.getActiveNetworkInfo() != null;
        }else {
            return false;
        }
    }

    // for sections
    public static void gotoLink(Context context, String linkType, String link,String name){
        if (context != null && context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;

            if (linkType.equals("web")) {
                if (isConnected(context)) mainActivity.pushFragment(
                        WebViewFragment.newInstance(link.concat("?hide"),name));
            }

            if (linkType.equals("page")) {
                switch (link){
                    case "home":
                        mainActivity.pushFragment(MainFragment.newInstance());
                        break;

                    case "charge":
                        mainActivity.pushFragment(CreditFragment.newInstance());
                        break;

                    case "invite":
                        mainActivity.pushFragment(FreeCreditFragment.newInstance());
                        break;

                    case "suggest":
                        mainActivity.pushFragment(StoreSuggestionFragment.newInstance());
                        break;

                    case "orders":
                        mainActivity.pushFragment(ProfileFragment.newInstance(ProfileFragment.ORDER_PAGE));
                        break;

                    case "favorite":
                        mainActivity.pushFragment(ProfileFragment.newInstance(ProfileFragment.FAVORITE_PAGE));
                        break;

                    case "settings":
                        mainActivity.pushFragment(ProfileFragment.newInstance(ProfileFragment.SETTING_PAGE));
                        break;

                    case "support":
                        mainActivity.pushFragment(SupportFragment.newInstance());
                        break;

                }
            }
        }
    }
}
