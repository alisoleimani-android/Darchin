package co.tinab.darchin.controller.fragment.store;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;

import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.SuggestsRequestHelper;
import co.tinab.darchin.model.network.resources.BasicResource;
import co.tinab.darchin.view.dialog.WaitingDialog;
import co.tinab.darchin.view.toolbox.MySnackbar;
import co.tinab.darchin.view.toolbox.TextViewLight;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreSuggestionFragment extends Fragment implements View.OnClickListener {
    private EditText txtInputStore,txtInputCity,txtInputAddress,txtInputPhone,txtInputName,txtInputMobile;
    private Switch btnIsManager;
    private WaitingDialog waitingDialog;

    public static StoreSuggestionFragment newInstance(){
        return new StoreSuggestionFragment();
    }

    public StoreSuggestionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store_suggestion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextViewLight txtTitle = view.findViewById(R.id.txt_title);
        txtTitle.setText(getString(R.string.nav_item_store_suggestion));

        txtInputStore = view.findViewById(R.id.txt_input_store);
        txtInputCity = view.findViewById(R.id.txt_input_city);
        txtInputAddress = view.findViewById(R.id.txt_input_address);
        txtInputMobile = view.findViewById(R.id.txt_input_mobile);
        txtInputPhone = view.findViewById(R.id.txt_input_phone);
        txtInputName = view.findViewById(R.id.txt_input_name);
        btnIsManager = view.findViewById(R.id.btn_switch);

        if (getContext() != null) waitingDialog = new WaitingDialog(getContext());

        view.findViewById(R.id.btn_back).setOnClickListener(this);
        view.findViewById(R.id.btn_submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;

            case R.id.btn_submit:
                suggestStore();
                break;
        }
    }

    private void suggestStore(){
        if (waitingDialog != null) waitingDialog.show();

        String name = txtInputStore.getText().toString();
        String city = txtInputCity.getText().toString();
        String address = txtInputAddress.getText().toString();
        String phone = txtInputPhone.getText().toString();
        String mobile = txtInputMobile.getText().toString();
        String creatorName = txtInputName.getText().toString();

        SuggestsRequestHelper requestHelper = SuggestsRequestHelper.getInstance();
        requestHelper.suggestStore(name,city,address,phone,creatorName,mobile,btnIsManager.isChecked()).enqueue(new MyCallback<BasicResource>() {
            @Override
            public void onRequestSuccess(Response<BasicResource> response) {
                if (waitingDialog != null) waitingDialog.dismiss();

                BasicResource basicResource = response.body();
                if (FunctionHelper.isSuccess(getView(),basicResource)) {
                    if (getActivity() != null) getActivity().onBackPressed();

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
