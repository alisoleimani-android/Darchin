package co.tinab.darchin.controller.fragment.store;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.adapter.VerticalProductListAdapter;
import co.tinab.darchin.controller.interfaces.ProductClickListener;
import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.store.Category;
import co.tinab.darchin.model.store.Product;
import co.tinab.darchin.model.store.ProductItem;
import co.tinab.darchin.model.store.Store;
import co.tinab.darchin.view.dialog.ProductImageDialog;
import co.tinab.darchin.view.toolbox.MyRecyclerView;
import co.tinab.darchin.view.toolbox.MySnackbar;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends Fragment implements ProductClickListener {
    private Category category;
    private Store store;
    private boolean isFragmentLoaded = false;
    private VerticalProductListAdapter adapter;
    private ProductImageDialog dialog;

    public static ProductsFragment newInstance(Store store, Category category){
        ProductsFragment productsFragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putParcelable("category", category);
        args.putParcelable("store", store);
        productsFragment.setArguments(args);
        return productsFragment;
    }

    public ProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("category")) {
                category = getArguments().getParcelable("category");
            }
            if (getArguments().containsKey("store")) {
                store = getArguments().getParcelable("store");
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sub_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MyRecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setItemAnimator(null);
        adapter = new VerticalProductListAdapter(category.getProducts());
        adapter.setOnProductClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyItemRangeChanged(0, category.getProducts().size());
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
                    // bind data to fragment here!
                    adapter.notifyItemRangeInserted(0, category.getProducts().size());
                }
            },50);
        }
    }

    @Override
    public void onAddItem(Product product, ProductItem item, int productPosition) {
        if (store.isStoreAvailable()) {
            if (getActivity() != null) {
                StoreFragment storeFragment = (StoreFragment) getActivity()
                        .getSupportFragmentManager().findFragmentByTag(StoreFragment.class.getName());
                if (storeFragment != null) {
                    // get selected product and item
                    List<ProductItem> items = product.getSelectedItems();

                    // increase count of item
                    item.setCount(item.getCount()+1);
                    if (items.contains(item)) {
                        items.set(items.indexOf(item),item);
                    }else {
                        items.add(item);
                    }

                    // add product to cartFoodList
                    List<Product> productList = store.getCart().getProducts();
                    if (productList.contains(product)) {
                        productList.set(productList.indexOf(product),product);
                    }else {
                        productList.add(product);
                    }

                    // notify adapter and btn cart
                    adapter.notifyItemChanged(productPosition);
                    storeFragment.btnCart.increase();
                }
            }

        }else {
            if (getActivity() != null) {
                StoreFragment storeFragment = (StoreFragment) getActivity()
                        .getSupportFragmentManager().findFragmentByTag(StoreFragment.class.getName());
                MySnackbar.make(storeFragment.getView(),MySnackbar.Alert,R.string.store_is_not_available).show();
            }
        }
    }

    @Override
    public void onRemoveItem(Product product, ProductItem item, int productPosition) {
        if (store.isStoreAvailable()) {
            if (getActivity() != null) {
                StoreFragment storeFragment = (StoreFragment) getActivity()
                        .getSupportFragmentManager().findFragmentByTag(StoreFragment.class.getName());
                if (storeFragment != null) {
                    // get selected item and product
                    List<ProductItem> items = product.getSelectedItems();

                    // decrease count of item
                    if (items.contains(item)) {
                        item.setCount(item.getCount()-1);
                        items.set(items.indexOf(item),item);
                        ProductItem itemInList = items.get(items.indexOf(item));
                        if (itemInList.getCount() == 0) items.remove(itemInList);
                    }

                    // remove product to cartFoodList
                    List<Product> productList = store.getCart().getProducts();
                    if (productList.contains(product)) {
                        productList.set(productList.indexOf(product),product);
                        Product productInList = productList.get(productList.indexOf(product));
                        if (productInList.getSelectedItems().isEmpty()) productList.remove(productInList);
                    }
                    adapter.notifyItemChanged(productPosition);
                    storeFragment.btnCart.decrease();
                }
            }
        }
    }

    @Override
    public void onProductImageTouchedDown(Product product) {
        if (product.getMeduimImage() != null) {
            dialog = ProductImageDialog.newInstance(
                    Constant.PRODUCT_MEDIUM_IMAGE_PATH.concat(product.getMeduimImage()));
            dialog.show(getChildFragmentManager(),"ProductImageDialog");
        }
    }

    @Override
    public void onProductImageTouchedUp() {
        if (dialog != null) dialog.dismiss();
    }
}
