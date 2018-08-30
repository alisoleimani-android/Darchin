package co.tinab.darchin.controller.fragment.user.credit;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

import co.tinab.darchin.controller.fragment.BaseFragment;
import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.Setting;
import co.tinab.darchin.model.User;
import co.tinab.darchin.R;
import co.tinab.darchin.view.toolbox.MySnackbar;
import co.tinab.darchin.view.toolbox.TextViewLight;
import co.tinab.darchin.view.toolbox.TextViewNormal;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FreeCreditFragment extends BaseFragment implements View.OnClickListener {
    private String shareLink;
    private Map<String,String> settings;

    public static FreeCreditFragment newInstance(){
        return new FreeCreditFragment();
    }

    public FreeCreditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_free_credit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextViewLight txtTitle = view.findViewById(R.id.txt_title);
        txtTitle.setText(getString(R.string.nav_item_share));

        view.findViewById(R.id.btn_back).setOnClickListener(this);
        view.findViewById(R.id.container_invitation_link).setOnClickListener(this);
        view.findViewById(R.id.btn_send).setOnClickListener(this);

        settings = Setting.getSettings(getContext());
        if (settings != null) {
            if (settings.containsKey("invitation_title")) {
                TextViewNormal txtInvitationTitle = view.findViewById(R.id.txt_invitation_title);
                txtInvitationTitle.setText(settings.get("invitation_title"));
            }

            if (settings.containsKey("invitation_description")) {
                TextViewLight txtInvitationDescription = view.findViewById(R.id.txt_invitation_desc);
                txtInvitationDescription.setText(settings.get("invitation_description"));
            }
        }

        TextViewLight txtLink = view.findViewById(R.id.txt_invitation_link);
        shareLink = Constant.REFERRAL_LINK.concat(User.getInstance(getContext()).getReferralLink());
        txtLink.setText(shareLink);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;

            case R.id.container_invitation_link:
                if (getContext() != null) {
                    ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Invitation_Link", shareLink);
                    if (clipboard != null) clipboard.setPrimaryClip(clip);
                    Snackbar snackbar = MySnackbar.make(getView(), MySnackbar.Info, R.string.invitation_link_copied_to_clipboard_successfully);
                    snackbar.show();
                }
                break;

            case R.id.btn_send:
                if (getContext() != null) {
                    String description;
                    if (settings != null && settings.containsKey("invitation_link_description")) {
                        description = settings.get("invitation_link_description");
                    }else {
                        description = getString(R.string.invitation_link_info);
                    }

                    Context mContext = getContext();
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.app_name));
                    i.putExtra(Intent.EXTRA_TEXT, description.concat("\n\n").concat(shareLink));
                    mContext.startActivity(Intent.createChooser(i, mContext.getString(R.string.send_invitation)));
                }
                break;
        }
    }
}