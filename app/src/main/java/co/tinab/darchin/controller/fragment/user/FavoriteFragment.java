package co.tinab.darchin.controller.fragment.user;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.adapter.VerticalStoreListAdapter;
import co.tinab.darchin.controller.fragment.store.StoreFragment;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.controller.tools.ItemClickSupport;
import co.tinab.darchin.model.Favorite;
import co.tinab.darchin.model.User;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.AccountRequestHelper;
import co.tinab.darchin.model.network.request_helpers.StoreRequestHelper;
import co.tinab.darchin.model.network.resources.FavoriteResource;
import co.tinab.darchin.model.network.resources.StoreResource;
import co.tinab.darchin.model.store.Store;
import co.tinab.darchin.view.component.EmptyView;
import co.tinab.darchin.view.component.LoadingView;
import co.tinab.darchin.view.dialog.WaitingDialog;
import co.tinab.darchin.view.toolbox.MyRecyclerView;
import co.tinab.darchin.view.toolbox.MySnackbar;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {
    private List<Store> storeList = new ArrayList<>();
    private VerticalStoreListAdapter adapter;
    private EmptyView emptyView;
    private LoadingView loadingView;
    private WaitingDialog waitingDialog;
    private boolean isFragmentLeaved = false,isFragmentLoaded = false;

    public static FavoriteFragment newInstance(){
        return new FavoriteFragment();
    }

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MyRecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new VerticalStoreListAdapter(storeList);
        recyclerView.setAdapter(adapter);
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                // goto the store page
                Store store = storeList.get(position);
                if (store != null){
                    openStore(store.getStoreId());
                }
            }
        });

        emptyView = view.findViewById(R.id.empty_view);
        loadingView = view.findViewById(R.id.loading_view);
        if (getContext() != null) waitingDialog = new WaitingDialog(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFragmentLeaved) {
            isFragmentLeaved = false;
            if (isAdded()) getFavorites();
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
                    if (isAdded()) getFavorites();
                }
            },50);
        }
    }

    private void getFavorites(){
        if (isAdded()) loadingView.show();
        String token = User.getToken(getContext());

        AccountRequestHelper requestHandler = AccountRequestHelper.getInstance();
        requestHandler.getFavoriteList(token).enqueue(new MyCallback<FavoriteResource>() {
            @Override
            public void onRequestSuccess(Response<FavoriteResource> response) {
                if (isAdded())  loadingView.hide();

                FavoriteResource favoriteResponse = response.body();
                if (FunctionHelper.isSuccess(null,favoriteResponse) && favoriteResponse.getFavoriteList() != null) {
                    List<Store> storeList = new ArrayList<>();
                    for (Favorite favorite : favoriteResponse.getFavoriteList()){
                        storeList.add(favorite.getStore());
                    }
                    User.getInstance(getContext()).setFavoriteList(storeList);
                    bind(storeList);
                }
            }

            @Override
            public void onRequestFailed(@Nullable List<String> messages) {
                if (isAdded()) {
                    loadingView.hide();
                }
            }

            @Override
            public void unAuthorizedDetected() {
                if (isAdded()) loadingView.hide();
                if (getActivity() != null) ((MainActivity)getActivity()).logout();
            }
        });
    }

    private void bind(List<Store> storeList){
        this.storeList.clear();
        this.storeList.addAll(storeList);
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount() > 0) {
            if (isAdded()) emptyView.hide();
        }else {
            if (isAdded()) emptyView.show();
        }
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

                    isFragmentLeaved = true;
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
