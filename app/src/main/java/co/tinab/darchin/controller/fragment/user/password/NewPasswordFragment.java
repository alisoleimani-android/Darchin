package co.tinab.darchin.controller.fragment.user.password;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.List;

import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.fragment.authentication.LoginFragment;
import co.tinab.darchin.controller.fragment.user.ProfileFragment;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.AccountRequestHelper;
import co.tinab.darchin.model.network.request_helpers.AuthRequestHelper;
import co.tinab.darchin.model.network.resources.BasicResource;
import co.tinab.darchin.model.User;
import co.tinab.darchin.R;
import co.tinab.darchin.view.dialog.WaitingDialog;
import co.tinab.darchin.view.toolbox.ButtonNormal;
import co.tinab.darchin.view.toolbox.EditTextNormal;
import co.tinab.darchin.view.toolbox.MySnackbar;
import co.tinab.darchin.view.toolbox.TextViewLight;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewPasswordFragment extends Fragment implements View.OnClickListener {
    private EditTextNormal txtInputNewPass,txtInputRepeatPass;
    private ImageView imgNewPassVisibility,imgRepeatPassVisibility;

    private short type = -1;
    public static final short FORGET_PASS = 0;
    public static final short CHANGE_PASS = 1;

    private String mobile,code,currentPass;

    private WaitingDialog waitingDialog;

    public static NewPasswordFragment newInstance(short type,String currentPass){
        NewPasswordFragment fragment = new NewPasswordFragment();
        Bundle args = new Bundle();
        args.putShort("type",type);
        args.putString("current_password",currentPass);
        fragment.setArguments(args);
        return fragment;
    }

    public static NewPasswordFragment newInstance(short type,String mobile,String code){
        NewPasswordFragment fragment = new NewPasswordFragment();
        Bundle args = new Bundle();
        args.putShort("type",type);
        args.putString("mobile",mobile);
        args.putString("code",code);
        fragment.setArguments(args);
        return fragment;
    }

    public NewPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getShort("type");

            if (getArguments().containsKey("mobile")) {
                mobile = getArguments().getString("mobile");
            }

            if (getArguments().containsKey("code")) {
                code = getArguments().getString("code");
            }

            if (getArguments().containsKey("current_password")) {
                currentPass = getArguments().getString("current_password");
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.container_toolbar).findViewById(R.id.toolbar);

        ImageButton btnBack = toolbar.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

        ButtonNormal btnContinue = view.findViewById(R.id.btn_save);
        btnContinue.setOnClickListener(this);

        TextViewLight txtTitle = toolbar.findViewById(R.id.txt_title);
        if (type == FORGET_PASS) {
            txtTitle.setText(getString(R.string.forget_password));
        }
        if (type == CHANGE_PASS) {
            txtTitle.setText(getString(R.string.change_password));
        }

        txtInputNewPass = view.findViewById(R.id.txt_input_new_password);
        txtInputNewPass.setTransformationMethod(new PasswordTransformationMethod());

        txtInputRepeatPass = view.findViewById(R.id.txt_input_repeat_password);
        txtInputRepeatPass.setTransformationMethod(new PasswordTransformationMethod());

        imgNewPassVisibility = view.findViewById(R.id.ic_visibility_new_password);
        imgNewPassVisibility.setOnClickListener(this);

        imgRepeatPassVisibility = view.findViewById(R.id.ic_visibility_repeat_password);
        imgRepeatPassVisibility.setOnClickListener(this);

        if (getContext() != null) waitingDialog = new WaitingDialog(getContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                String newPass = txtInputNewPass.getText().toString();
                String repeatNewPass = txtInputRepeatPass.getText().toString();
                if (newPass.equals(repeatNewPass)) {
                    if (type == CHANGE_PASS) {
                        changePassword();
                    }

                    if (type == FORGET_PASS) {
                        forgetCheck();
                    }

                }else {
                    MySnackbar.make(getView(),MySnackbar.Alert,getString(R.string.password_confirmation_wrong)).show();
                }

                break;

            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;

            case R.id.ic_visibility_new_password:
                if (txtInputNewPass.getTransformationMethod() instanceof PasswordTransformationMethod) {
                    txtInputNewPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgNewPassVisibility.setImageResource(R.drawable.ic_visibility_off_24dp);

                } else if (txtInputNewPass.getTransformationMethod() instanceof HideReturnsTransformationMethod) {
                    txtInputNewPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgNewPassVisibility.setImageResource(R.drawable.ic_visibility_24dp);
                }
                break;

            case R.id.ic_visibility_repeat_password:
                if (txtInputRepeatPass.getTransformationMethod() instanceof PasswordTransformationMethod) {
                    txtInputRepeatPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgRepeatPassVisibility.setImageResource(R.drawable.ic_visibility_off_24dp);

                } else if (txtInputRepeatPass.getTransformationMethod() instanceof HideReturnsTransformationMethod) {
                    txtInputRepeatPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgRepeatPassVisibility.setImageResource(R.drawable.ic_visibility_24dp);
                }
                break;
        }
    }

    // Check forget password code and change password:
    private void forgetCheck(){
        if (waitingDialog != null) waitingDialog.show();

        String password = txtInputNewPass.getText().toString();
        AuthRequestHelper requestHandler = AuthRequestHelper.getInstance();

        requestHandler.forgetCheck(mobile,password,code).enqueue(new MyCallback<BasicResource>() {
            @Override
            public void onRequestSuccess(Response<BasicResource> response) {
                if (waitingDialog != null) waitingDialog.dismiss();

                BasicResource basicResource = response.body();
                if (FunctionHelper.isSuccess(getView(),basicResource)) {
                    if (type == FORGET_PASS) {
                        if (getActivity() != null) ((MainActivity)getActivity())
                                .popFragment(LoginFragment.class.getName());
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

    private void changePassword(){
        if (waitingDialog != null) waitingDialog.show();

        String newPassword = txtInputNewPass.getText().toString();
        AccountRequestHelper requestHandler = AccountRequestHelper.getInstance();
        String token = User.getToken(getContext());

        requestHandler.changePassword(token,currentPass,newPassword).enqueue(new MyCallback<BasicResource>() {
            @Override
            public void onRequestSuccess(Response<BasicResource> response) {
                if (waitingDialog != null) waitingDialog.dismiss();

                BasicResource basicResource = response.body();
                if (FunctionHelper.isSuccess(getView(),basicResource)) {
                    if (type == CHANGE_PASS) {
                        if (getActivity() != null) ((MainActivity)getActivity())
                                .popFragment(ProfileFragment.class.getName());
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
