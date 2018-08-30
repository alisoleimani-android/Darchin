package co.tinab.darchin.controller.fragment.store;


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
import java.util.List;
import java.util.Locale;

import co.tinab.darchin.R;
import co.tinab.darchin.model.store.Category;
import co.tinab.darchin.model.store.Product;
import co.tinab.darchin.model.store.Store;
import klogi.com.RtlViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    private boolean isFragmentLoaded = false;
    private TabLayout tabLayout;
    private RtlViewPager viewPager;
    private ViewPagerAdapter adapter;
    private Store store;
    private Product selectedProduct;

    public static CategoryFragment newInstance(Store store, Product selectedProduct){
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putParcelable("store",store);
        args.putParcelable("product",selectedProduct);
        fragment.setArguments(args);
        return fragment;
    }

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("store")) {
                store = getArguments().getParcelable("store");
            }

            if (getArguments().containsKey("product")) {
                selectedProduct = getArguments().getParcelable("product");
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tabs);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            for (Fragment fragment : adapter.mFragmentList){
                fragment.onResume();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isFragmentLoaded) {
            isFragmentLoaded = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    buildTabLayoutView(store.getCategories());
                }
            },50);
        }
    }

    private void buildTabLayoutView(List<Category> categories){
        if(viewPager != null) {
            viewPager.setOffscreenPageLimit(categories.size());
            setupViewPager(viewPager,categories);
        }
        if(tabLayout != null){
            tabLayout.setupWithViewPager(viewPager);
            this.changeTabsFont();
        }

        checkProductInCategories(); // checking selected product by user in categories
    }

    //Setting font of Tab's Title
    private void changeTabsFont() {
        if (getContext() != null) {
            Typeface font;
            if (Locale.getDefault().getLanguage().equals("fa")) {
                font = Typeface.createFromAsset(getContext().getAssets(),"fonts/Light.ttf");
            }else {
                font = Typeface.createFromAsset(getContext().getAssets(),"fonts/LatinLight.ttf");
            }

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
    }

    private void setupViewPager(ViewPager viewPager,List<Category> categories) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());

        //initialize fragments:
        for (Category category : categories){
            adapter.addFragment(ProductsFragment.newInstance(store,category), category.getName());
        }

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

    private void checkProductInCategories(){
        int index = -1;
        if (selectedProduct != null && store != null) {
            List<Category> categories = store.getCategories();
            for (Category category : categories){
                for (Product product : category.getProducts()){
                    if (product.getProductId() == selectedProduct.getProductId()) {
                        index = categories.indexOf(category);
                        break;
                    }
                }
                if (index != -1) {
                    break;
                }
            }

            // select category
            if (index != -1) {
                viewPager.setCurrentItem(index);
            }
        }
    }
}
