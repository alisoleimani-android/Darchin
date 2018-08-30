package co.tinab.darchin.controller.fragment.other;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import co.tinab.darchin.controller.adapter.ProductSearchListAdapter;
import co.tinab.darchin.controller.adapter.VerticalStoreListAdapter;
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
import co.tinab.darchin.view.toolbox.TextViewLight;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowAllFragment extends Fragment implements View.OnClickListener {
    private Section section;
    private RecyclerView.Adapter adapter;
    private MyRecyclerView recyclerView;
    private List<Product> productList = new ArrayList<>();
    private List<Store> storeList = new ArrayList<>();

    private WaitingDialog waitingDialog;

    public static ShowAllFragment newInstance(Section section){
        ShowAllFragment fragment = new ShowAllFragment();
        Bundle args = new Bundle();
        args.putParcelable("section",section);
        fragment.setArguments(args);
        return fragment;
    }

    public ShowAllFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            section = getArguments().getParcelable("section");
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_all, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextViewLight txtTitle = view.findViewById(R.id.txt_title);
        txtTitle.setText(section.getName());

        view.findViewById(R.id.btn_back).setOnClickListener(this);

        recyclerView = view.findViewById(R.id.recycler_view);
        if (section.getType() != null) {
            if (section.getType().equals("product")) {
                adapter = new ProductSearchListAdapter(productList);
            }
            if (section.getType().equals("store")) {
                adapter = new VerticalStoreListAdapter(storeList);
            }
            recyclerView.setAdapter(adapter);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestData(section);
                }
            },500);
        }

        // waiting dialog:
        if (getContext() != null) waitingDialog = new WaitingDialog(getContext());
    }

    private void requestData(Section section){
        if (section.getType() != null && !section.getContents().isJsonNull()) {
            this.section = section;
            if (section.getType().equals("product")) {
                bindProducts(getProducts(section.getContents()));

            } else if (section.getType().equals("store")) {
                bindStores(getStores(section.getContents()));

            }
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

    private void bindProducts(final List<Product> productList){
        int positionStart = this.productList.size();
        this.productList.addAll(productList);
        adapter.notifyItemRangeInserted(positionStart,this.productList.size());
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                if (productList.get(position) != null) openStore(productList.get(position));
            }
        });
    }

    private void bindStores(final List<Store> storeList){
        int positionStart = this.storeList.size();
        this.storeList.addAll(storeList);
        adapter.notifyItemRangeInserted(positionStart,this.storeList.size());
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Store store = storeList.get(position);
                if (store != null) openStore(store.getStoreId());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
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
                if (FunctionHelper.isSuccess(getView(), storeResource) && storeResource.getStore() != null) {
                    if (getActivity() != null ) ((MainActivity)getActivity())
                            .pushFragment(StoreFragment.newInstance(storeResource.getStore(),product));

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
                if (FunctionHelper.isSuccess(getView(), storeResource) && storeResource.getStore() != null) {
                    if (getActivity() != null) ((MainActivity)getActivity())
                            .pushFragment(StoreFragment.newInstance(storeResource.getStore()));

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
    }
}
