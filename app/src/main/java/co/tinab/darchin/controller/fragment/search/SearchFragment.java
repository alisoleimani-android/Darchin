package co.tinab.darchin.controller.fragment.search;


import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.fragment.BaseFragment;
import co.tinab.darchin.controller.fragment.store.StoreSuggestionFragment;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.User;
import co.tinab.darchin.model.address.AreaSearch;
import co.tinab.darchin.model.address.City;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.SearchRequestHelper;
import co.tinab.darchin.model.network.request_helpers.StoreRequestHelper;
import co.tinab.darchin.model.network.resources.SearchResultResource;
import co.tinab.darchin.model.network.resources.StoreCollectionResource;
import co.tinab.darchin.model.store.Product;
import co.tinab.darchin.model.store.Store;
import co.tinab.darchin.view.component.EmptyView;
import co.tinab.darchin.view.component.LoadingView;
import co.tinab.darchin.view.toolbox.ButtonNormal;
import co.tinab.darchin.view.toolbox.MySnackbar;
import co.tinab.darchin.view.toolbox.TextViewLight;
import klogi.com.RtlViewPager;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends BaseFragment implements View.OnClickListener {
    private String string;
    private short type = -1;
    private AreaSearch area;

    private final short STRING = 0;
    private final short AREA = 1;

    private Typeface font;
    private TabLayout tabLayout;
    private RtlViewPager viewPager;
    private LinkedHashMap<String,Fragment> fragmentList = new LinkedHashMap<>();
    private EmptyView emptyView;
    private LoadingView loadingView;
    private ButtonNormal btnSuggest;

    public static SearchFragment newInstance(String string){
        SearchFragment searchFragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString("string",string);
        searchFragment.setArguments(args);
        return searchFragment;
    }

    public static SearchFragment newInstance(AreaSearch area){
        SearchFragment searchFragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putParcelable("area",area);
        searchFragment.setArguments(args);
        return searchFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("string")) {
                string = getArguments().getString("string");
                type = STRING;
            }

            if (getArguments().containsKey("area")) {
                area = getArguments().getParcelable("area");
                type = AREA;
            }
        }
        if (getContext() != null) {
            if (Locale.getDefault().getLanguage().equals("fa")) {
                font = Typeface.createFromAsset(getContext().getAssets(),"fonts/Normal.ttf");
            }else {
                font = Typeface.createFromAsset(getContext().getAssets(),"fonts/LatinNormal.ttf");
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tabs);

        // btn back
        view.findViewById(R.id.btn_back).setOnClickListener(this);

        emptyView = view.findViewById(R.id.empty_view);
        loadingView = view.findViewById(R.id.loading_view);

        btnSuggest = view.findViewById(R.id.btn_suggest);
        btnSuggest.setOnClickListener(this);

        TextViewLight txtTitle = view.findViewById(R.id.txt_title);
        if (type == STRING) {
            txtTitle.setText(getString(R.string.search_results));
        }

        if (type == AREA) {
            txtTitle.setText(getString(R.string.search_area_title,area.getValue()));
        }

        // get data if search field is messed up:
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (type == STRING) {
                    searchString();
                }

                if (type == AREA) {
                    searchArea();
                }
            }
        },500);

    }

    @Override
    public void onResume() {
        super.onResume();
        for (Fragment fragment : fragmentList.values()){
            fragment.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        for (Fragment fragment : fragmentList.values()){
            fragment.onPause();
        }
    }

    private void searchString(){
        City city = User.getInstance(getContext()).getCity(getContext());
        if (city != null) {
            if (loadingView != null) loadingView.show();

            int cityId = city.getCityId();
            SearchRequestHelper requestHelper = SearchRequestHelper.getInstance();
            requestHelper.search(cityId,string).enqueue(new MyCallback<SearchResultResource>() {
                @Override
                public void onRequestSuccess(Response<SearchResultResource> response) {
                    if (loadingView != null) loadingView.hide();

                    SearchResultResource resultResource = response.body();
                    if (FunctionHelper.isSuccess(getView(), resultResource) && resultResource.getResponse() != null && isAdded()) {

                        SearchResultResource.Response resultResponse = resultResource.getResponse();
                        if (resultResponse.getProducts() != null && resultResponse.getStores() != null) {

                            if (!resultResponse.getStores().isEmpty()) {
                                List<Store> stores = resultResponse.getStores();
                                fragmentList.put(getString(R.string.stores, stores.size()),
                                        SearchResultFragment.newRestaurantInstance(stores));
                            }

                            if (!resultResponse.getProducts().isEmpty()) {
                                List<Product> products = resultResponse.getProducts();
                                fragmentList.put(getString(R.string.products, products.size()),
                                        SearchResultFragment.newFoodInstance(products));
                            }
                            buildTabLayoutView(fragmentList);

                            if (resultResponse.getStores().isEmpty() &&
                                    resultResponse.getProducts().isEmpty()) {
                                btnSuggest.setVisibility(View.VISIBLE);
                            }else {
                                btnSuggest.setVisibility(View.GONE);
                            }

                        }else {
                            if (emptyView != null) emptyView.show();
                        }

                    }else {
                        if (isAdded()) MySnackbar.make(getView()
                                ,MySnackbar.Failure,R.string.request_failed).show();
                        if (emptyView != null) emptyView.show();
                    }
                }

                @Override
                public void onRequestFailed(@Nullable List<String> messages) {
                    if (loadingView != null) loadingView.hide();
                    if (emptyView != null) emptyView.show();
                    FunctionHelper.showMessages(getView(),messages);
                }

                @Override
                public void unAuthorizedDetected() {
                    if (loadingView != null) loadingView.hide();
                    if (getContext() instanceof MainActivity) ((MainActivity)getContext()).logout();
                }
            });
        }
    }

    private void searchArea(){
        if (loadingView != null) loadingView.show();

        StoreRequestHelper requestHelper = StoreRequestHelper.getInstance();
        requestHelper.getStoreServiceByLocation(area.getLocation()).enqueue(new MyCallback<StoreCollectionResource>() {
            @Override
            public void onRequestSuccess(Response<StoreCollectionResource> response) {
                if (loadingView != null) loadingView.hide();

                StoreCollectionResource resource = response.body();
                if (FunctionHelper.isSuccess(getView(), resource) && resource.getStores() != null && isAdded()) {
                    List<Store> stores = resource.getStores();
                    if (!stores.isEmpty()) {
                        fragmentList.put(getString(R.string.stores, stores.size()),
                                SearchResultFragment.newRestaurantInstance(stores));
                    }
                    buildTabLayoutView(fragmentList);

                }else {
                    if (isAdded()) MySnackbar.make(getView()
                            , MySnackbar.Failure,R.string.request_failed).show();
                    if (emptyView != null) emptyView.show();
                }
            }

            @Override
            public void onRequestFailed(@Nullable List<String> messages) {
                if (loadingView != null) loadingView.hide();
                if (emptyView != null) emptyView.show();
                FunctionHelper.showMessages(getView(),messages);
            }

            @Override
            public void unAuthorizedDetected() {
                if (loadingView != null) loadingView.hide();
                if (getContext() instanceof MainActivity) ((MainActivity)getContext()).logout();
            }
        });
    }

    private void buildTabLayoutView(LinkedHashMap<String,Fragment> fragmentList){
        if (!fragmentList.isEmpty() && isAdded()) {
            if (viewPager != null) {
                viewPager.setOffscreenPageLimit(fragmentList.size());
                setupViewPager(viewPager, fragmentList);
            }
            if (tabLayout != null) {
                tabLayout.setupWithViewPager(viewPager);
                changeTabsFont();
            }
        }else {
            if (emptyView != null) emptyView.show();
        }
    }

    //Setting font of Tab's Title
    private void changeTabsFont() {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(font);
                }
            }
        }
    }

    private void setupViewPager(ViewPager viewPager, LinkedHashMap<String,Fragment> fragmentList) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        //initialize fragments:
        for (String title : fragmentList.keySet()){
            adapter.addFragment(fragmentList.get(title),title);
        }

        //set adapter to viewpager
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;

            case R.id.btn_suggest:
                if (getActivity() != null) ((MainActivity)getActivity())
                        .pushFragment(StoreSuggestionFragment.newInstance());
                break;
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
