package co.tinab.darchin.controller.fragment.user.credit;


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
import co.tinab.darchin.controller.fragment.BaseFragment;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.CreditRequestHelper;
import co.tinab.darchin.model.network.resources.BasicResource;
import co.tinab.darchin.model.User;
import co.tinab.darchin.R;
import co.tinab.darchin.view.dialog.WaitingDialog;
import co.tinab.darchin.view.toolbox.EditTextNormal;
import co.tinab.darchin.view.toolbox.MySnackbar;
import co.tinab.darchin.view.toolbox.TextViewLight;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class GiftCodeFragment extends BaseFragment implements View.OnClickListener {
    private EditTextNormal txtInputGiftCode;
    private WaitingDialog waitingDialog;

    public static GiftCodeFragment newInstance(){
        return new GiftCodeFragment();
    }

    public GiftCodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gift_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.container_toolbar).findViewById(R.id.toolbar);

        TextViewLight txtTitle = toolbar.findViewById(R.id.txt_title);
        txtTitle.setText(getString(R.string.gift_code));

        view.findViewById(R.id.btn_confirm).setOnClickListener(this);

        txtInputGiftCode = view.findViewById(R.id.txt_input_gift_code);

        ImageButton btnBack = toolbar.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

        if (getContext() != null) waitingDialog = new WaitingDialog(getContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;

            case R.id.btn_confirm:
                // request to register gift code:
                registerGift();
                break;
        }
    }

    private void registerGift(){
        if (waitingDialog != null) waitingDialog.show();
        String code = txtInputGiftCode.getText().toString();
        String token = User.getToken(getContext());

        CreditRequestHelper requestHelper = CreditRequestHelper.getInstance();
        requestHelper.registerGift(token,code).enqueue(new MyCallback<BasicResource>() {
            @Override
            public void onRequestSuccess(Response<BasicResource> response) {
                if (waitingDialog != null) waitingDialog.dismiss();

                BasicResource basicResource = response.body();
                if (FunctionHelper.isSuccess(getView(), basicResource)) {
                    if (getActivity() != null) ((MainActivity)getActivity())
                            .popFragment(null,CreditFragment.class.getName());

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
