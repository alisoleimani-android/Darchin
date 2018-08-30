package co.tinab.darchin.controller.fragment.user.address;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.List;

import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.fragment.order.AddressSelectionFragment;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.address.Address;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.LocationRequestHelper;
import co.tinab.darchin.model.network.resources.BasicResource;
import co.tinab.darchin.model.User;
import co.tinab.darchin.R;
import co.tinab.darchin.view.dialog.WaitingDialog;
import co.tinab.darchin.view.toolbox.ButtonNormal;
import co.tinab.darchin.view.toolbox.EditTextLight;
import co.tinab.darchin.view.toolbox.MySnackbar;
import co.tinab.darchin.view.toolbox.TextViewLight;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewAddressFragment extends Fragment implements View.OnClickListener {
    private Address address;
    private EditTextLight txtInputAddress,txtInputTag;
    private TextViewLight txtArea;

    public static final short CREATE = 0;
    public static final short CREATE_FROM_ORDERS = 2;
    private short type = -1;

    private WaitingDialog waitingDialog;

    public static NewAddressFragment newInstance(short type,Address address){
        NewAddressFragment fragment = new NewAddressFragment();
        Bundle args = new Bundle();
        args.putParcelable("address",address);
        args.putShort("type",type);
        fragment.setArguments(args);
        return fragment;
    }

    public NewAddressFragment() {
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
        return inflater.inflate(R.layout.fragment_new_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.container_toolbar).findViewById(R.id.toolbar);

        ImageButton btnBack = toolbar.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

        TextViewLight txtTitle = toolbar.findViewById(R.id.txt_title);
        txtTitle.setText(getString(R.string.new_address));

        TextViewLight txtArea = view.findViewById(R.id.txt_district);
        txtArea.setText(address.getAreaSearch().getValue());

        txtInputAddress = view.findViewById(R.id.txt_input_address);
        txtInputTag = view.findViewById(R.id.txt_input_tag);

        ButtonNormal btnSave = view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);

        if (getContext() != null) waitingDialog = new WaitingDialog(getContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;

            case R.id.btn_save:
                createAddress();
                break;
        }
    }

    private void createAddress(){
        if (waitingDialog != null) waitingDialog.show();

        String name = txtInputAddress.getText().toString();
        String tag = txtInputTag.getText().toString();
        int locationID = address.getAreaSearch().getLocation();
        String token = User.getToken(getContext());

        LocationRequestHelper requestHandler = LocationRequestHelper.getInstance();
        requestHandler.createAddress(token,name,locationID,tag).enqueue(new MyCallback<BasicResource>() {
            @Override
            public void onRequestSuccess(Response<BasicResource> response) {
                if (waitingDialog != null) waitingDialog.dismiss();

                BasicResource basicResource = response.body();
                if (FunctionHelper.isSuccess(getView(),basicResource)) {
                    if (type == CREATE) {
                        if (getActivity() != null) ((MainActivity)getActivity())
                                .popFragment(null,AddressFragment.class.getName());
                    }

                    if (type == CREATE_FROM_ORDERS) {
                        if (getActivity() != null) ((MainActivity)getActivity())
                                .popFragment(null,AddressSelectionFragment.class.getName());
                    }

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
