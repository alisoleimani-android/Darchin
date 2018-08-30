package co.tinab.darchin.controller.fragment.user;


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
import co.tinab.darchin.model.network.request_helpers.AccountRequestHelper;
import co.tinab.darchin.model.network.resources.BasicResource;
import co.tinab.darchin.model.network.resources.UserResource;
import co.tinab.darchin.model.User;
import co.tinab.darchin.R;
import co.tinab.darchin.view.dialog.WaitingDialog;
import co.tinab.darchin.view.toolbox.ButtonNormal;
import co.tinab.darchin.view.toolbox.EditTextLight;
import co.tinab.darchin.view.toolbox.MySnackbar;
import co.tinab.darchin.view.toolbox.TextViewLight;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends BaseFragment implements View.OnClickListener{
    private EditTextLight txtInputName,txtInputPhone,txtInputTel;
    private WaitingDialog waitingDialog;

    public static EditProfileFragment newInstance(){
        return new EditProfileFragment();
    }

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.container_toolbar).findViewById(R.id.toolbar);

        ImageButton btnBack = toolbar.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

        TextViewLight txtTitle = toolbar.findViewById(R.id.txt_title);
        txtTitle.setText(getString(R.string.user_profile));

        ButtonNormal btnSave = view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);

        txtInputName = view.findViewById(R.id.txt_input_name);
        txtInputName.setText(User.getInstance(getContext()).getName());

        txtInputPhone = view.findViewById(R.id.txt_input_mobile);
        txtInputPhone.setText(User.getInstance(getContext()).getUsername());

        txtInputTel = view.findViewById(R.id.txt_input_phone);
        txtInputTel.setText(User.getInstance(getContext()).getPhone());

        if (getContext()!=null) waitingDialog = new WaitingDialog(getContext());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                // request to save info
                editAccountInfo();
                break;
            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;
        }
    }

    private void editAccountInfo(){
        if (waitingDialog != null) waitingDialog.show();

        String name = txtInputName.getText().toString();
        String mobile = txtInputPhone.getText().toString();
        String tel = txtInputTel.getText().toString();
        String token = User.getToken(getContext());

        AccountRequestHelper requestHandler = AccountRequestHelper.getInstance();
        requestHandler.editAccountInfo(token,name,mobile,tel).enqueue(new MyCallback<BasicResource>() {

            @Override
            public void onRequestSuccess(Response<BasicResource> response) {
                if (waitingDialog != null) waitingDialog.dismiss();

                BasicResource basicResource = response.body();
                if (FunctionHelper.isSuccess(getView(),basicResource)) {
                    // update account info
                    getAccountInfo();

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

    private void getAccountInfo(){
        String token = User.getToken(getContext());

        AccountRequestHelper requestHandler = AccountRequestHelper.getInstance();
        requestHandler.getAccountInfo(token).enqueue(new MyCallback<UserResource>() {
            @Override
            public void onRequestSuccess(Response<UserResource> response) {
                UserResource userResponse = response.body();
                if (FunctionHelper.isSuccess(null,userResponse) && userResponse.getUser() != null) {
                    // save user
                    User.saveInstance(getContext(),userResponse.getUser());
                }

                // pop fragment:
                if (getActivity() != null) getActivity().onBackPressed();
            }

            @Override
            public void onRequestFailed(@Nullable List<String> messages) {
                // pop fragment:
                if (getActivity() != null) getActivity().onBackPressed();
            }

            @Override
            public void unAuthorizedDetected() {
                if (getActivity() != null) ((MainActivity)getActivity()).logout();
            }
        });
    }
}
