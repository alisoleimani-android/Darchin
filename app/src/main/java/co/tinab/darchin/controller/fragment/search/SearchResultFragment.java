package co.tinab.darchin.controller.fragment.search;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.adapter.ProductSearchListAdapter;
import co.tinab.darchin.controller.adapter.VerticalStoreListAdapter;
import co.tinab.darchin.controller.fragment.store.StoreFragment;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.controller.tools.ItemClickSupport;
import co.tinab.darchin.model.address.City;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.StoreRequestHelper;
import co.tinab.darchin.model.network.resources.StoreResource;
import co.tinab.darchin.model.section.Section;
import co.tinab.darchin.model.store.Product;
import co.tinab.darchin.model.store.Store;
import co.tinab.darchin.model.User;
import co.tinab.darchin.R;
import co.tinab.darchin.view.component.SectionView;
import co.tinab.darchin.view.dialog.WaitingDialog;
import co.tinab.darchin.view.toolbox.MyRecyclerView;
import co.tinab.darchin.view.toolbox.MySnackbar;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultFragment extends Fragment implements SectionView.RequestCompleteListener, ProductSearchListAdapter.ClickListener {
    private static final String FRAGMENT_TYPE = "type";
    private static final String DATA = "data";
    private String typeOfFragment;
    private List<Product> productList;
    private List<Store> storeList;
    private WaitingDialog waitingDialog;

    public void setTypeOfFragment(String typeOfFragment) {
        this.typeOfFragment = typeOfFragment;
    }

    public String getTypeOfFragment() {
        return typeOfFragment;
    }

    public static SearchResultFragment newFoodInstance(List<Product> productList){
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putString(FRAGMENT_TYPE, "product");
        args.putParcelableArrayList(DATA, (ArrayList<? extends Parcelable>) productList);
        fragment.setArguments(args);
        return fragment;
    }

    public static SearchResultFragment newRestaurantInstance(List<Store> storeList){
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putString(FRAGMENT_TYPE, "store");
        args.putParcelableArrayList(DATA, (ArrayList<? extends Parcelable>) storeList);
        fragment.setArguments(args);
        return fragment;
    }

    public SearchResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            setTypeOfFragment(getArguments().getString(FRAGMENT_TYPE));
            if (getTypeOfFragment().equals("product")) {
                productList = getArguments().getParcelableArrayList(DATA);
            } else if (getTypeOfFragment().equals("store")) {
                storeList = getArguments().getParcelableArrayList(DATA);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        SectionView sectionViewTop = view.findViewById(R.id.container_section_top);
        sectionViewTop.setOnRequestCompleteListener(this);
        SectionView sectionViewBottom = view.findViewById(R.id.container_section_bottom);
        sectionViewBottom.setOnRequestCompleteListener(this);

        if (getContext() != null) waitingDialog = new WaitingDialog(getContext());

        MyRecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerView.Adapter adapter;
        if (getTypeOfFragment().equals("product")) {
            adapter = new ProductSearchListAdapter(productList);
            recyclerView.setAdapter(adapter);
            ((ProductSearchListAdapter)adapter).setOnClickListener(this);

        } else if (getTypeOfFragment().equals("store")) {
            adapter = new VerticalStoreListAdapter(storeList);
            recyclerView.setAdapter(adapter);
            ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    Store store = storeList.get(position);
                    if (store != null) openStore(store.getStoreId());
                }
            });
        }

        City city = User.getInstance(getContext()).getCity(getContext());
        if (city != null) {
            if (FunctionHelper.isConnected(getContext())) {
                sectionViewTop.requestData("search","top");
                sectionViewBottom.requestData("search","down");
            }else {
                List<Section> topSections = User.getInstance(getContext())
                        .getSections(getContext(),"search","top");
                List<Section> bottomSections = User.getInstance(getContext())
                        .getSections(getContext(),"search","down");

                sectionViewTop.bind(topSections);
                sectionViewBottom.bind(bottomSections);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onRequestCompleted() {}

    @Override
    public void onRequestFailed() {}

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

    @Override
    public void onClick(int productPosition) {
        if (getTypeOfFragment().equals("product")) {
            if (productList.get(productPosition) != null) openStore(productList.get(productPosition));
        }
    }
}
