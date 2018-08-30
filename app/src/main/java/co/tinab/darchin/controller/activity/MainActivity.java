package co.tinab.darchin.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.crashlytics.android.Crashlytics;

import java.util.List;

import co.tinab.darchin.Darchin;
import co.tinab.darchin.R;
import co.tinab.darchin.controller.fragment.MainFragment;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.User;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.AuthRequestHelper;
import co.tinab.darchin.model.network.resources.BasicResource;
import co.tinab.darchin.view.dialog.WaitingDialog;
import co.tinab.darchin.view.toolbox.MySnackbar;
import retrofit2.Response;

public class MainActivity extends BaseActivity {
    private FragmentManager fragmentManager;
    // press btn back again to exit
    private boolean pressBackBtnAgain = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init fragment manager
        fragmentManager = getSupportFragmentManager();
        // add main fragment
        pushFragment(MainFragment.newInstance());
    }

    private void showConnectionState(){
        if (!FunctionHelper.isConnected(getApplicationContext())) {
            Snackbar snackbar = MySnackbar.make(findViewById(R.id.container_fragment)
                    , MySnackbar.Alert,R.string.please_connect_to_network);
            snackbar.show();
        }
    }

    // method for adding fragments into main activity
    public void pushFragment(Fragment fragment){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        try {
            if(fragment != null) {
                String tag = fragment.getClass().getName();
                boolean poppedFragment = fragmentManager.popBackStackImmediate(tag,0);
                if (!poppedFragment && fragmentManager.findFragmentByTag(tag) == null) {
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    if (Darchin.isRtl(this)) {
                        ft.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right
                                ,R.anim.enter_from_right,R.anim.exit_to_left);
                    }else {
                        ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left
                                ,R.anim.enter_from_left,R.anim.exit_to_right);
                    }

                    if (fragmentManager.getBackStackEntryCount() >= 1) {
                        int lastFragmentIndex = fragmentManager.getBackStackEntryCount()-1;
                        Fragment lastFragment = fragmentManager.getFragments().get(lastFragmentIndex);
                        lastFragment.onPause();
                        ft.hide(lastFragment);
                    }

                    ft.add(R.id.container_fragment,fragment,tag);
                    ft.addToBackStack(tag);
                    ft.commitAllowingStateLoss();

                } else if (poppedFragment) {
                    fragmentManager.getFragments().get(fragmentManager.getBackStackEntryCount()-1).onResume();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
        }
    }

    public void pushFragment(Fragment fragment,String targetFragmentTag,int requestCode){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        try {
            if(fragment != null) {
                String tag = fragment.getClass().getName();
                boolean poppedFragment = fragmentManager.popBackStackImmediate(tag,0);
                if (!poppedFragment && fragmentManager.findFragmentByTag(tag) == null) {
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    if (Darchin.isRtl(this)) {
                        ft.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right
                                ,R.anim.enter_from_right,R.anim.exit_to_left);
                    }else {
                        ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left
                                ,R.anim.enter_from_left,R.anim.exit_to_right);
                    }

                    if (fragmentManager.getBackStackEntryCount() >= 1) {
                        int lastFragmentIndex = fragmentManager.getBackStackEntryCount()-1;
                        Fragment lastFragment = fragmentManager.getFragments().get(lastFragmentIndex);

                        // find target fragment
                        Fragment targetFragment = fragmentManager.findFragmentByTag(targetFragmentTag);

                        // for fragment result:
                        fragment.setTargetFragment(targetFragment,requestCode);

                        lastFragment.onPause();
                        ft.hide(lastFragment);
                    }

                    ft.add(R.id.container_fragment,fragment,tag);
                    ft.addToBackStack(tag);
                    ft.commitAllowingStateLoss();

                } else if (poppedFragment) {
                    fragmentManager.getFragments().get(fragmentManager.getBackStackEntryCount()-1).onResume();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {}

    @Override
    protected void onResume() {
        super.onResume();

        // show connection state:
        showConnectionState();
    }

    private void popFragment(){
        fragmentManager.getFragments().get(fragmentManager.getBackStackEntryCount()-1).onPause();
        super.onBackPressed();
        fragmentManager.getFragments().get(fragmentManager.getBackStackEntryCount()-1).onResume();
    }

    public void popFragment(Intent intent,String targetFragmentTag){
        Fragment lastFragment = fragmentManager.getFragments().get(fragmentManager.getBackStackEntryCount()-1);
        lastFragment.onPause();
        if (lastFragment.getTargetFragment() != null) {
            lastFragment.getTargetFragment().onActivityResult(lastFragment.getTargetRequestCode(),RESULT_OK,intent);
            boolean poppedFragment = fragmentManager.popBackStackImmediate(targetFragmentTag,0);
            if (poppedFragment) {
                fragmentManager.getFragments().get(fragmentManager.getBackStackEntryCount()-1).onResume();
            }
        }
    }

    public void popFragment(String targetFragmentTag){
        Fragment lastFragment = fragmentManager.getFragments().get(fragmentManager.getBackStackEntryCount()-1);
        lastFragment.onPause();
        boolean poppedFragment = fragmentManager.popBackStackImmediate(targetFragmentTag,0);
        if (poppedFragment) {
            fragmentManager.getFragments().get(fragmentManager.getBackStackEntryCount()-1).onResume();
        }
    }

    public Fragment getLastFragment(){
        return fragmentManager.getFragments().get(fragmentManager.getBackStackEntryCount()-1);
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 1) {
            popFragment();
        }else {
            MainFragment mainFragment = (MainFragment) fragmentManager
                    .findFragmentByTag(MainFragment.class.getName());
            if (mainFragment != null) {
                if (mainFragment.drawerFragment.isDrawerOpen()) {
                    mainFragment.drawerFragment.closeDrawer();
                }else {
                    exit();
                }
            }else {
                exit();
            }
        }
    }

    // logout user
    public void logout(){
        final WaitingDialog waitingDialog = new WaitingDialog(this);
        waitingDialog.show();

        AuthRequestHelper requestHandler = AuthRequestHelper.getInstance();
        requestHandler.logout(User.getToken(getBaseContext())).enqueue(new MyCallback<BasicResource>() {
            @Override
            public void onRequestSuccess(Response<BasicResource> response) {
                waitingDialog.dismiss();

                BasicResource basicResource = response.body();
                if (basicResource.getMessage() != null) {
                    MySnackbar.make(findViewById(R.id.container_fragment)
                            ,MySnackbar.Failure, basicResource.getMessage()).show();
                }
                // logout user from app
                logoutFromApp();
            }

            @Override
            public void onRequestFailed(@Nullable List<String> messages) {
                waitingDialog.dismiss();
                // logout user from app
                logoutFromApp();
            }

            @Override
            public void unAuthorizedDetected() {
                waitingDialog.dismiss();
                // logout user from app
                logoutFromApp();
            }
        });
    }

    private void logoutFromApp(){
        // clear user
        User.getInstance(getBaseContext()).delete(getBaseContext());

        // pop to main page
        popFragment(MainFragment.class.getName());

        // notify drawer
        MainFragment mainFragment = (MainFragment) fragmentManager
                .findFragmentByTag(MainFragment.class.getName());
        if (mainFragment != null) {
            mainFragment.drawerFragment.notifyDrawer();
        }
    }

    private void exit(){
        if (pressBackBtnAgain) {
            finish();
            return;
        }
        this.pressBackBtnAgain = true;
        Snackbar snackbar = MySnackbar.make(findViewById(R.id.container_fragment)
                , MySnackbar.Info,R.string.press_back_btn_again);
        snackbar.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pressBackBtnAgain = false;
            }
        }, 2000);
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            boolean isActive = cm.getActiveNetworkInfo() != null;
            if (!isActive) {
                Snackbar snackbar = MySnackbar.make(findViewById(R.id.container_fragment)
                        ,MySnackbar.Alert,R.string.please_connect_to_network);
                snackbar.show();
            }
            return isActive;
        } else {
            return false;
        }
    }
}
