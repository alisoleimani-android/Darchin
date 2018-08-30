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

import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.fragment.BaseFragment;
import co.tinab.darchin.R;
import co.tinab.darchin.view.toolbox.ButtonNormal;
import co.tinab.darchin.view.toolbox.EditTextNormal;
import co.tinab.darchin.view.toolbox.TextViewLight;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentPasswordFragment extends BaseFragment implements View.OnClickListener {
    private EditTextNormal txtInputCurrentPassword;
    private ImageView imgPasswordVisibility;

    public static CurrentPasswordFragment newInstance(){
        return new CurrentPasswordFragment();
    }

    public CurrentPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.container_toolbar).findViewById(R.id.toolbar);

        ImageButton btnBack = toolbar.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

        TextViewLight txtTitle = toolbar.findViewById(R.id.txt_title);
        txtTitle.setText(getString(R.string.change_password));

        txtInputCurrentPassword = view.findViewById(R.id.txt_input_current_password);
        txtInputCurrentPassword.setTransformationMethod(new PasswordTransformationMethod());

        imgPasswordVisibility = view.findViewById(R.id.ic_visibility);
        imgPasswordVisibility.setOnClickListener(this);

        ButtonNormal btnContinue = view.findViewById(R.id.btn_continue);
        btnContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_continue:
                String currentPass = txtInputCurrentPassword.getText().toString();
                if (getActivity() != null) ((MainActivity)getActivity())
                        .pushFragment(NewPasswordFragment.newInstance(NewPasswordFragment.CHANGE_PASS,currentPass));
                break;

            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;

            case R.id.ic_visibility:
                if (txtInputCurrentPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
                    txtInputCurrentPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgPasswordVisibility.setImageResource(R.drawable.ic_visibility_off_24dp);

                } else if (txtInputCurrentPassword.getTransformationMethod() instanceof HideReturnsTransformationMethod) {
                    txtInputCurrentPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgPasswordVisibility.setImageResource(R.drawable.ic_visibility_24dp);
                }
                break;
        }
    }
}
