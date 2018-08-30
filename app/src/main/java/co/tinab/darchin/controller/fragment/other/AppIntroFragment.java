package co.tinab.darchin.controller.fragment.other;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import co.tinab.darchin.Darchin;
import co.tinab.darchin.R;
import co.tinab.darchin.controller.tools.AppIntroSlideFragment;
import co.tinab.darchin.model.User;
import co.tinab.darchin.view.toolbox.TextViewLight;
import klogi.com.RtlViewPager;
import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppIntroFragment extends Fragment implements View.OnClickListener {
    private RtlViewPager viewPager;
    private CircleIndicator indicator;

    public static AppIntroFragment newInstance(){
        return new AppIntroFragment();
    }

    public AppIntroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_app_intro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextViewLight txtTitle = view.findViewById(R.id.txt_title);
        txtTitle.setText(getString(R.string.how_to_use_darchin));

        view.findViewById(R.id.btn_back).setOnClickListener(this);

        viewPager = view.findViewById(R.id.viewpager);
        indicator = view.findViewById(R.id.indicator);
        buildView();
    }

    private void buildView(){
        if(viewPager!=null) {
            viewPager.setOffscreenPageLimit(4);
            setupViewPager(viewPager);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        //initialize fragments:
        if (Darchin.isRtl(getContext())) {
            adapter.addFragment(AppIntroSlideFragment.newInstance(true,R.drawable.intro_step_4_img,getString(R.string.intro_step_4_title),getString(R.string.intro_step_4_desc)));
            adapter.addFragment(AppIntroSlideFragment.newInstance(false,R.drawable.intro_step_3_img,getString(R.string.intro_step_3_title),getString(R.string.intro_step_3_desc)));
            adapter.addFragment(AppIntroSlideFragment.newInstance(false,R.drawable.intro_step_2_img,getString(R.string.intro_step_2_title),getString(R.string.intro_step_2_desc)));
            adapter.addFragment(AppIntroSlideFragment.newInstance(false,R.drawable.intro_step_1_img,getString(R.string.intro_step_1_title),getString(R.string.intro_step_1_desc)));
        }else {
            adapter.addFragment(AppIntroSlideFragment.newInstance(false,R.drawable.intro_step_1_img,getString(R.string.intro_step_1_title),getString(R.string.intro_step_1_desc)));
            adapter.addFragment(AppIntroSlideFragment.newInstance(false,R.drawable.intro_step_2_img,getString(R.string.intro_step_2_title),getString(R.string.intro_step_2_desc)));
            adapter.addFragment(AppIntroSlideFragment.newInstance(false,R.drawable.intro_step_3_img,getString(R.string.intro_step_3_title),getString(R.string.intro_step_3_desc)));
            adapter.addFragment(AppIntroSlideFragment.newInstance(true,R.drawable.intro_step_4_img,getString(R.string.intro_step_4_title),getString(R.string.intro_step_4_desc)));
        }

        //set adapter to viewpager
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
        if (Darchin.isRtl(getContext())) {
            viewPager.setCurrentItem(adapter.getCount()-1);
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
    public void onDestroyView() {
        // set user has app intro to pref
        User.getInstance(getContext()).setAppIntro(getContext());

        super.onDestroyView();
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList = new ArrayList<>();

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

        void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }
}
