package co.tinab.darchin.controller.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.fragment.authentication.LoginOrRegisterFragment;
import co.tinab.darchin.controller.fragment.other.AppIntroFragment;
import co.tinab.darchin.controller.fragment.other.ServicesFragment;
import co.tinab.darchin.controller.fragment.other.SupportFragment;
import co.tinab.darchin.controller.fragment.search.SearchFragment;
import co.tinab.darchin.controller.fragment.store.StoreFragment;
import co.tinab.darchin.controller.fragment.store.StoreSuggestionFragment;
import co.tinab.darchin.controller.fragment.user.LanguageSelectionFragment;
import co.tinab.darchin.controller.fragment.user.ProfileFragment;
import co.tinab.darchin.controller.fragment.user.address.CitySelectionDialog;
import co.tinab.darchin.controller.fragment.user.credit.CreditFragment;
import co.tinab.darchin.controller.fragment.user.credit.FreeCreditFragment;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.Favorite;
import co.tinab.darchin.model.Setting;
import co.tinab.darchin.model.User;
import co.tinab.darchin.model.address.AreaSearch;
import co.tinab.darchin.model.address.City;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.AccountRequestHelper;
import co.tinab.darchin.model.network.request_helpers.SettingsRequestHelper;
import co.tinab.darchin.model.network.request_helpers.StoreRequestHelper;
import co.tinab.darchin.model.network.resources.FavoriteResource;
import co.tinab.darchin.model.network.resources.SettingsResource;
import co.tinab.darchin.model.network.resources.StoreResource;
import co.tinab.darchin.model.network.resources.UserResource;
import co.tinab.darchin.model.store.Product;
import co.tinab.darchin.model.store.Store;
import co.tinab.darchin.view.component.EmptyView;
import co.tinab.darchin.view.component.LoadingView;
import co.tinab.darchin.view.component.SearchResultView;
import co.tinab.darchin.view.component.SectionView;
import co.tinab.darchin.view.dialog.ForceUpdateDialog;
import co.tinab.darchin.view.dialog.QuestionDialog;
import co.tinab.darchin.view.dialog.WaitingDialog;
import co.tinab.darchin.view.navigation_drawer.DrawerFragment;
import co.tinab.darchin.view.toolbox.EditTextLight;
import co.tinab.darchin.view.toolbox.MySnackbar;
import co.tinab.darchin.view.toolbox.TextViewNormal;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseFragment implements DrawerFragment.FragmentDrawerListener, View.OnClickListener, SectionView.RequestCompleteListener, SearchResultView.ResultClickListener, TextView.OnEditorActionListener, SwipeRefreshLayout.OnRefreshListener {
    public DrawerFragment drawerFragment;
    private EditTextLight txtInputSearch;
    private SectionView sectionViewTop;
    private TextViewNormal txtCity;
    private EmptyView emptyView;
    private LoadingView loadingView;
    private WaitingDialog waitingDialog;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static MainFragment newInstance(){
        return new MainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // toolbar
        Toolbar mToolbar = view.findViewById(R.id.container_toolbar).findViewById(R.id.toolbar);
        // set logo
        setLogo(mToolbar);

        // init drawer frg
        drawerFragment = (DrawerFragment)
                getChildFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) view.findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // swipe to refresh layout:
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);

        sectionViewTop = view.findViewById(R.id.container_section_top);
        sectionViewTop.setOnRequestCompleteListener(this);

        txtInputSearch = view.findViewById(R.id.txt_input_search);
        txtInputSearch.setOnEditorActionListener(this);

        // waiting dialog:
        if (getContext() != null) waitingDialog = new WaitingDialog(getContext());

        // if city is empty, this shows message to user
        emptyView = view.findViewById(R.id.empty_view);

        // loading view:
        loadingView = view.findViewById(R.id.loading_view);

        // city
        txtCity = view.findViewById(R.id.txt_city);
        City city = User.getInstance(getContext()).getCity(getContext());
        if (city != null) {
            txtCity.setText(city.getName());
        }else {
            txtCity.setText(R.string.which_city_do_you_live);
        }
        txtCity.setOnClickListener(this);

        view.findViewById(R.id.btn_search).setOnClickListener(this);
        view.findViewById(R.id.btn_help).setOnClickListener(this);

        // Search Result View:
        SearchResultView searchResultView = view.findViewById(R.id.search_result_view);
        searchResultView.addTo(txtInputSearch);
        searchResultView.setOnResultClickListener(this);

        // get favorites:
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getContext() != null)
                    if (User.getInstance(getContext()).hasLoggedIn(getContext())) getFavoriteList();

                // get Sections from server
                loadingView.show();
                getSections();

                // get settings
                getSettings(false);

            }
        },500);
    }

    private void getSections(){
        City city = User.getInstance(getContext()).getCity(getContext());
        if (city != null) {
            if (FunctionHelper.isConnected(getContext())) {
                sectionViewTop.requestData("home","top");
            }else {
                sectionViewTop.bind(User.getInstance(getContext()).getSections(getContext(),"home","top"));
            }
        }else {
            emptyView.show();
            loadingView.hide();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        txtInputSearch.setText("");

        // if user comes first time, then goto app intro
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    User user = User.getInstance(getContext());
                    if (!user.hasSelectedLanguage(getContext())) {
                        ((MainActivity)getActivity()).pushFragment(LanguageSelectionFragment.newInstance());

                    }else if (!user.hasAppIntro(getContext()) && user.hasSelectedLanguage(getContext())) {
                        ((MainActivity)getActivity()).pushFragment(AppIntroFragment.newInstance());
                    }
                }
            }
        },500);

        // notify drawer to update
        drawerFragment.notifyDrawer();

        // get account info
        if (getContext() != null)
            if (User.getInstance(getContext()).hasLoggedIn(getContext())) getAccountInfo();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        switch (position){
            case 0:
                if (getActivity() != null) {
                    if (!User.getInstance(getContext()).hasLoggedIn(getActivity())) {
                        String tag = MainFragment.class.getName();
                        ((MainActivity)getActivity()).pushFragment(LoginOrRegisterFragment.newInstance(tag));

                    }else {
                        ((MainActivity)getActivity())
                                .pushFragment(ProfileFragment.newInstance(ProfileFragment.ORDER_PAGE));
                    }
                }
                break;

            case 1: // favorite
                if (getActivity() != null) ((MainActivity)getActivity())
                        .pushFragment(ProfileFragment.newInstance(ProfileFragment.FAVORITE_PAGE));
                break;

            case 2: // profile
                if (getActivity() != null) ((MainActivity)getActivity())
                        .pushFragment(ProfileFragment.newInstance(ProfileFragment.SETTING_PAGE));
                break;

            case 3: // charge account
                if (getActivity() != null) ((MainActivity)getActivity())
                        .pushFragment(CreditFragment.newInstance());
                break;

            case 4: // share with friends
                if (getActivity() != null) ((MainActivity)getActivity())
                        .pushFragment(FreeCreditFragment.newInstance());
                break;

            case 5: // register store
                if (getActivity() != null) ((MainActivity)getActivity())
                        .pushFragment(StoreSuggestionFragment.newInstance());
                break;

            case 6: // package services
                if (getActivity() != null) ((MainActivity)getActivity())
                        .pushFragment(ServicesFragment.newInstance());
                break;

            case 7: // support
                if (Setting.getSettings(getContext()).isEmpty()) {
                    getSettings(true);
                }else {
                    if (getActivity() != null) ((MainActivity)getActivity())
                            .pushFragment(SupportFragment.newInstance());
                }
                break;
        }
    }

    private void getSettings(final boolean gotoSupportPage){
        if (waitingDialog != null && gotoSupportPage) waitingDialog.show();

        SettingsRequestHelper requestHelper = SettingsRequestHelper.getInstance();
        requestHelper.getSettings().enqueue(new MyCallback<SettingsResource>() {
            @Override
            public void onRequestSuccess(Response<SettingsResource> response) {
                if (waitingDialog != null && gotoSupportPage) waitingDialog.dismiss();

                SettingsResource resource = response.body();
                if (resource != null && FunctionHelper.isSuccess(getView(), resource)
                        && resource.getSettings() != null) {
                    // save settings to db
                    Setting.setSettings(getContext(),resource.getSettings());

                    // check app update when settings request has done
                    checkAppUpdate();

                    // goto support page
                    if (getActivity() != null && gotoSupportPage) ((MainActivity)getActivity())
                            .pushFragment(SupportFragment.newInstance());

                }
            }

            @Override
            public void onRequestFailed(@Nullable List<String> messages) {
                if (waitingDialog != null && gotoSupportPage) waitingDialog.dismiss();
                FunctionHelper.showMessages(getView(),messages);
            }

            @Override
            public void unAuthorizedDetected() {
                if (waitingDialog != null && gotoSupportPage) waitingDialog.dismiss();
                if (getActivity() != null) ((MainActivity)getActivity()).logout();
            }
        });
    }

    private void checkAppUpdate(){
        if (getContext() != null) {
            try {
                Map<String, String> map = new LinkedHashMap<>(Setting.getSettings(getContext()));
                if (map.containsKey("last_stable_version") && map.containsKey("last_version")) {
                    int lastVersion = Integer.parseInt(map.get("last_version"));
                    int lastStableVersion = Integer.parseInt(map.get("last_stable_version"));

                    PackageInfo pInfo = getContext().getPackageManager()
                            .getPackageInfo(getContext().getPackageName(), 0);
                    int versionCode = pInfo.versionCode;

                    // version control
                    if (versionCode < lastStableVersion) {
                        openForceUpdateDialog();

                    } else if (versionCode < lastVersion) {
                        openOptUpdateDialog();

                    }

                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void openForceUpdateDialog(){
        ForceUpdateDialog dialog = ForceUpdateDialog.newInstance();
        dialog.setListener(new ForceUpdateDialog.ClickListener() {
            @Override
            public void onBtnClicked(DialogFragment dialog) {
                Uri uri = Uri.parse(Constant.CafeBazaarLink);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(uri);
                if (getActivity() != null) getActivity().startActivity(intent);

                // close dialog
                dialog.dismiss();

                // close the app
                if (getActivity() != null) getActivity().finish();
            }
        });
        dialog.show(getChildFragmentManager(),"ForceUpdateDialog");
    }

    private void openOptUpdateDialog(){
        String desc = getString(R.string.opt_update_desc);
        String yes = getString(R.string.yes);
        String no = getString(R.string.no);

        QuestionDialog dialog = QuestionDialog.newInstance(yes,no,desc);
        dialog.setListener(new QuestionDialog.ClickListener() {
            @Override
            public void onPositiveBtnClicked(DialogFragment dialogFragment) {
                Uri uri = Uri.parse(Constant.CafeBazaarLink);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(uri);
                if (getActivity() != null) getActivity().startActivity(intent);

                // close dialog
                dialogFragment.dismiss();
            }

            @Override
            public void onNegativeBtnClicked(DialogFragment dialogFragment) {
                dialogFragment.dismiss();
            }
        });
        dialog.show(getChildFragmentManager(),"QuestionDialog");
    }

    private void setLogo(Toolbar toolbar){
        ImageView imgLogo = toolbar.findViewById(R.id.img_logo);
        String language = Locale.getDefault().getLanguage();
        int resource;
        if (language.equals("fa")) {
            resource = R.drawable.persian_logo;
        }else {
            resource = R.drawable.latin_logo;
        }
        Picasso.with(getContext())
                .load(resource)
                .into(imgLogo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_search:
                String phrase = txtInputSearch.getText().toString();
                if (!phrase.isEmpty()) {
                    if (getActivity() != null) ((MainActivity)getActivity())
                            .pushFragment(SearchFragment.newInstance(phrase));
                }
                break;

            case R.id.txt_city:
                // open city selection dialog
                CitySelectionDialog dialog = new CitySelectionDialog(v.getContext(),R.style.MyDialogTheme);
                dialog.setListener(new CitySelectionDialog.CitySelectionListener() {
                    @Override
                    public void onCitySelected(City city) {
                        emptyView.hide();
                        User.getInstance(getContext()).setCity(getContext(),city);

                        City userCity = User.getInstance(getContext()).getCity(getContext());
                        if (userCity != null) {
                            txtCity.setText(userCity.getName());

                            // get Sections from server
                            loadingView.show();
                            getSections();
                        }
                    }
                });
                dialog.show();
                break;

            case R.id.btn_help:
                if (getActivity() != null) ((MainActivity)getActivity())
                        .pushFragment(AppIntroFragment.newInstance());
                break;
        }
    }

    private void getAccountInfo(){
        AccountRequestHelper requestHandler = AccountRequestHelper.getInstance();
        requestHandler.getAccountInfo(User.getToken(getContext())).enqueue(new MyCallback<UserResource>() {
            @Override
            public void onRequestSuccess(Response<UserResource> response) {
                UserResource userResponse = response.body();
                if (FunctionHelper.isSuccess(null,userResponse) && userResponse.getUser() != null) {
                    // save user
                    User.saveInstance(getContext(),userResponse.getUser());

                    // notify drawer:
                    drawerFragment.notifyDrawer();
                }
            }

            @Override
            public void onRequestFailed(@Nullable List<String> messages) {}

            @Override
            public void unAuthorizedDetected() {
                if (getActivity() != null) ((MainActivity)getActivity()).logout();
            }
        });
    }

    private void getFavoriteList(){
        AccountRequestHelper requestHandler = AccountRequestHelper.getInstance();
        requestHandler.getFavoriteList(User.getToken(getContext())).enqueue(new MyCallback<FavoriteResource>() {
            @Override
            public void onRequestSuccess(Response<FavoriteResource> response) {
                FavoriteResource favoriteResponse = response.body();
                if (FunctionHelper.isSuccess(null,favoriteResponse) && favoriteResponse.getFavoriteList() != null) {
                    List<Store> storeList = new ArrayList<>();
                    for (Favorite favorite : favoriteResponse.getFavoriteList()){
                        storeList.add(favorite.getStore());
                    }
                    User.getInstance(getContext()).setFavoriteList(storeList);
                }
            }

            @Override
            public void onRequestFailed(@Nullable List<String> messages) {}

            @Override
            public void unAuthorizedDetected() {
                if (getActivity() != null) ((MainActivity)getActivity()).logout();
            }
        });
    }

    @Override
    public void onRequestCompleted() {
        loadingView.hide();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRequestFailed() {
        loadingView.hide();
        swipeRefreshLayout.setRefreshing(false);
    }


    // methods for search result view
    @Override
    public void onStoreClicked(Store store) {
        if (store != null) openStore(store.getStoreId());
    }

    @Override
    public void onProductClicked(Store store, Product product) {
        if (getActivity() != null) ((MainActivity)getActivity())
                .pushFragment(StoreFragment.newInstance(store,product));
    }

    @Override
    public void onAreaClicked(AreaSearch area) {
        if (getActivity() != null) ((MainActivity)getActivity())
                .pushFragment(SearchFragment.newInstance(area));
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_SEARCH){
            // close keypad:
            if (getActivity() != null) {
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
                String phrase = txtInputSearch.getText().toString();
                if (!phrase.isEmpty()) {
                    ((MainActivity)getActivity()).pushFragment(SearchFragment.newInstance(phrase));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void onRefresh() {
        getSections();
    }

    // open store:
    private void openStore(int storeId){
        if (waitingDialog != null) waitingDialog.show();

        StoreRequestHelper requestHelper = StoreRequestHelper.getInstance();
        requestHelper.getStore(storeId).enqueue(new MyCallback<StoreResource>() {
            @Override
            public void onRequestSuccess(Response<StoreResource> response) {
                if (waitingDialog != null) waitingDialog.dismiss();

                StoreResource storeResource = response.body();
                if (FunctionHelper.isSuccess(getView(), storeResource) && storeResource.getStore() != null) {
                    if (getActivity() != null) ((MainActivity)getActivity())
                            .pushFragment(StoreFragment.newInstance(storeResource.getStore()));

                }else {
                    MySnackbar.make(getView(),MySnackbar.Failure,R.string.request_failed).show();
                }
            }

            @Override
            public void onRequestFailed(@Nullable List<String> messages) {
                if (waitingDialog != null) waitingDialog.dismiss();
                FunctionHelper.showMessages(getView(),messages);
            }

            @Override
            public void unAuthorizedDetected() {
                if (waitingDialog != null) waitingDialog.dismiss();
                if (getActivity() != null) ((MainActivity)getActivity()).logout();
            }
        });
    }
}
