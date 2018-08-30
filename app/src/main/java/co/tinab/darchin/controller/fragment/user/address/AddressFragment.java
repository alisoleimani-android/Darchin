package co.tinab.darchin.controller.fragment.user.address;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.adapter.AddressListAdapter;
import co.tinab.darchin.controller.fragment.BaseFragment;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.controller.tools.ItemClickSupport;
import co.tinab.darchin.model.address.Address;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.LocationRequestHelper;
import co.tinab.darchin.model.network.resources.AddressResource;
import co.tinab.darchin.model.User;
import co.tinab.darchin.R;
import co.tinab.darchin.view.component.EmptyView;
import co.tinab.darchin.view.component.LoadingView;
import co.tinab.darchin.view.toolbox.MyRecyclerView;
import co.tinab.darchin.view.toolbox.TextViewLight;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddressFragment extends BaseFragment implements View.OnClickListener {
    private List<Address> addressList = new ArrayList<>();
    private AddressListAdapter adapter;
    private EmptyView emptyView;
    private LoadingView loadingView;

    public static AddressFragment newInstance(){
        return new AddressFragment();
    }

    public AddressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.container_toolbar).findViewById(R.id.toolbar);
        TextViewLight txtTitle = toolbar.findViewById(R.id.txt_title);
        txtTitle.setText(R.string.addresses);

        ImageButton btnBack = toolbar.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

        loadingView = view.findViewById(R.id.loading_view);

        // fab
        final FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        // Address RecyclerView
        MyRecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new AddressListAdapter(addressList);
        recyclerView.setAdapter(adapter);
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                // goto edit address page
                if (getActivity() != null) ((MainActivity)getActivity()).pushFragment(
                        EditAddressFragment.newInstance(EditAddressFragment.EDIT,addressList.get(position)),
                        AddressFragment.class.getName(), 0);
            }
        });
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

        // Empty view for empty list
        emptyView = view.findViewById(R.id.empty_view);

        // get account addresses from server
        getAddressList();
    }

    private void getAddressList(){
        String token = User.getToken(getContext());
        if (isAdded()) loadingView.show();

        LocationRequestHelper requestHandler = LocationRequestHelper.getInstance();
        requestHandler.getAddressList(token).enqueue(new MyCallback<AddressResource>() {
            @Override
            public void onRequestSuccess(Response<AddressResource> response) {
                if (isAdded()) loadingView.hide();

                AddressResource addressResource = response.body();
                if (FunctionHelper.isSuccess(null,addressResource) && addressResource.getAddressList() != null) {
                    bind(addressResource.getAddressList());
                }
            }

            @Override
            public void onRequestFailed(@Nullable List<String> messages) {
                if (isAdded()) loadingView.hide();
                FunctionHelper.showMessages(getView(),messages);
            }

            @Override
            public void unAuthorizedDetected() {
                if (isAdded()) loadingView.hide();
                if (getActivity() != null) ((MainActivity)getActivity()).logout();
            }
        });
    }

    private void bind(List<Address> addressList){
        this.addressList.clear();
        this.addressList.addAll(addressList);
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount() > 0) {
            if (isAdded()) emptyView.hide();
        }else {
            if (isAdded()) emptyView.show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;
            case R.id.fab:
                // add a new address:
                if (getActivity() != null)
                    ((MainActivity)getActivity()).pushFragment(AreaSelectionFragment
                            .newInstance(AreaSelectionFragment.CREATE,new Address()));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            getAddressList();
        }
    }
}
