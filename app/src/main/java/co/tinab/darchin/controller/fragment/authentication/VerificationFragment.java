package co.tinab.darchin.controller.fragment.authentication;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.fragment.user.password.NewPasswordFragment;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.User;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.AuthRequestHelper;
import co.tinab.darchin.model.network.resources.BasicResource;
import co.tinab.darchin.view.dialog.WaitingDialog;
import co.tinab.darchin.view.toolbox.ButtonLight;
import co.tinab.darchin.view.toolbox.EditTextNormal;
import co.tinab.darchin.view.toolbox.MySnackbar;
import co.tinab.darchin.view.toolbox.TextViewLight;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerificationFragment extends Fragment implements View.OnClickListener {
    private String mobile,targetTag;
    public static final short FORGET_PASS = 0;
    public static final short VERIFICATION_REGISTER = 1;
    public static final short VERIFICATION_ONLY = 2;
    private short type = -1;
    private EditTextNormal txtInputCode;
    private int ttl;
    private Timer timer;
    private ButtonLight btnResend;

    // Just for FORGET_PASS
    private boolean codeReceived = false;

    private WaitingDialog waitingDialog;

    public static VerificationFragment newInstance(short type,String mobile,String targetTag){
        VerificationFragment fragment = new VerificationFragment();
        Bundle args = new Bundle();
        args.putShort("type",type);
        args.putString("mobile",mobile);
        args.putString("target_tag",targetTag);
        fragment.setArguments(args);
        return fragment;
    }

    public VerificationFragment() {
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

            if (getArguments().containsKey("target_tag") && getArguments().getString("target_tag") != null) {
                targetTag = getArguments().getString("target_tag");
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextViewLight txtTitle = view.findViewById(R.id.txt_title);

        if (type == FORGET_PASS) {
            txtTitle.setText(getString(R.string.forget_password));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (waitingDialog != null) waitingDialog.show();
                    forgetSend();
                }
            },500);
        }

        if (type == VERIFICATION_REGISTER || type == VERIFICATION_ONLY) {
            txtTitle.setText(getString(R.string.verification));
        }

        if (type == VERIFICATION_ONLY) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (waitingDialog != null) waitingDialog.show();
                    activationResend();
                }
            },500);
        }

        view.findViewById(R.id.btn_back).setOnClickListener(this);
        view.findViewById(R.id.btn_confirm).setOnClickListener(this);

        TextViewLight txtInfo = view.findViewById(R.id.txt_info);
        txtInfo.setText(getString(R.string.verification_info, mobile));

        btnResend = view.findViewById(R.id.btn_resend);
        btnResend.setOnClickListener(this);

        txtInputCode = view.findViewById(R.id.txt_input_code);

        startTimer();

        if (getContext()!=null) waitingDialog = new WaitingDialog(getContext());

    }

    private void startTimer(){
        ttl = 15;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(isAdded() && getActivity() != null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(ttl > 0) {
                                btnResend.setEnabled(false);
                                btnResend.setTextColor(ResourcesCompat.getColor(getResources(),R.color.semi_white,null));
                                btnResend.setText(getString(R.string.resend)
                                        .concat(" (".concat(String.valueOf((ttl--))))
                                        .concat(")")
                                );
                            }else {
                                btnResend.setEnabled(true);
                                btnResend.setTextColor(ResourcesCompat.getColor(getResources(),R.color.colorAccent,null));
                                btnResend.setText(getString(R.string.resend));
                                timer.cancel();
                                timer.purge();
                            }
                        }
                    });
                }
            }
        }, 0, 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_resend:
                if (v.isEnabled()) {
                    if (waitingDialog != null) waitingDialog.show();
                    startTimer();

                    // resend verification code
                    if (type == VERIFICATION_REGISTER || type == VERIFICATION_ONLY) {
                        activationResend();
                    }

                    if (type == FORGET_PASS) {
                        forgetSend();
                    }
                }
                break;

            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;

            case R.id.btn_confirm:
                String code = txtInputCode.getText().toString();
                if (!code.isEmpty()) {
                    if (type == VERIFICATION_REGISTER || type == VERIFICATION_ONLY) {
                        if (waitingDialog != null) waitingDialog.show();
                        activationCheck();
                    }

                    if (type == FORGET_PASS && codeReceived) {
                        if (getActivity() != null) ((MainActivity)getActivity())
                                .pushFragment(NewPasswordFragment.newInstance(FORGET_PASS,mobile,code));
                    }
                }
                break;
        }
    }

    // check activation code
    private void activationCheck(){
        String code = txtInputCode.getText().toString();
        AuthRequestHelper requestHandler = AuthRequestHelper.getInstance();

        requestHandler.activationCheck(mobile,code).enqueue(new MyCallback<BasicResource>() {
            @Override
            public void onRequestSuccess(Response<BasicResource> response) {
                if (waitingDialog != null) waitingDialog.dismiss();

                BasicResource basicResource = response.body();
                if (FunctionHelper.isSuccess(getView(),basicResource)) {

                    // goto main page
                    User.getInstance(getContext()).activateUser();
                    User.getInstance(getContext()).setOrderActive();

                    if (getActivity() != null) ((MainActivity)getActivity()).popFragment(targetTag);

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

    // resend activation code:
    private void activationResend(){
        AuthRequestHelper requestHandler = AuthRequestHelper.getInstance();
        requestHandler.activationResend(mobile).enqueue(new MyCallback<BasicResource>() {
            @Override
            public void onRequestSuccess(Response<BasicResource> response) {
                if (waitingDialog != null) waitingDialog.dismiss();

                BasicResource basicResource = response.body();
                if (!FunctionHelper.isSuccess(getView(),basicResource)) {
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


    // Send sms forget password code to user mobile:
    private void forgetSend(){
        AuthRequestHelper requestHandler = AuthRequestHelper.getInstance();

        requestHandler.forgetSend(mobile).enqueue(new MyCallback<BasicResource>() {
            @Override
            public void onRequestSuccess(Response<BasicResource> response) {
                if (waitingDialog != null) waitingDialog.dismiss();

                BasicResource basicResource = response.body();
                if (FunctionHelper.isSuccess(getView(),basicResource)) {
                    codeReceived = true;

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
