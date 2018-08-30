package co.tinab.darchin.controller.fragment.order;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.adapter.CartListAdapter;
import co.tinab.darchin.controller.fragment.BaseFragment;
import co.tinab.darchin.controller.interfaces.ProductItemClickListener;
import co.tinab.darchin.model.store.Cart;
import co.tinab.darchin.model.store.Product;
import co.tinab.darchin.model.store.ProductItem;
import co.tinab.darchin.R;
import co.tinab.darchin.view.dialog.CartInfoModal;
import co.tinab.darchin.view.toolbox.MoneyTextView;
import co.tinab.darchin.view.toolbox.MyRecyclerView;
import co.tinab.darchin.view.toolbox.TextViewLight;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends BaseFragment implements
        View.OnClickListener, ProductItemClickListener {
    private Cart cart;
    private CartListAdapter adapter;
    private MoneyTextView txtSumTotal;

    public static CartFragment newInstance(Cart cart){
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putParcelable("cart", cart);
        fragment.setArguments(args);
        return fragment;
    }

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cart = getArguments().getParcelable("cart");
            if (cart != null){
                List<ProductItem> items = new ArrayList<>();

                for (Product product : cart.getProducts()){
                    if (product.getSelectedItems() != null) {
                        if (!product.getSelectedItems().isEmpty()) {
                            for (ProductItem item : product.getSelectedItems()){
                                if (!item.getName().isEmpty()) {
                                    item.setLabel(product.getName().concat(" - ").concat(item.getName()));
                                }else {
                                    item.setLabel(product.getName());
                                }
                                item.setDescription(product.getDescription());
                                item.setProduct(product);
                                items.add(item);
                            }
                        }
                    }
                }

                // setItems to cart
                cart.setItems(items);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        TextViewLight txtTitle = view.findViewById(R.id.txt_title);
        txtTitle.setText(getString(R.string.cart_of_store, cart.getStore().getName()));

        txtSumTotal = view.findViewById(R.id.txt_sum_total);

        MyRecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new CartListAdapter(cart.getItems());
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        final FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0){
                    fab.hide();
                } else{
                    fab.show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        view.findViewById(R.id.container_info).setOnClickListener(this);
        view.findViewById(R.id.btn_back).setOnClickListener(this);
        view.findViewById(R.id.btn_take_order).setOnClickListener(this);
        view.findViewById(R.id.btn_delete).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.container_info:
                // open cart info dialog
                CartInfoModal.newInstance(cart).show(getChildFragmentManager(),CartInfoModal.class.getName());
                break;

            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;

            case R.id.fab:
                // goto description page
                if (getActivity() != null) ((MainActivity)getActivity())
                        .pushFragment(OrderDescriptionFragment.newInstance(cart));
                break;

            case R.id.btn_take_order:
                if (getActivity() != null) ((MainActivity)getActivity())
                        .pushFragment(DeliveryTypeFragment.newInstance(cart));
                break;

            case R.id.btn_delete:
                removeAllItems();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        txtSumTotal.setText(String.valueOf(cart.getSumTotal()));
    }

    @Override
    public void onAddItem(ProductItem item, int position) {
        // add item to cartList
        if (cart.getItems().contains(item)) {
            ProductItem itemInList = cart.getItems().get(position);
            itemInList.setCount(itemInList.getCount() + 1);
            adapter.notifyItemChanged(position);

            // recalculate sum total
            txtSumTotal.setText(String.valueOf(cart.getSumTotal()));
        }
    }

    @Override
    public void onRemoveItem(ProductItem item, int position) {
        // remove item to cartList
        if (cart.getItems().contains(item)) {
            ProductItem itemInList = cart.getItems().get(position);
            itemInList.setCount(itemInList.getCount()-1);
            if (itemInList.getCount() == 0){
                // remove item from cart items
                cart.getItems().remove(position);

                // remove item from selected items in product
                List<ProductItem> selectedItems = item.getProduct().getSelectedItems();
                selectedItems.remove(selectedItems.indexOf(item));

                adapter.notifyItemRemoved(position);
            }else {
                adapter.notifyItemChanged(position);
            }

            // recalculate sum total
            txtSumTotal.setText(String.valueOf(cart.getSumTotal()));
        }

        if (cart.getItems().isEmpty()) {
            if (getActivity() != null) getActivity().onBackPressed();
        }
    }

    private void removeAllItems(){
        int count = adapter.getItemCount();
        for (Product product : cart.getProducts()){
            for (ProductItem item : product.getSelectedItems()){
                item.setCount(0);
            }
            product.getSelectedItems().clear();
        }
        cart.getProducts().clear();
        cart.getItems().clear();
        adapter.notifyItemRangeRemoved(0,count);
        if (getActivity() != null) getActivity().onBackPressed();
    }
}
