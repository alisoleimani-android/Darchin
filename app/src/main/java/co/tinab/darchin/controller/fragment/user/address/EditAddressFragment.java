package co.tinab.darchin.controller.fragment.user.address;


import android.app.Activity;
import android.content.Intent;
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
public class EditAddressFragment extends Fragment implements View.OnClickListener {
    private Address address;
    private EditTextLight txtInputAddress,txtInputTag;
    private TextViewLight txtDistrict;
    private int locationId;

    public static final short EDIT = 0;
    public static final short EDIT_FROM_ORDERS = 1;
    private short type = -1;

    private WaitingDialog waitingDialog;

    public static EditAddressFragment newInstance(short type,Address address){
        EditAddressFragment fragment = new EditAddressFragment();
        Bundle args = new Bundle();
        args.putParcelable("address",address);
        args.putShort("type",type);
        fragment.setArguments(args);
        return fragment;
    }

    public EditAddressFragment() {
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
        return inflater.inflate(R.layout.fragment_edit_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.container_toolbar).findViewById(R.id.toolbar);
        ImageButton btnDelete = toolbar.findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(this);

        ImageButton btnBack = toolbar.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

        // get location id:
        locationId = address.getLocation().getId();

        txtDistrict = view.findViewById(R.id.txt_district);
        txtDistrict.setText(address.getAreaName());

        txtInputAddress = view.findViewById(R.id.txt_input_address);
        txtInputAddress.setText(address.getName());

        txtInputTag = view.findViewById(R.id.txt_input_tag);
        txtInputTag.setText(address.getTag());

        ButtonNormal btnSave = view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);

        ViewGroup btnDistrict = view.findViewById(R.id.btn_district);
        btnDistrict.setOnClickListener(this);

        if (getContext() != null) waitingDialog = new WaitingDialog(getContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;

            case R.id.btn_delete:
                // request to delete this address
                deleteAddress();
                break;

            case R.id.btn_save:
                editAddress();
                break;

            case R.id.btn_district:
                // goto district selection page
                if (getActivity() != null)
                    ((MainActivity)getActivity()).pushFragment(
                            AreaSelectionFragment.newInstance(AreaSelectionFragment.EDIT,address),
                            EditAddressFragment.class.getName(), 0);
                break;
        }
    }

    private void deleteAddress(){
        if (waitingDialog != null) waitingDialog.show();

        LocationRequestHelper requestHandler = LocationRequestHelper.getInstance();
        requestHandler.deleteAddress(User.getToken(getContext()),address.getId()).enqueue(new MyCallback<BasicResource>() {
            @Override
            public void onRequestSuccess(Response<BasicResource> response) {
                if (waitingDialog != null) waitingDialog.dismiss();

                BasicResource basicResource = response.body();
                if (FunctionHelper.isSuccess(getView(),basicResource)) {
                    if (getActivity() != null) ((MainActivity)getActivity())
                            .popFragment(null,AddressFragment.class.getName());

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

    private void editAddress(){
        if (waitingDialog != null) waitingDialog.show();

        String name = txtInputAddress.getText().toString();
        int id = address.getId();
        String tag = txtInputTag.getText().toString();
        String token = User.getToken(getContext());

        LocationRequestHelper requestHandler = LocationRequestHelper.getInstance();
        requestHandler.editAddress(token, id, name, tag, locationId).enqueue(new MyCallback<BasicResource>() {
            @Override
            public void onRequestSuccess(Response<BasicResource> response) {
                if (waitingDialog != null) waitingDialog.dismiss();

                BasicResource basicResource = response.body();
                if (FunctionHelper.isSuccess(getView(),basicResource)) {
                    if (type == EDIT) {
                        if (getActivity() != null) ((MainActivity)getActivity())
                                .popFragment(null,AddressFragment.class.getName());
                    }

                    if (type == EDIT_FROM_ORDERS) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            txtDistrict.setText(address.getAreaSearch().getValue());
            locationId = address.getAreaSearch().getLocation();
        }
    }
}
