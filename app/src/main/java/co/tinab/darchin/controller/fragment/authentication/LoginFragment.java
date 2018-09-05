package co.tinab.darchin.controller.fragment.authentication;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import co.ronash.pushe.Pushe;
import co.tinab.darchin.R;
import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.User;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.AuthRequestHelper;
import co.tinab.darchin.model.network.resources.UserResource;
import co.tinab.darchin.view.dialog.WaitingDialog;
import co.tinab.darchin.view.toolbox.EditTextLight;
import co.tinab.darchin.view.toolbox.MySnackbar;
import co.tinab.darchin.view.toolbox.TextViewLight;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    private EditTextLight txtInputPassword;
    private ImageView imgPassVisibility;
    private WaitingDialog waitingDialog;
    private String mobile;
    private String targetTag;

    public static LoginFragment newInstance(String targetTag,String mobile){
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString("mobile",mobile);
        args.putString("target_tag",targetTag);
        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mobile = getArguments().getString("mobile");
            targetTag = getArguments().getString("target_tag");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextViewLight txtTitle = view.findViewById(R.id.txt_title);
        txtTitle.setText(getString(R.string.login_to_account));

        if (getContext()!=null) waitingDialog = new WaitingDialog(getContext());

        view.findViewById(R.id.btn_back).setOnClickListener(this);
        view.findViewById(R.id.btn_login).setOnClickListener(this);
        view.findViewById(R.id.btn_forget_password).setOnClickListener(this);
        txtInputPassword = view.findViewById(R.id.txt_input_password);

        TextViewLight txtUsername = view.findViewById(R.id.txt_username);
        txtUsername.setText(mobile);

        imgPassVisibility = view.findViewById(R.id.ic_visibility_password);
        imgPassVisibility.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;

            case R.id.btn_login:
                if (txtInputPassword.getText().toString().trim().length() >= 4) {
                    if (waitingDialog != null) waitingDialog.show();
                    login();

                }else {
                    MySnackbar.make(getView(),MySnackbar.Alert,
                            R.string.password_should_be_more_than_some_char).show();
                }
                break;

            case R.id.btn_forget_password:
                if (getActivity() != null) ((MainActivity)getActivity())
                        .pushFragment(VerificationFragment.newInstance(VerificationFragment.FORGET_PASS,mobile,null));
                break;

            case R.id.ic_visibility_password:
                if (txtInputPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
                    txtInputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgPassVisibility.setImageResource(R.drawable.ic_visibility_off_24dp);

                } else if (txtInputPassword.getTransformationMethod() instanceof HideReturnsTransformationMethod) {
                    txtInputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgPassVisibility.setImageResource(R.drawable.ic_visibility_24dp);
                }
                break;
        }
    }

    private void login(){
        String password = txtInputPassword.getText().toString();

        AuthRequestHelper requestHandler = AuthRequestHelper.getInstance();
        Call<UserResource> call = requestHandler.login(mobile,password);
        call.enqueue(new MyCallback<UserResource>() {
            @Override
            public void onRequestSuccess(Response<UserResource> response) {
                if (waitingDialog != null) waitingDialog.dismiss();

                UserResource userResponse = response.body();
                if (FunctionHelper.isSuccess(getView(),userResponse) && userResponse.getUser() != null) {

                    // get user and save it to db
                    User.saveInstance(getContext(),userResponse.getUser());
                    User.getInstance(getContext()).setLoggedIn(getContext(),true);
                    User.setToken(getContext(),userResponse.getUser().getToken());

                    // send oneSignal Tags:
                    try {
                        JSONObject tags = new JSONObject();
                        tags.put("id", User.getInstance(getContext()).getUserID());
                        tags.put("username", User.getInstance(getContext()).getUsername());
                        OneSignal.sendTags(tags);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // send Pushe Tags:
                    Pushe.subscribe(getContext(), String.valueOf(User.getInstance(getContext()).getUserID()));
                    Pushe.subscribe(getContext(), User.getInstance(getContext()).getUsername());

                    // user should active the account
                    if (userResponse.getUser().getStatus().equals("deactive")) {
                        // move to verification
                        if (getActivity() != null) ((MainActivity)getActivity()).pushFragment(VerificationFragment.newInstance(
                                VerificationFragment.VERIFICATION_ONLY,User.getInstance(getContext()).getUsername(),targetTag));
                    }

                    // user is active
                    if (userResponse.getUser().getStatus().equals("active")) {
                        if (getActivity() != null) ((MainActivity)getActivity()).popFragment(targetTag);
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
