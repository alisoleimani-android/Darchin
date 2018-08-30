package co.tinab.darchin.view.component;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.adapter.SearchAreaListAdapter;
import co.tinab.darchin.controller.adapter.SearchProductListAdapter;
import co.tinab.darchin.controller.adapter.SearchStoreListAdapter;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.controller.tools.ItemClickSupport;
import co.tinab.darchin.model.address.AreaSearch;
import co.tinab.darchin.model.address.City;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.SearchRequestHelper;
import co.tinab.darchin.model.network.request_helpers.StoreRequestHelper;
import co.tinab.darchin.model.network.resources.SearchResultResource;
import co.tinab.darchin.model.network.resources.StoreResource;
import co.tinab.darchin.model.store.Product;
import co.tinab.darchin.model.store.Store;
import co.tinab.darchin.model.User;
import co.tinab.darchin.R;
import co.tinab.darchin.view.dialog.WaitingDialog;
import co.tinab.darchin.view.toolbox.MyRecyclerView;
import co.tinab.darchin.view.toolbox.MySnackbar;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by A.S.R on 4/10/2018.
 */

public class SearchResultView extends FrameLayout implements TextWatcher {
    private List<Product> products = new ArrayList<>();
    private SearchProductListAdapter searchProductListAdapter;

    private List<Store> stores = new ArrayList<>();
    private SearchStoreListAdapter searchStoreListAdapter;

    private List<AreaSearch> areas = new ArrayList<>();
    private SearchAreaListAdapter searchAreaListAdapter;

    private WaitingDialog waitingDialog;
    private EmptyView emptyView;
    private LoadingView loadingView;
    private Timer timer;
    private Call<SearchResultResource> searchCall;

    public SearchResultView(@NonNull Context context) {
        this(context,null,0);
    }

