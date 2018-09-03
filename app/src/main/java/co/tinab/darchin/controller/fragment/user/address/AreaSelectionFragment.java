package co.tinab.darchin.controller.fragment.user.address;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.adapter.AreaListAdapter;
import co.tinab.darchin.controller.fragment.order.AddressSelectionFragment;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.controller.tools.SimpleDividerItemDecoration;
import co.tinab.darchin.model.User;
import co.tinab.darchin.model.address.Address;
import co.tinab.darchin.model.address.AreaSearch;
import co.tinab.darchin.model.address.City;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.LocationRequestHelper;
import co.tinab.darchin.model.network.resources.AreaResource;
import co.tinab.darchin.view.component.EmptyView;
import co.tinab.darchin.view.component.LoadingView;
import co.tinab.darchin.view.component.SectionView;
import co.tinab.darchin.view.toolbox.ButtonNormal;
import co.tinab.darchin.view.toolbox.EditTextLight;
import co.tinab.darchin.view.toolbox.MyRecyclerView;
import co.tinab.darchin.view.toolbox.MySnackbar;
import co.tinab.darchin.view.toolbox.TextViewLight;
import co.tinab.darchin.view.toolbox.TextViewNormal;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AreaSelectionFragment extends Fragment implements View.OnClickListener,TextWatcher, SectionView.RequestCompleteListener {
    private List<AreaSearch> areaList = new ArrayList<>();
    private AreaListAdapter adapter;
    private Address address;
    private City selectedCity;

    private Timer timer;
    private Call<AreaResource> searchCall;
    private EmptyView emptyView;
    private ButtonNormal btnContinue;
    private LoadingView loadingView;
    private TextViewNormal txtCity;

    public static final short CREATE = 0;
    public static final short EDIT = 1;
    public static final short CREATE_FROM_ORDERS = 2;
    private short type = -1;

    public static AreaSelectionFragment newInstance(short type, Address address){
        AreaSelectionFragment fragment = new AreaSelectionFragment();
        Bundle args = new Bundle();
        args.putParcelable("address",address);
        args.putShort("type",type);
        fragment.setArguments(args);
        return fragment;
    }

    public AreaSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            address = getArguments().getParcelable("address");
            type = getArguments().getShort("type");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_area_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.container_toolbar).findViewById(R.id.toolbar);

        ImageButton btnBack = toolbar.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

        TextViewLight txtTitle = toolbar.findViewById(R.id.txt_title);
        txtTitle.setText(getString(R.string.area_selection));

        btnContinue = view.findViewById(R.id.btn_continue);
        btnContinue.setOnClickListener(this);

        loadingView = view.findViewById(R.id.loading_view);
        loadingView.hide();

        MyRecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        adapter = new AreaListAdapter(areaList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemSelectedListener(new AreaListAdapter.ItemSelectedListener() {
            @Override
            public void onItemSelected(AreaSearch location) {
                address.setAreaSearch(location);
                btnContinue.setVisibility(View.VISIBLE);
            }
        });

        EditTextLight txtInputSearch = view.findViewById(R.id.txt_input_search);
        txtInputSearch.addTextChangedListener(this);

        txtCity = view.findViewById(R.id.txt_city);
        if (address.getLocation() != null &&
                address.getLocation().getArea() != null &&
                address.getLocation().getArea().getCity() != null &&
                address.getLocation().getArea().getCity().getName() != null) {
            selectedCity = address.getLocation().getArea().getCity();
            txtCity.setText(selectedCity.getName().concat(","));

        }else {
            txtCity.setText(getString(R.string.which_city_do_you_live).concat(","));
        }
        txtCity.setOnClickListener(this);

        // Empty view for empty list
        emptyView = view.findViewById(R.id.empty_view);

        SectionView sectionView = view.findViewById(R.id.container_section);
        sectionView.setOnRequestCompleteListener(this);

        if (FunctionHelper.isConnected(getContext())) {
            sectionView.requestData("area","top");
        }else {
            sectionView.bind(User.getInstance(getContext()).getSections(getContext(),"area","top"));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;

            case R.id.btn_continue:
                if (type == CREATE) {
                    if (address.getAreaSearch() != null) {
                        if (getActivity() != null) ((MainActivity)getActivity()).pushFragment(
                                NewAddressFragment.newInstance(NewAddressFragment.CREATE,address),
                                AddressFragment.class.getName(), 0);
                    }else {
                        MySnackbar.make(getView(),MySnackbar.Alert,R.string.please_select_an_area);
                    }
                }

                if (type == EDIT) {
                    if (address.getAreaSearch() != null) {
                        if (getActivity() != null) ((MainActivity)getActivity())
                                .popFragment(null,EditAddressFragment.class.getName());
                    }else {
                        MySnackbar.make(getView(),MySnackbar.Alert,R.string.please_select_an_area);
                    }
                }

                if (type == CREATE_FROM_ORDERS) {
                    if (address.getAreaSearch() != null) {
                        if (getActivity() != null) ((MainActivity)getActivity()).pushFragment(
                                NewAddressFragment.newInstance(NewAddressFragment.CREATE_FROM_ORDERS,address),
                                AddressSelectionFragment.class.getName(),0);
                    }else {
                        MySnackbar.make(getView(),MySnackbar.Alert,R.string.please_select_an_area);
                    }
                }
                break;

            case R.id.txt_city:
                // open city selection dialog
                CitySelectionDialog dialog = new CitySelectionDialog(v.getContext(),R.style.MyDialogTheme);
                dialog.setListener(new CitySelectionDialog.CitySelectionListener() {
                    @Override
                    public void onCitySelected(City city) {
                        selectedCity = city;
                        txtCity.setText(selectedCity.getName());
                    }
                });
                dialog.show();
                break;
        }
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
                if (getActivity() != null) getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        emptyView.hide();
                        if(phrase.trim().length() >= 3) {
                            searchAddress(phrase);
                        }else {
                            loadingView.hide();
                            areaList.clear();
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }, 600);
    }

    private void searchAddress(String phrase){
        if (selectedCity != null) {
            loadingView.show();

            int cityId = selectedCity.getCityId();
            LocationRequestHelper requestHandler = LocationRequestHelper.getInstance();
            searchCall = requestHandler.searchAddress(String.valueOf(cityId),phrase);
            searchCall.enqueue(new MyCallback<AreaResource>() {
                @Override
                public void onRequestSuccess(Response<AreaResource> response) {
                    loadingView.hide();
                    AreaResource areaResource = response.body();
                    if (FunctionHelper.isSuccess(null,areaResource) && areaResource.getAreaList() != null) {
                        bind(areaResource.getAreaList());
                    }
                }

                @Override
                public void onRequestFailed(@Nullable List<String> messages) {
                    loadingView.hide();
                }

                @Override
                public void unAuthorizedDetected() {
                    loadingView.hide();
                    if (getActivity() != null) ((MainActivity)getActivity()).logout();
                }
            });
        }else {
            MySnackbar.make(getView(),MySnackbar.Alert,R.string.please_select_your_city).show();
        }
    }

    private void bind(List<AreaSearch> areaList){
        this.areaList.clear();
        if (!areaList.isEmpty()) {
            emptyView.hide();
            this.areaList.addAll(areaList);
        }else {
            emptyView.show();
        }
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestCompleted() {

    }

    @Override
    public void onRequestFailed() {

    }
}
