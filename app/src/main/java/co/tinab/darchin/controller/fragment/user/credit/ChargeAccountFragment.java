package co.tinab.darchin.controller.fragment.user.credit;


import android.content.Intent;
import android.net.Uri;
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

import co.tinab.darchin.R;
import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.fragment.BaseFragment;
import co.tinab.darchin.controller.fragment.MainFragment;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.User;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.CreditRequestHelper;
import co.tinab.darchin.model.network.resources.OrderResource;
import co.tinab.darchin.view.dialog.WaitingDialog;
import co.tinab.darchin.view.toolbox.ButtonNormal;
import co.tinab.darchin.view.toolbox.EditTextNormal;
import co.tinab.darchin.view.toolbox.MySnackbar;
import co.tinab.darchin.view.toolbox.TextViewLight;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChargeAccountFragment extends BaseFragment implements View.OnClickListener {
    public static ChargeAccountFragment newInstance(){
        return new ChargeAccountFragment();
    }

    private WaitingDialog waitingDialog;
    private EditTextNormal txtInputAmount;

    public ChargeAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_charge_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.container_toolbar).findViewById(R.id.toolbar);

        TextViewLight txtTitle = toolbar.findViewById(R.id.txt_title);
        txtTitle.setText(getString(R.string.nav_item_charge_account));

        ImageButton btnBack = toolbar.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

        ButtonNormal btnConfirm = view.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);

        txtInputAmount = view.findViewById(R.id.txt_input_amount);
        if (getContext() != null) waitingDialog = new WaitingDialog(getContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;

            case R.id.btn_confirm:
                charge();
                break;
        }
    }

    private void charge(){
        if (waitingDialog != null) waitingDialog.show();
        String code = txtInputAmount.getText().toString();
        String token = User.getToken(getContext());

        CreditRequestHelper requestHelper = CreditRequestHelper.getInstance();
        requestHelper.charge(token,code).enqueue(new MyCallback<OrderResource>() {
            @Override
            public void onRequestSuccess(Response<OrderResource> response) {
                if (waitingDialog != null) waitingDialog.dismiss();

                OrderResource orderResource = response.body();
                if (FunctionHelper.isSuccess(getView(), orderResource)) {
                    // goto payment gate
                    String url = Constant.BASE_URL.concat("payment/")
                            .concat(String.valueOf(orderResource.getOrder().getOrderId()));
                    gotoPayment(url);

                    // pop to main
                    if (getActivity() != null) ((MainActivity)getActivity())
                            .popFragment(MainFragment.class.getName());

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

    private void gotoPayment(String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(uri);
        startActivity(intent);
    }
}
