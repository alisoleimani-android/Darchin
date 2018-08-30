package co.tinab.darchin.controller.fragment.authentication;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.AuthRequestHelper;
import co.tinab.darchin.model.network.resources.RegisterCheckResource;
import co.tinab.darchin.R;
import co.tinab.darchin.view.dialog.WaitingDialog;
import co.tinab.darchin.view.toolbox.EditTextLight;
import co.tinab.darchin.view.toolbox.MySnackbar;
import co.tinab.darchin.view.toolbox.TextViewLight;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginOrRegisterFragment extends Fragment implements View.OnClickListener {
    private EditTextLight txtInputMobile;
    private WaitingDialog waitingDialog;
    private String targetTag;

    public static LoginOrRegisterFragment newInstance(String targetTag){
        LoginOrRegisterFragment fragment = new LoginOrRegisterFragment();
        Bundle args = new Bundle();
        args.putString("target_tag",targetTag);
        fragment.setArguments(args);
        return fragment;
    }

    public LoginOrRegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            targetTag = getArguments().getString("target_tag");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_or_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextViewLight txtTitle = view.findViewById(R.id.txt_title);
        txtTitle.setText(getString(R.string.login_or_register));

        if (getContext()!=null) waitingDialog = new WaitingDialog(getContext());

        view.findViewById(R.id.btn_back).setOnClickListener(this);
        view.findViewById(R.id.btn_continue).setOnClickListener(this);

        txtInputMobile = view.findViewById(R.id.txt_input_mobile);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;

            case R.id.btn_continue:
                if (waitingDialog!=null) waitingDialog.show();
                registerCheck();
                break;
        }
    }

    private void registerCheck(){
        AuthRequestHelper requestHandler = AuthRequestHelper.getInstance();
        String mobile = txtInputMobile.getText().toString();

        requestHandler.registerCheck(mobile).enqueue(new MyCallback<RegisterCheckResource>() {
            @Override
            public void onRequestSuccess(Response<RegisterCheckResource> response) {
                if (waitingDialog!=null) waitingDialog.dismiss();

                RegisterCheckResource registerCheckResponse = response.body();
                if (FunctionHelper.isSuccess(null,registerCheckResponse) && registerCheckResponse.getData() != null) {
                    if (registerCheckResponse.getData().isRegister()) {
                        if (getActivity() != null) ((MainActivity)getActivity())
                                .pushFragment(LoginFragment.newInstance(targetTag,txtInputMobile.getText().toString()));

                    }else {
                        if (getActivity() != null) ((MainActivity)getActivity())
                                .pushFragment(RegisterFragment.newInstance(targetTag,txtInputMobile.getText().toString()));

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
                if (getActivity() != null) ((MainActivity)getActivity()).logout();
            }
        });
    }
}
