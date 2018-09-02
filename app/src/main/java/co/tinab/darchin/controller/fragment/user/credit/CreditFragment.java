package co.tinab.darchin.controller.fragment.user.credit;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.List;

import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.fragment.BaseFragment;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.address.City;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.AccountRequestHelper;
import co.tinab.darchin.model.network.resources.UserResource;
import co.tinab.darchin.model.User;
import co.tinab.darchin.R;
import co.tinab.darchin.view.component.SectionView;
import co.tinab.darchin.view.dialog.WaitingDialog;
import co.tinab.darchin.view.toolbox.MoneyTextView;
import co.tinab.darchin.view.toolbox.TextViewLight;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreditFragment extends BaseFragment implements View.OnClickListener, SectionView.RequestCompleteListener {
    private WaitingDialog waitingDialog;
    private MoneyTextView txtCharge;

    public static CreditFragment newInstance(){
        return new CreditFragment();
    }

    public CreditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_credit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageButton btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

        SectionView sectionViewTop = view.findViewById(R.id.container_section_top);
        sectionViewTop.setOnRequestCompleteListener(this);
        SectionView sectionViewBottom = view.findViewById(R.id.container_section_bottom);
        sectionViewBottom.setOnRequestCompleteListener(this);

        TextViewLight txtTitle = view.findViewById(R.id.txt_title);
        txtTitle.setText(getString(R.string.nav_item_charge_account));

        txtCharge = view.findViewById(R.id.txt_account_charge);
        txtCharge.setText(User.getInstance(getContext()).getCredit());

        ViewGroup btnCharge,btnGift;
        btnCharge = view.findViewById(R.id.btn_charge_account);
        btnCharge.setOnClickListener(this);

        btnGift = view.findViewById(R.id.btn_gift);
        btnGift.setOnClickListener(this);

        view.findViewById(R.id.btn_transfer).setOnClickListener(this);

        City city = User.getInstance(getContext()).getCity(getContext());
        if (city != null) {
            if (!city.getName().isEmpty()) {
                if (FunctionHelper.isConnected(getContext())) {
                    sectionViewTop.requestData("charge","top");
                    sectionViewBottom.requestData("charge","down");
                }else {
                    sectionViewTop.bind(User.getInstance(getContext()).getSections(getContext(),"charge","top"));
                    sectionViewBottom.bind(User.getInstance(getContext()).getSections(getContext(),"charge","down"));
                }
            }
        }

        if(getContext() != null) waitingDialog = new WaitingDialog(getContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_charge_account:
                if (getActivity() != null) ((MainActivity)getActivity())
                        .pushFragment(ChargeAccountFragment.newInstance());
                break;

            case R.id.btn_gift:
                if (getActivity() != null) ((MainActivity)getActivity())
                        .pushFragment(GiftCodeFragment.newInstance(),CreditFragment.class.getName(),0);
                break;

            case R.id.btn_transfer:
                if (getActivity() != null) ((MainActivity) getActivity())
                        .pushFragment(TransferFragment.newInstance(),CreditFragment.class.getName(),0);
                break;

            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            // get User Info
            getAccountInfo();
        }
    }

    private void getAccountInfo(){
        if (waitingDialog != null) waitingDialog.show();
        String token = User.getToken(getContext());

        AccountRequestHelper requestHandler = AccountRequestHelper.getInstance();
        requestHandler.getAccountInfo(token).enqueue(new MyCallback<UserResource>() {
            @Override
            public void onRequestSuccess(Response<UserResource> response) {
                if (waitingDialog != null) waitingDialog.dismiss();

                UserResource userResponse = response.body();
                if (FunctionHelper.isSuccess(null,userResponse) && userResponse.getUser() != null) {
                    // save user
                    User.saveInstance(getContext(),userResponse.getUser());
                    txtCharge.setText(User.getInstance(getContext()).getCredit());
                }
            }

            @Override
            public void onRequestFailed(@Nullable List<String> messages) {
                if (waitingDialog != null) waitingDialog.dismiss();
            }

            @Override
            public void unAuthorizedDetected() {
                if (waitingDialog != null) waitingDialog.dismiss();
                if (getActivity() != null) ((MainActivity)getActivity()).logout();
            }
        });
    }
}