    public SearchResultView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public SearchResultView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        buildView(context);
        setVisibility(GONE);
    }

    public void addTo(EditText editText){
        editText.addTextChangedListener(this);
    }

    private void buildView(Context context){
        View view = LayoutInflater
                .from(context).inflate(R.layout.layout_search_result_view,this,true);

        MyRecyclerView recyclerViewProduct = view.findViewById(R.id.recycler_view_product);
        searchProductListAdapter = new SearchProductListAdapter(products);
        recyclerViewProduct.setAdapter(searchProductListAdapter);
        ItemClickSupport.addTo(recyclerViewProduct).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                // request to get store by store_id
                getStore(products.get(position));
            }
        });

        MyRecyclerView recyclerViewStore = view.findViewById(R.id.recycler_view_store);
        searchStoreListAdapter = new SearchStoreListAdapter(stores);
        recyclerViewStore.setAdapter(searchStoreListAdapter);
        ItemClickSupport.addTo(recyclerViewStore).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                listener.onStoreClicked(stores.get(position));
            }
        });

        MyRecyclerView recyclerViewLocation = view.findViewById(R.id.recycler_view_location);
        searchAreaListAdapter = new SearchAreaListAdapter(areas);
        recyclerViewLocation.setAdapter(searchAreaListAdapter);
        ItemClickSupport.addTo(recyclerViewLocation).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                listener.onAreaClicked(areas.get(position));
            }
        });

        if (getContext() != null) waitingDialog = new WaitingDialog(getContext());
        emptyView = view.findViewById(R.id.empty_view);
        loadingView = view.findViewById(R.id.loading_view);
    }

    private void setProducts(List<Product> products){
        this.products.clear();
        if (!products.isEmpty()) {
            if (products.size() > 3) {
                this.products.addAll(products.subList(0,2));
            }else {
                this.products.addAll(products);
            }
        }
        searchProductListAdapter.notifyDataSetChanged();
    }

    private void setStores(List<Store> stores) {
        this.stores.clear();
        if (!stores.isEmpty()) {
            if (stores.size() > 3) {
                this.stores.addAll(stores.subList(0,2));
            }else {
                this.stores.addAll(stores);
            }
        }
        searchStoreListAdapter.notifyDataSetChanged();
    }

    private void setAreas(List<AreaSearch> areas){
        this.areas.clear();
        if (!areas.isEmpty()) {
            if (areas.size() > 3) {
                this.areas.addAll(areas.subList(0,2));
            }else {
                this.areas.addAll(areas);
            }
        }
        searchAreaListAdapter.notifyDataSetChanged();
    }

    private void clearLists(){
        stores.clear();
        searchStoreListAdapter.notifyDataSetChanged();

        products.clear();
        searchProductListAdapter.notifyDataSetChanged();

        areas.clear();
        searchAreaListAdapter.notifyDataSetChanged();
    }

    private void search(String phrase){
        City city = User.getInstance(getContext()).getCity(getContext());
        if (city != null) {
            int cityId = city.getCityId();

            SearchRequestHelper requestHelper = SearchRequestHelper.getInstance();
            searchCall = requestHelper.search(cityId,phrase);
            searchCall.enqueue(new MyCallback<SearchResultResource>() {
                @Override
                public void onRequestSuccess(Response<SearchResultResource> response) {
                    loadingView.hide();

                    SearchResultResource resultResource = response.body();
                    if (FunctionHelper.isSuccess(getRootView(), resultResource) && resultResource.getResponse() != null) {

                        SearchResultResource.Response resultResponse = resultResource.getResponse();

                        if (resultResponse.getAreas() != null && resultResponse.getProducts() != null
                                && resultResponse.getStores() != null) {

                            if (!resultResponse.getAreas().isEmpty() || !resultResponse.getProducts().isEmpty()
                                    || !resultResponse.getStores().isEmpty()) {

                                setAreas(resultResponse.getAreas());
                                setProducts(resultResponse.getProducts());
                                setStores(resultResponse.getStores());

                            }else {
                                emptyView.show();
                            }

                        }else {
                            emptyView.show();
                        }

                    }else {
                        MySnackbar.make(getRootView(),MySnackbar.Failure,R.string.request_failed).show();
                        loadingView.hide();
                        emptyView.hide();
                        setVisibility(GONE);
                    }
                }

                @Override
                public void onRequestFailed(@Nullable List<String> messages) {
                    loadingView.hide();
                    emptyView.hide();
                    setVisibility(GONE);
                }

                @Override
                public void unAuthorizedDetected() {
                    loadingView.hide();
                    emptyView.hide();
                    setVisibility(GONE);
                    if (getContext() instanceof MainActivity) ((MainActivity)getContext()).logout();
                }
            });
        }
    }

    // open store:
    private void getStore(final Product product){
        if (waitingDialog != null) waitingDialog.show();

        StoreRequestHelper requestHelper = StoreRequestHelper.getInstance();
        requestHelper.getStore(product.getStoreId()).enqueue(new MyCallback<StoreResource>() {
            @Override
            public void onRequestSuccess(Response<StoreResource> response) {
                if (waitingDialog != null) waitingDialog.dismiss();

                StoreResource storeResource = response.body();
                if (FunctionHelper.isSuccess(getRootView(), storeResource) && storeResource.getStore() != null) {
                    listener.onProductClicked(storeResource.getStore(),product);

                }else {
                    MySnackbar.make(getRootView(),MySnackbar.Failure,R.string.request_failed).show();
                }
            }

            @Override
            public void onRequestFailed(@Nullable List<String> messages) {
                if (waitingDialog != null) waitingDialog.dismiss();
                FunctionHelper.showMessages(getRootView(),messages);
            }

            @Override
            public void unAuthorizedDetected() {
                if (waitingDialog != null) waitingDialog.dismiss();
                if (getContext() instanceof MainActivity) ((MainActivity)getContext()).logout();
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (searchCall != null) searchCall.cancel();
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        final String phrase = s.toString();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getContext() instanceof Activity) ((Activity)getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // clear container
                        clearLists();
                        emptyView.hide();

                        if(phrase.trim().length() >= 3) {
                            setVisibility(VISIBLE);
                            loadingView.show();

                            // search the phrase entered by user
                            search(phrase);
                        }else {
                            loadingView.hide();
                            setVisibility(GONE);
                        }
                    }
                });
            }
        }, 600);
    }

    public interface ResultClickListener{
        void onStoreClicked(Store store);
        void onProductClicked(Store store,Product product);
        void onAreaClicked(AreaSearch area);
    }
    private ResultClickListener listener;
    public void setOnResultClickListener(ResultClickListener listener){
        this.listener = listener;
    }
}
