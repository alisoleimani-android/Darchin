package co.tinab.darchin.view.component;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.adapter.HorizontalProductListAdapter;
import co.tinab.darchin.controller.adapter.HorizontalStoreListAdapter;
import co.tinab.darchin.controller.fragment.other.ShowAllFragment;
import co.tinab.darchin.controller.fragment.store.StoreFragment;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.controller.tools.ItemClickSupport;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.StoreRequestHelper;
import co.tinab.darchin.model.network.resources.StoreResource;
import co.tinab.darchin.model.section.Section;
import co.tinab.darchin.model.store.Product;
import co.tinab.darchin.model.store.Store;
import co.tinab.darchin.R;
import co.tinab.darchin.view.dialog.WaitingDialog;
import co.tinab.darchin.view.toolbox.MyRecyclerView;
import co.tinab.darchin.view.toolbox.MySnackbar;
import co.tinab.darchin.view.toolbox.TextViewNormal;
import retrofit2.Response;

/**
 * Created by A.S.R on 1/1/2018.
 */

class HorizontalListSectionView implements View.OnClickListener{
    private ViewGroup parent;
    private View view;
    private MyRecyclerView recyclerView;
    private TextViewNormal txtTitle;
    private ViewGroup btnShowAll;
    private Section section;
    private WaitingDialog waitingDialog;

    HorizontalListSectionView(ViewGroup parent) {
        this.parent = parent;
    }

    public View getView(){
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_horizontal_list_section_view,parent,false);

        txtTitle = view.findViewById(R.id.txt_title);

        btnShowAll = view.findViewById(R.id.btn_show_all);
        btnShowAll.setOnClickListener(this);

        recyclerView = view.findViewById(R.id.recycler_view);

        waitingDialog = new WaitingDialog(parent.getContext());

        return view;
    }

    void requestData(Section section){
        this.section = section;
        if (section.getType().equals("product")) {
            bindProduct(getProducts(section.getContents()));

        } else if (section.getType().equals("store")) {
            bindStore(getStores(section.getContents()));

        }
    }

    private List<Product> getProducts(JsonArray jsonArray){
        Gson gson = new Gson();
        List<Product> products = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray){
            Product product = gson.fromJson(jsonElement,Product.class);
            products.add(product);
        }
        return products;
    }

    private List<Store> getStores(JsonArray jsonArray){
        Gson gson = new Gson();
        List<Store> stores = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray){
            Store store = gson.fromJson(jsonElement,Store.class);
            stores.add(store);
        }
        return stores;
    }

    private void bindProduct(final List<Product> productList){
        if (productList.size() <= 10) {
            btnShowAll.setVisibility(View.GONE);
        }
        txtTitle.setText(section.getName());
        recyclerView.setAdapter(new HorizontalProductListAdapter(productList));
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                openStore(productList.get(position));
            }
        });
    }

    private void bindStore(final List<Store> storeList){
        if (storeList.size() <= 10) {
            btnShowAll.setVisibility(View.GONE);
        }
        txtTitle.setText(section.getName());
        recyclerView.setAdapter(new HorizontalStoreListAdapter(storeList));
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                openStore(storeList.get(position).getStoreId());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_show_all:
                if (parent.getContext() instanceof Activity) {
                    ((MainActivity)parent.getContext()).pushFragment(ShowAllFragment.newInstance(section));
                }
                break;
        }
    }

    // open store:
    private void openStore(final Product product){
        if (waitingDialog != null) waitingDialog.show();

        StoreRequestHelper requestHelper = StoreRequestHelper.getInstance();
        requestHelper.getStore(product.getStoreId()).enqueue(new MyCallback<StoreResource>() {
            @Override
            public void onRequestSuccess(Response<StoreResource> response) {
                if (waitingDialog != null) waitingDialog.dismiss();

                StoreResource storeResource = response.body();
                if (FunctionHelper.isSuccess(view, storeResource) && storeResource.getStore() != null) {
                    if (parent.getContext() instanceof MainActivity) ((MainActivity)parent.getContext())
                            .pushFragment(StoreFragment.newInstance(storeResource.getStore(),product));

                }else {
                    MySnackbar.make(view,MySnackbar.Failure,R.string.request_failed).show();
                }
            }

            @Override
            public void onRequestFailed(@Nullable List<String> messages) {
                if (waitingDialog != null) waitingDialog.dismiss();
                FunctionHelper.showMessages(view,messages);
            }

            @Override
            public void unAuthorizedDetected() {
                if (waitingDialog != null) waitingDialog.dismiss();
                if (parent.getContext() instanceof MainActivity)
                    ((MainActivity)parent.getContext()).logout();
            }
        });
    }

    // open store:
    private void openStore(int storeId){
        if (waitingDialog != null) waitingDialog.show();

        StoreRequestHelper requestHelper = StoreRequestHelper.getInstance();
        requestHelper.getStore(storeId).enqueue(new MyCallback<StoreResource>() {
            @Override
            public void onRequestSuccess(Response<StoreResource> response) {
                if (waitingDialog != null) waitingDialog.dismiss();

                StoreResource storeResource = response.body();
                if (FunctionHelper.isSuccess(view, storeResource) && storeResource.getStore() != null) {
                    if (parent.getContext() instanceof MainActivity) ((MainActivity)parent.getContext())
                            .pushFragment(StoreFragment.newInstance(storeResource.getStore()));

                }else {
                    MySnackbar.make(view,MySnackbar.Failure,R.string.request_failed).show();
                }
            }

            @Override
            public void onRequestFailed(@Nullable List<String> messages) {
                if (waitingDialog != null) waitingDialog.dismiss();
                FunctionHelper.showMessages(view,messages);
            }

            @Override
            public void unAuthorizedDetected() {
                if (waitingDialog != null) waitingDialog.dismiss();
                if (parent.getContext() instanceof MainActivity)
                    ((MainActivity)parent.getContext()).logout();
            }
        });
    }
}
