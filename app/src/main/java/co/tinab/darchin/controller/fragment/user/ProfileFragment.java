package co.tinab.darchin.controller.fragment.user;


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
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.fragment.BaseFragment;
import co.tinab.darchin.controller.fragment.user.order.OrdersFragment;
import co.tinab.darchin.controller.fragment.user.order.TransactionFragment;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.User;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.AccountRequestHelper;
import co.tinab.darchin.model.network.resources.UserResource;
import co.tinab.darchin.view.toolbox.MoneyTextView;
import klogi.com.RtlViewPager;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment implements View.OnClickListener {
    private Typeface font;
    private TabLayout tabLayout;
    private RtlViewPager viewPager;
    private ViewPagerAdapter adapter;
    private short pageSelectionIndex = 0;
    public static final short FAVORITE_PAGE = 2;
    public static final short ORDER_PAGE = 1;
    public static final short SETTING_PAGE = 0;

    private MoneyTextView txtCredit;
    private TextView txtName;

    public static ProfileFragment newInstance(short pageSelectionIndex){
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putShort("pageSelectionIndex",pageSelectionIndex);
        profileFragment.setArguments(args);
        return profileFragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageSelectionIndex = getArguments().getShort("pageSelectionIndex");
        }
        if (getContext() != null) {
            if (Locale.getDefault().getLanguage().equals("fa")) {
                font = Typeface.createFromAsset(getContext().getAssets(),"fonts/Bold.ttf");
            }else {
                font = Typeface.createFromAsset(getContext().getAssets(),"fonts/LatinBold.ttf");
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tabs);
        Toolbar toolbar = view.findViewById(R.id.container_toolbar).findViewById(R.id.toolbar);

        txtName = view.findViewById(R.id.txt_name);
        txtCredit = view.findViewById(R.id.txt_credit);

        // btn back
        ImageButton btnBack = toolbar.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

        // title
        TextView txtTitle = toolbar.findViewById(R.id.txt_title);
        txtTitle.setText(R.string.nav_item_profile);

        // build tabs
        buildTabLayoutView();
    }

    private void buildTabLayoutView(){
        if(viewPager!=null) {
            viewPager.setOffscreenPageLimit(4);
            setupViewPager(viewPager);
        }
        if(tabLayout!=null){
            tabLayout.setupWithViewPager(viewPager);
            changeTabsFont();
            TabLayout.Tab tab = tabLayout.getTabAt(pageSelectionIndex);
            if (tab != null) tab.select();
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

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());

        //initialize fragments:
        adapter.addFragment(SettingFragment.newInstance(),getString(R.string.setting));
        adapter.addFragment(OrdersFragment.newInstance(),getString(R.string.nav_item_orders));
        adapter.addFragment(FavoriteFragment.newInstance(),getString(R.string.nav_item_favorite));
        adapter.addFragment(TransactionFragment.newInstance(),getString(R.string.accounting));

        //set adapter to viewpager
        viewPager.setAdapter(adapter);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // request to get user credit account
        getAccountInfo();

        // on Resume all of the children:
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (Fragment fragment : adapter.mFragmentList){
                    fragment.onResume();
                }
            }
        },500);
    }

    public void getAccountInfo(){
        AccountRequestHelper requestHandler = AccountRequestHelper.getInstance();
        requestHandler.getAccountInfo(User.getToken(getContext())).enqueue(new MyCallback<UserResource>() {
            @Override
            public void onRequestSuccess(Response<UserResource> response) {
                UserResource userResponse = response.body();
                if (FunctionHelper.isSuccess(null,userResponse) && userResponse.getUser() != null) {
                    // save user
                    User.saveInstance(getContext(),userResponse.getUser());
                    txtName.setText(User.getInstance(getContext()).getName());
                    txtCredit.setText(User.getInstance(getContext()).getCredit());
                }
            }

            @Override
            public void onRequestFailed(@Nullable List<String> messages) {
                txtName.setText(User.getInstance(getContext()).getName());
                txtCredit.setText(User.getInstance(getContext()).getCredit());
            }

            @Override
            public void unAuthorizedDetected() {
                if (getActivity() != null) ((MainActivity)getActivity()).logout();
            }
        });
    }
}
