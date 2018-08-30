package co.tinab.darchin.controller.fragment.store;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.fragment.BaseFragment;
import co.tinab.darchin.controller.fragment.order.CartFragment;
import co.tinab.darchin.controller.tools.AppBarStateChangeListener;
import co.tinab.darchin.controller.tools.Display;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.controller.tools.MyDateTime;
import co.tinab.darchin.controller.tools.ToolbarColorizeHelper;
import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.User;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.AccountRequestHelper;
import co.tinab.darchin.model.network.resources.BasicResource;
import co.tinab.darchin.model.store.Cart;
import co.tinab.darchin.model.store.Product;
import co.tinab.darchin.model.store.ProductItem;
import co.tinab.darchin.model.store.Store;
import co.tinab.darchin.view.dialog.WaitingDialog;
import co.tinab.darchin.view.toolbox.ButtonCart;
import co.tinab.darchin.view.toolbox.MySnackbar;
import co.tinab.darchin.view.toolbox.TextViewLight;
import co.tinab.darchin.view.toolbox.TextViewNormal;
import klogi.com.RtlViewPager;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFragment extends BaseFragment implements View.OnClickListener {
    private KenBurnsView imgCover;
    private ViewGroup containerInfo;
    public ButtonCart btnCart;
    private Toolbar toolbar;
    private Product product;
    public Store store;
    private TextViewLight txtName;
    private Typeface font;
    private TabLayout tabLayout;
    private RtlViewPager viewPager;
    private ViewPagerAdapter adapter;
    private ImageButton btnFavorite;
    private TextViewNormal txtOrderStatus;
    private View shapeOnline;

    private WaitingDialog waitingDialog;

    public static StoreFragment newInstance(Store store,Product product){
        StoreFragment storeFragment = new StoreFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("product", product);
        bundle.putParcelable("store", store);
        storeFragment.setArguments(bundle);
        return storeFragment;
    }

    public static StoreFragment newInstance(Store store){
        StoreFragment storeFragment = new StoreFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("store", store);
        storeFragment.setArguments(bundle);
        return storeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("product")) {
                product = getArguments().getParcelable("product");
            }
            if (getArguments().containsKey("store")) {
                store = getArguments().getParcelable("store");
                if (store != null) store.setCart();
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

    public StoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tabs);
        toolbar = view.findViewById(R.id.toolbar);
        containerInfo = view.findViewById(R.id.container_info);

        txtName = view.findViewById(R.id.txt_title);
        txtName.setText(store.getName());

        imgCover = view.findViewById(R.id.img_cover);
        imgCover.setScaleType(KenBurnsView.ScaleType.CENTER_CROP);
        if (getContext() != null) {
            Picasso.with(getContext())
                    .load(store.getCover())
                    .resize(Display.getWidthOfDevice(getContext()),Display.heightOfCover(getContext()))
                    .onlyScaleDown()
                    .into(imgCover);
        }

        AppBarLayout appBarLayout = view.findViewById(R.id.appBar);
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.COLLAPSED) {
                    ToolbarColorizeHelper.colorizeToolbar(toolbar,R.color.textOnPrimary);
                    txtName.setVisibility(View.VISIBLE);
                    containerInfo.setVisibility(View.INVISIBLE);
                    imgCover.pause();
                } else if (state == State.EXPANDED) {
                    ToolbarColorizeHelper.colorizeToolbar(toolbar,android.R.color.white);
                    txtName.setVisibility(View.INVISIBLE);
                    containerInfo.setVisibility(View.VISIBLE);
                    imgCover.resume();
                }
            }
        });

        RoundedImageView imgLogo = view.findViewById(R.id.img);
        if (FunctionHelper.isConnected(getContext())) {
            Picasso.with(getContext())
                    .load(store.getPicture())
                    .resizeDimen(R.dimen.store_image_width,R.dimen.store_image_height)
                    .centerCrop()
                    .error(R.drawable.ic_store_placeholder)
                    .placeholder(R.drawable.ic_store_placeholder)
                    .into(imgLogo);
        }else {
            Picasso.with(getContext())
                    .load(store.getPicture())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .resizeDimen(R.dimen.store_image_width,R.dimen.store_image_height)
                    .centerCrop()
                    .error(R.drawable.ic_store_placeholder)
                    .placeholder(R.drawable.ic_store_placeholder)
                    .into(imgLogo);
        }

        TextViewNormal txtName = view.findViewById(R.id.txt_name);
        txtName.setText(store.getName());

        TextViewNormal txtScore = view.findViewById(R.id.txt_score);
        txtScore.setBackground(store.getVote().getScoreBackground(getContext()));
        txtScore.setText(store.getVote().getAverageString());

        TextViewNormal txtDiscount = view.findViewById(R.id.txt_discount);
        if (store.hasDiscount()) {
            txtDiscount.setVisibility(View.VISIBLE);
            txtDiscount.setText(store.getDiscount());
        }

        shapeOnline = view.findViewById(R.id.shape_circle);
        txtOrderStatus = view.findViewById(R.id.txt_status);

        TextViewLight txtAddress = view.findViewById(R.id.txt_address);
        txtAddress.setText(store.getFullAddress(getContext()));

        if (getContext() != null) waitingDialog = new WaitingDialog(getContext());

        // btn back
        ImageButton btnBack = toolbar.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

        // btn shopping
        btnCart = view.findViewById(R.id.btn_cart);
        btnCart.setOnClickListener(this);

        // btn favorite
        btnFavorite = view.findViewById(R.id.btn_favorite);
        btnFavorite.setOnClickListener(this);

        // btn share
        ImageButton btnShare = view.findViewById(R.id.btn_share);
        btnShare.setOnClickListener(this);

        // build tabs and viewpager
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                buildTabLayoutView();

                // set store getting order status
                setOrderStatus();
            }
        },400);

        // is favorite?
        isFavorite();
    }

    private void setOrderStatus(){
        Date currentTime = MyDateTime.getCurrentTime();

        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.blink);
        if (store.isOrderAccepted(currentTime) && store.isStoreAvailable()) {
            txtOrderStatus.setText(getString(R.string.accept_order));
            shapeOnline.setBackgroundResource(R.drawable.online_circle);
            shapeOnline.setVisibility(View.VISIBLE);
            shapeOnline.startAnimation(animation);
            if (getContext() != null) txtOrderStatus
                    .setTextColor(ContextCompat.getColor(getContext(),android.R.color.white));
        }else {
            if (store.isStoreAvailable()) {
                txtOrderStatus.setText(getString(R.string.store_closed));
                shapeOnline.setBackgroundResource(R.drawable.pre_order_circle);
                shapeOnline.setVisibility(View.VISIBLE);
                shapeOnline.startAnimation(animation);
                if (getContext() != null) txtOrderStatus
                        .setTextColor(ContextCompat.getColor(getContext(),android.R.color.white));
            }else {
                txtOrderStatus.setText(getString(R.string.temporarily_inactive));
                shapeOnline.setVisibility(View.GONE);
                animation.cancel();
                if (getContext() != null) txtOrderStatus
                        .setTextColor(ContextCompat.getColor(getContext(),R.color.failure));
            }
        }
    }

    private void buildTabLayoutView(){
        if (isAdded()) {
            if(viewPager != null) {
                viewPager.setOffscreenPageLimit(3);
                setupViewPager(viewPager);
            }
            if(tabLayout != null){
                tabLayout.setupWithViewPager(viewPager);
                // set font for tabs
                this.changeTabsFont();
            }
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

        // add fragments
        adapter.addFragment(CategoryFragment.newInstance(store,product),getString(R.string.store_menu));
        adapter.addFragment(InfoFragment.newInstance(store),getString(R.string.store_info));
        adapter.addFragment(CommentFragment.newInstance(store),getString(R.string.store_comments));

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
    public void onResume() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Cart cart = store.getCart();
                if (adapter != null) {
                    // 0 is index of menu fragment
                    CategoryFragment categoryFragment = ((CategoryFragment)adapter.getItem(0));
                    categoryFragment.onResume();
                }
                if (btnCart != null) {
                    if (!cart.getProducts().isEmpty()) {
                        int countOfFoods = 0;
                        for (Product product : cart.getProducts()){
                            if (!product.getSelectedItems().isEmpty()) {
                                for (ProductItem item : product.getSelectedItems()){
                                    countOfFoods += item.getCount();
                                }
                            }
                        }
                        btnCart.notify(countOfFoods);
                    }else {
                        btnCart.notify(0);
                    }
                }
            }
        },300);
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        Cart cart = store.getCart();
        for (Product product : cart.getProducts()){
            for (ProductItem item : product.getSelectedItems()){
                item.setCount(0);
            }
            product.getSelectedItems().clear();
        }
        cart.getProducts().clear();
        cart.setSelectedPeriod(null);
        cart.setSelectedAddress(null);
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;

            case R.id.btn_cart:
                if (getActivity() != null) ((MainActivity)getActivity())
                        .pushFragment(CartFragment.newInstance(store.getCart()));
                break;

            case R.id.btn_favorite:
                if (User.getInstance(getContext()).isFavorite(store.getStoreId())) {
                    deleteFavorite();

                }else {
                    createFavorite();
                }
                break;

            case R.id.btn_share:
                shareStore();
                break;
        }
    }

    private void shareStore(){
        if (getContext() != null) {
            String shareLink = Constant.DOMAIN.concat("store/").concat(store.getSlug());
            String description = getString(R.string.share_store_description,store.getName());

            Context mContext = getContext();
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.app_name));
            i.putExtra(Intent.EXTRA_TEXT, description.concat("\n\n").concat(shareLink));
            mContext.startActivity(Intent.createChooser(i, "Share with :"));
        }
    }

    private void createFavorite(){
        if (getContext() != null) {
            if (User.getInstance(getContext()).hasLoggedIn(getContext())) {
                if (waitingDialog != null) waitingDialog.show();
                int storeID = store.getStoreId();

                AccountRequestHelper requestHandler = AccountRequestHelper.getInstance();
                requestHandler.createFavorite(User.getToken(getContext()),storeID).enqueue(new MyCallback<BasicResource>() {
                    @Override
                    public void onRequestSuccess(Response<BasicResource> response) {
                        if (waitingDialog != null) waitingDialog.dismiss();

                        BasicResource basicResource = response.body();
                        if (FunctionHelper.isSuccess(getView(),basicResource)) {
                            if (getContext() != null) {
                                btnFavorite.setImageDrawable(ContextCompat
                                        .getDrawable(getContext(),R.drawable.ic_favorite_24dp));
                            }
                            User.getInstance(getContext()).getFavoriteList().add(store);

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
            }else {
                MySnackbar.make(getView(),MySnackbar.Alert,R.string.need_to_be_logged_in).show();
            }
        }
    }

    private void deleteFavorite(){
        if (getContext() != null) {
            if (User.getInstance(getContext()).hasLoggedIn(getContext())) {
                if (waitingDialog != null) waitingDialog.show();
                int storeID = store.getStoreId();

                AccountRequestHelper requestHandler = AccountRequestHelper.getInstance();
                requestHandler.deleteFavorite(User.getToken(getContext()),storeID).enqueue(new MyCallback<BasicResource>() {
                    @Override
                    public void onRequestSuccess(Response<BasicResource> response) {
                        if (waitingDialog != null) waitingDialog.dismiss();

                        BasicResource basicResource = response.body();
                        if (FunctionHelper.isSuccess(getView(),basicResource)) {
                            if (getContext() != null) {
                                btnFavorite.setImageDrawable(ContextCompat
                                        .getDrawable(getContext(),R.drawable.ic_not_favorite_24dp));
                            }
                            User.getInstance(getContext()).removeFavorite(store.getStoreId());

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
            }else {
                MySnackbar.make(getView(),MySnackbar.Alert,R.string.need_to_be_logged_in).show();
            }
        }
    }

    private void isFavorite(){
        if (getContext() != null) {
            if (User.getInstance(getContext()).isFavorite(store.getStoreId())) {
                btnFavorite.setImageDrawable(ContextCompat
                        .getDrawable(getContext(),R.drawable.ic_favorite_24dp));

            }else {
                btnFavorite.setImageDrawable(ContextCompat
                        .getDrawable(getContext(),R.drawable.ic_not_favorite_24dp));
            }
        }
    }

}
