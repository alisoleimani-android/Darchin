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
import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.AuthRequestHelper;
import co.tinab.darchin.model.network.resources.UserResource;
import co.tinab.darchin.model.User;
import co.tinab.darchin.R;
import co.tinab.darchin.view.dialog.WaitingDialog;
import co.tinab.darchin.view.toolbox.EditTextLight;
import co.tinab.darchin.view.toolbox.MySnackbar;
import co.tinab.darchin.view.toolbox.TextViewLight;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    private String mobile;
    private String targetTag;
    private WaitingDialog waitingDialog;
    private EditTextLight txtInputName,txtInputPass,txtInputRepeatPass;
    private ImageView imgPassVisibility,imgRepeatPassVisibility;

    public static RegisterFragment newInstance(String targetTag,String mobile){
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString("mobile",mobile);
        args.putString("target_tag",targetTag);
        fragment.setArguments(args);
        return fragment;
    }

    public RegisterFragment() {
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
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextViewLight txtTitle = view.findViewById(R.id.txt_title);
        txtTitle.setText(getString(R.string.register));

        if (getContext()!=null) waitingDialog = new WaitingDialog(getContext());

        view.findViewById(R.id.btn_back).setOnClickListener(this);
        view.findViewById(R.id.btn_register).setOnClickListener(this);

        txtInputName = view.findViewById(R.id.txt_input_name);
        txtInputPass = view.findViewById(R.id.txt_input_password);
        txtInputRepeatPass = view.findViewById(R.id.txt_input_repeat_password);

        imgPassVisibility = view.findViewById(R.id.ic_visibility_password);
        imgPassVisibility.setOnClickListener(this);

        imgRepeatPassVisibility = view.findViewById(R.id.ic_visibility_repeat_password);
        imgRepeatPassVisibility.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;

            case R.id.btn_register:
                if (txtInputPass.getText().toString().trim().length() >= 4) {
                    if (txtInputPass.getText().toString().equals(txtInputRepeatPass.getText().toString())) {
                        if (waitingDialog!=null) waitingDialog.show();
                        register();
                    }else {
                        MySnackbar.make(getView(),MySnackbar.Alert,getString(R.string.password_confirmation_wrong)).show();
                    }
                }else {
                    MySnackbar.make(getView(),MySnackbar.Alert,
                            R.string.password_should_be_more_than_some_char).show();
                }

                break;

            case R.id.ic_visibility_password:
                if (txtInputPass.getTransformationMethod() instanceof PasswordTransformationMethod) {
                    txtInputPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgPassVisibility.setImageResource(R.drawable.ic_visibility_off_24dp);

                } else if (txtInputPass.getTransformationMethod() instanceof HideReturnsTransformationMethod) {
                    txtInputPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgPassVisibility.setImageResource(R.drawable.ic_visibility_24dp);
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

    private void register(){
        String name = txtInputName.getText().toString();
        String username = mobile;
        String password = txtInputPass.getText().toString();
        AuthRequestHelper requestHandler = AuthRequestHelper.getInstance();

        requestHandler.register(name,username,password).enqueue(new MyCallback<UserResource>() {
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

                    // move to verification
                    if (getActivity() != null) ((MainActivity)getActivity()).pushFragment(VerificationFragment.newInstance(
                            VerificationFragment.VERIFICATION_REGISTER, User.getInstance(getContext()).getUsername(),targetTag));

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
