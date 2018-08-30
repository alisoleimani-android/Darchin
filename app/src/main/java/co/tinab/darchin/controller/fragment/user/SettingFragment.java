package co.tinab.darchin.controller.fragment.user;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.fragment.user.address.AddressFragment;
import co.tinab.darchin.controller.fragment.user.password.CurrentPasswordFragment;
import co.tinab.darchin.controller.fragment.user.credit.CreditFragment;
import co.tinab.darchin.R;
import co.tinab.darchin.view.dialog.QuestionDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {
    public static SettingFragment newInstance(){
        return new SettingFragment();
    }

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewGroup btnUserProfile,btnAddresses,btnUserAccount,btnChangePassword,btnLogout,
        btnLanguage;

        btnUserProfile = view.findViewById(R.id.btn_user_profile);
        btnUserProfile.setOnClickListener(this);

        btnAddresses = view.findViewById(R.id.btn_addresses);
        btnAddresses.setOnClickListener(this);

        btnUserAccount = view.findViewById(R.id.btn_user_account);
        btnUserAccount.setOnClickListener(this);

        btnLanguage = view.findViewById(R.id.btn_language);
        btnLanguage.setOnClickListener(this);

        btnChangePassword = view.findViewById(R.id.btn_change_password);
        btnChangePassword.setOnClickListener(this);

        btnLogout = view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_user_profile:
                if (getActivity() != null)
                    ((MainActivity)getActivity()).pushFragment(EditProfileFragment.newInstance());
                break;

            case R.id.btn_addresses:
                if (getActivity() != null)
                    ((MainActivity)getActivity()).pushFragment(AddressFragment.newInstance());
                break;

            case R.id.btn_user_account:
                if (getActivity() != null)
                    ((MainActivity)getActivity()).pushFragment(CreditFragment.newInstance());
                break;

            case R.id.btn_language:
                // change locale
                if (getActivity() != null)
                    ((MainActivity)getActivity()).pushFragment(LanguageSelectionFragment.newInstance());
                break;

            case R.id.btn_change_password:
                if (getActivity() != null)
                    ((MainActivity)getActivity()).pushFragment(CurrentPasswordFragment.newInstance());
                break;

            case R.id.btn_logout:
                String desc = getString(R.string.do_you_want_to_logout);
                String posName = getString(R.string.yes);
                String negativeName = getString(R.string.no);

                QuestionDialog dialog = QuestionDialog.newInstance(posName,negativeName,desc);
                dialog.setListener(new QuestionDialog.ClickListener() {

                    @Override
                    public void onPositiveBtnClicked(DialogFragment dialogFragment) {
                        dialogFragment.dismiss();
                        if (getActivity() != null) ((MainActivity)getActivity()).logout();
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
}
