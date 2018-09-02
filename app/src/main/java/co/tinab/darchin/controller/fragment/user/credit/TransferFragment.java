package co.tinab.darchin.controller.fragment.user.credit;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.User;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.AuthRequestHelper;
import co.tinab.darchin.model.network.request_helpers.CreditRequestHelper;
import co.tinab.darchin.model.network.resources.BasicResource;
import co.tinab.darchin.model.network.resources.RegisterCheckResource;
import co.tinab.darchin.view.dialog.QuestionDialog;
import co.tinab.darchin.view.dialog.WaitingDialog;
import co.tinab.darchin.view.toolbox.MySnackbar;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransferFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher {

    private EditText txtInputMobile,txtInputAmount;
    private Button btnTransfer;
    private String name;
    private WaitingDialog waitingDialog;

    public static TransferFragment newInstance(){
        return new TransferFragment();
    }

    public TransferFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transfer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((TextView)view.findViewById(R.id.txt_title)).setText(R.string.transfer_credit_to_another_user);

        txtInputMobile = view.findViewById(R.id.txt_input_mobile);
        txtInputMobile.addTextChangedListener(this);

        txtInputAmount = view.findViewById(R.id.txt_input_amount);
        txtInputAmount.setOnFocusChangeListener(this);

        view.findViewById(R.id.btn_back).setOnClickListener(this);

        btnTransfer = view.findViewById(R.id.btn_transfer);
        btnTransfer.setOnClickListener(this);

        if (getContext() != null) waitingDialog = new WaitingDialog(getContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;

            case R.id.btn_transfer:
                String yes = getString(R.string.yes);
                String no = getString(R.string.no);
                String amount = txtInputAmount.getText().toString();
                String mobile = txtInputMobile.getText().toString();
                String description = getString(R.string.transfer_credit_question,amount,mobile,name);

                QuestionDialog dialog = QuestionDialog.newInstance(yes,no,description);
                dialog.setListener(new QuestionDialog.ClickListener() {
                    @Override
                    public void onPositiveBtnClicked(DialogFragment dialogFragment) {
                        // request to transfer credit
                        transfer();
                    }

                    @Override
                    public void onNegativeBtnClicked(DialogFragment dialogFragment) {
                        dialogFragment.dismiss();
                    }
                });
                dialog.show(getChildFragmentManager(),"QuestionDialog");

                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (isMobileValid()) {
                registerCheck();
            }
        }
    }

    private boolean isMobileValid(){
        int length = txtInputMobile.getText().toString().trim().length();
        if (length == 11) {
            return true;
        }else {
            MySnackbar.make(getView(),MySnackbar.Alert,R.string.mobile_number_is_not_correct).show();
            txtInputMobile.requestFocus();
            return false;
        }
    }

    private void registerCheck(){
        String mobile = txtInputMobile.getText().toString();

        AuthRequestHelper requestHelper = AuthRequestHelper.getInstance();
        requestHelper.registerCheck(mobile).enqueue(new MyCallback<RegisterCheckResource>() {
            @Override
            public void onRequestSuccess(Response<RegisterCheckResource> response) {
                RegisterCheckResource resource = response.body();

                if (resource != null && FunctionHelper.isSuccess(getView(), resource)
                        && resource.getData() != null) {

                    // check user registration
                    if (!resource.getData().isRegister()) {
                        clear();
                        MySnackbar.make(getView(),MySnackbar.Failure,R.string.user_not_registered).show();
                        return;
                    }

                    // check user activation
                    if (resource.getData().getStatus().equals("active")) {
                        if (resource.getData().getName() == null) return;

                        name = resource.getData().getName();
                        btnTransfer.setVisibility(View.VISIBLE);
                        btnTransfer.setText(getString(R.string.transfer_credit_to_specific_user,name));
                    }else {
                        clear();
                        MySnackbar.make(getView(),MySnackbar.Failure,R.string.user_is_not_active).show();
                    }
                }else {
                    clear();
                    MySnackbar.make(getView(),MySnackbar.Failure,R.string.request_failed).show();
                }
            }

            @Override
            public void onRequestFailed(@Nullable List<String> messages) {
                clear();
                FunctionHelper.showMessages(getView(),messages);
            }

            @Override
            public void unAuthorizedDetected() {
                if (getActivity() != null) ((MainActivity)getActivity()).logout();
            }
        });
    }

    private void clear(){
        txtInputMobile.requestFocus();
        btnTransfer.setVisibility(View.GONE);
        name = "";
        btnTransfer.setText("");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        btnTransfer.setVisibility(View.GONE);
    }

    @Override
    public void afterTextChanged(Editable s) {}

    private void transfer(){
        if (waitingDialog != null) waitingDialog.show();
        String username = txtInputMobile.getText().toString();
        String amount = txtInputAmount.getText().toString();

        CreditRequestHelper requestHelper = CreditRequestHelper.getInstance();
        requestHelper.transfer(User.getToken(getContext()),username,amount)
                .enqueue(new MyCallback<BasicResource>() {
            @Override
            public void onRequestSuccess(Response<BasicResource> response) {
                if (waitingDialog != null) waitingDialog.dismiss();

                BasicResource basicResource = response.body();
                if (basicResource != null && FunctionHelper.isSuccess(getView(), basicResource)) {
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
