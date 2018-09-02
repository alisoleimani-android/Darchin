package co.tinab.darchin.controller.fragment.order;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.tools.NonSwipeableViewPager;
import co.tinab.darchin.model.store.Cart;
import co.tinab.darchin.view.toolbox.MySnackbar;
import co.tinab.darchin.view.toolbox.TextViewLight;

/**
 * Created by A.S.R on 3/6/2018.
 */

public class TimeSelectionFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private Typeface font;
    private TabLayout tabLayout;
    private NonSwipeableViewPager viewPager;
    private Cart cart;
    private boolean isDelivery,isInside;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0: // today
                if (viewPager.getAdapter() != null) {
                    TimeFragment fragment = (TimeFragment)
                            ((ViewPagerAdapter)viewPager.getAdapter()).getItem(1);
                    fragment.notifyAdapter();
                }
                break;

            case 1: // tomorrow
                if (viewPager.getAdapter() != null) {
                    TimeFragment fragment = (TimeFragment)
                            ((ViewPagerAdapter)viewPager.getAdapter()).getItem(0);
                    fragment.notifyAdapter();
                }
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    public static TimeSelectionFragment newInstance(Cart cart, boolean isDelivery, boolean isInside){
        TimeSelectionFragment dialog = new TimeSelectionFragment();
        Bundle args = new Bundle();
        args.putParcelable("cart",cart);
        args.putBoolean("isDelivery",isDelivery);
        args.putBoolean("isInside",isInside);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cart = getArguments().getParcelable("cart");
            isDelivery = getArguments().getBoolean("isDelivery");
            isInside = getArguments().getBoolean("isInside");
        }

        if (getContext() != null) {
            font = Typeface.createFromAsset(getContext().getAssets(),"fonts/Light.ttf");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_time_selection,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextViewLight txtTitle = view.findViewById(R.id.txt_title);
        txtTitle.setText(getString(R.string.order_deliver_time));

        viewPager = view.findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(this);
        tabLayout = view.findViewById(R.id.tabs);
        view.findViewById(R.id.btn_back).setOnClickListener(this);
        view.findViewById(R.id.btn_choose).setOnClickListener(this);
        buildTabLayoutView();

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event ) {
                if( keyCode == KeyEvent.KEYCODE_BACK ) {
                    onBackPressed();
                    return true;
                }
                return false;
            }
        } );
    }

    private void buildTabLayoutView(){
        if(viewPager!=null) {
            viewPager.setOffscreenPageLimit(2);
            setupViewPager(viewPager);
        }
        if(tabLayout!=null){
            tabLayout.setupWithViewPager(viewPager);
            changeTabsFont();
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
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        //initialize fragments:
        adapter.addFragment(TimeFragment.newInstance("today",cart,isDelivery,isInside),getString(R.string.today));
        adapter.addFragment(TimeFragment.newInstance("tomorrow",cart,isDelivery,isInside),getString(R.string.tomorrow));

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

    private void onBackPressed(){
        cart.setSelectedPeriod(null);
        if (getActivity() != null) ((MainActivity)getActivity())
                .popFragment(null,DeliveryTypeFragment.class.getName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;

            case R.id.btn_choose:
                if (cart.getSelectedPeriod() == null) {
                    MySnackbar.make(getView(),MySnackbar.Alert,R.string.order_time_not_selected).show();
                }else {
                    if (getActivity() != null) ((MainActivity)getActivity())
                            .popFragment(null,DeliveryTypeFragment.class.getName());
                }
                break;
        }
    }
}
