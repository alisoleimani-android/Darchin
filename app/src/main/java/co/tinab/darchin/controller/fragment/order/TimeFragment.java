package co.tinab.darchin.controller.fragment.order;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import co.tinab.darchin.controller.adapter.OrderTimeListAdapter;
import co.tinab.darchin.model.store.Cart;
import co.tinab.darchin.model.store.Period;
import co.tinab.darchin.R;
import co.tinab.darchin.view.component.EmptyView;
import co.tinab.darchin.view.toolbox.MyRecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeFragment extends Fragment implements OrderTimeListAdapter.ItemSelectedListener{
    private Cart cart;
    private String type = "";
    private boolean isDelivery,isInside;
    private OrderTimeListAdapter adapter;

    public static TimeFragment newInstance(String type, Cart cart, boolean isDelivery, boolean isInside){
        TimeFragment fragment = new TimeFragment();
        Bundle args = new Bundle();
        args.putString("type",type);
        args.putParcelable("cart", cart);
        args.putBoolean("isDelivery",isDelivery);
        args.putBoolean("isInside",isInside);
        fragment.setArguments(args);
        return fragment;
    }

    public TimeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cart = getArguments().getParcelable("cart");
            type = getArguments().getString("type");
            isDelivery = getArguments().getBoolean("isDelivery");
            isInside = getArguments().getBoolean("isInside");
        }
    }

    public void notifyAdapter(){
        adapter.setSelectedItem(-1);
        adapter.notifyItemRangeChanged(0,adapter.getItemCount());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        EmptyView emptyView = view.findViewById(R.id.empty_view);

        MyRecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        List<Period> periods = cart.getStore().getTimePeriods(type,isInside,isDelivery);
        adapter = new OrderTimeListAdapter(periods);
        adapter.setOnItemSelectedListener(this);
        recyclerView.setAdapter(adapter);

        if (periods.isEmpty()) {
            emptyView.show();
        }else {
            emptyView.hide();
        }
    }

    @Override
    public void onItemSelected(Period period) {
        cart.setSelectedPeriod(period);
    }
}
