package co.tinab.darchin.view.navigation_drawer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.tools.ItemClickSupport;
import co.tinab.darchin.model.User;
import co.tinab.darchin.view.toolbox.MoneyTextView;
import co.tinab.darchin.view.toolbox.MyRecyclerView;
/**
 * Created by ali.soleimani on 7/31/2017.
 */

public class DrawerFragment extends Fragment {

    private final int[] titleArray = {
            R.string.nav_item_login_register,
            R.string.nav_item_orders,
            R.string.nav_item_favorite,
            R.string.nav_item_profile,
            R.string.nav_item_charge_account,
            R.string.nav_item_share,
            R.string.nav_item_store_suggestion,
            R.string.services_packages,
            R.string.nav_item_support
    };
    private final int[] imgArray = {
            R.drawable.ic_login_24dp,
            R.drawable.ic_orders_24dp,
            R.drawable.ic_favorite_24dp,
            R.drawable.ic_profile_24dp,
            R.drawable.ic_credit_24dp,
            R.drawable.ic_gift_credit_24dp,
            R.drawable.ic_store_24dp,
            R.drawable.ic_services_24dp,
            R.drawable.ic_support_24dp
    };

    private DrawerLayout mDrawerLayout;
    private View containerView;
    private ViewGroup containerCredit;
    private FragmentDrawerListener drawerListener;
    private TextView txtName;
    private MoneyTextView txtCredit;

    private List<NavDrawerModel> list = new ArrayList<>();
    private NavDrawerAdapter adapter;

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public List<NavDrawerModel> getData() {
        List<NavDrawerModel> data = new ArrayList<>();
        // preparing navigation drawer items
        if (getContext() != null) {
            if (!User.getInstance(getContext()).hasLoggedIn(getContext())) {
                data.add(new NavDrawerModel(getString(titleArray[0]),imgArray[0]));
            }else {
                for (int i = 1 ; i < titleArray.length ; i++ ){
                    data.add(new NavDrawerModel(getString(titleArray[i]),imgArray[i]));
                }
            }
        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        MyRecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        // init views:
        txtName = view.findViewById(R.id.txt_name);
        txtCredit = view.findViewById(R.id.txt_money);
        containerCredit = view.findViewById(R.id.container_credit);

        adapter = new NavDrawerAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                drawerListener.onDrawerItemSelected(v, position);
                mDrawerLayout.closeDrawer(containerView);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        notifyDrawer();
    }

    public void notifyDrawer(){
        if (getContext() != null) {
            User user = User.getInstance(getContext());
            if (!user.hasLoggedIn(getContext())) {
                containerCredit.setVisibility(View.INVISIBLE);
                txtName.setText(getString(R.string.welcome_guest_user));
            }else {
                containerCredit.setVisibility(View.VISIBLE);
                txtName.setText(user.getName());
                txtCredit.setText(user.getCredit());
            }
            if (!list.isEmpty()) list.clear();
            list.addAll(getData());
            adapter.notifyDataSetChanged();
        }
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        if (getActivity() != null) {
            containerView = getActivity().findViewById(fragmentId);
            mDrawerLayout = drawerLayout;
            toolbar.findViewById(R.id.btn_menu).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
                        mDrawerLayout.closeDrawer(Gravity.START);
                    } else {
                        mDrawerLayout.openDrawer(Gravity.START);
                    }
                }
            });
        }
    }

    public boolean isDrawerOpen(){
        return mDrawerLayout.isDrawerOpen(Gravity.START);
    }

    public void closeDrawer(){
        mDrawerLayout.closeDrawer(Gravity.START);
    }

    public interface FragmentDrawerListener {
        void onDrawerItemSelected(View view, int position);
    }
}

