package co.tinab.darchin.controller.fragment.other;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.Setting;
import co.tinab.darchin.model.User;
import co.tinab.darchin.view.component.SectionView;
import co.tinab.darchin.view.toolbox.TextViewLight;

/**
 * A simple {@link Fragment} subclass.
 */
public class SupportFragment extends Fragment implements View.OnClickListener, SectionView.RequestCompleteListener {
    private Map<String,String> map = new LinkedHashMap<>();

    public static SupportFragment newInstance(){
        return new SupportFragment();
    }

    public SupportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        map.putAll(Setting.getSettings(getContext()));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_support, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((TextViewLight)view.findViewById(R.id.txt_title)).setText(R.string.nav_item_support);

        view.findViewById(R.id.ic_telegram).setOnClickListener(this);
        view.findViewById(R.id.ic_instagram).setOnClickListener(this);
        view.findViewById(R.id.btn_call).setOnClickListener(this);
        view.findViewById(R.id.btn_back).setOnClickListener(this);

        SectionView sectionView = view.findViewById(R.id.container_section);
        sectionView.setOnRequestCompleteListener(this);

        String aboutUs = map.get("about_us");
        if (aboutUs != null)
            ((TextViewLight)view.findViewById(R.id.txt_about_us)).setText(aboutUs);

        String address = map.get("address");
        if (address != null)
            ((TextViewLight)view.findViewById(R.id.txt_address)).setText(address);

        // get sections
        if (FunctionHelper.isConnected(getContext())) {
            sectionView.requestData("support","top");
        }else {
            sectionView.bind(User.getInstance(getContext()).getSections(getContext(),"support","top"));
        }
    }

    private boolean isIntentAvailable(@Nullable Context ctx, Intent intent) {
        if (ctx != null) {
            final PackageManager packageManager = ctx.getPackageManager();
            List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            return list.size() > 0;
        }else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;

            case R.id.ic_telegram:
                String telegramId = map.get("telegram_channel");
                if (telegramId == null) {
                    telegramId = "darchin_app";
                }

                String link = "http://telegram.me/_u/".concat(telegramId);
                Uri uri = Uri.parse(link);
                Intent telegram = new Intent(Intent.ACTION_VIEW, uri);
                telegram.setPackage("com.telegram.android");

                if (isIntentAvailable(getActivity(), telegram)) {
                    startActivity(telegram);
                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
                }
                break;

            case R.id.ic_instagram:
                String instagramId = map.get("instagram");
                if (instagramId == null) {
                    instagramId = "darchin_app";
                }

                link = "http://instagram.com/_u/".concat(instagramId);
                uri = Uri.parse(link);
                Intent instagram = new Intent(Intent.ACTION_VIEW, uri);
                instagram.setPackage("com.instagram.android");

                if (isIntentAvailable(getActivity(), instagram)){
                    startActivity(instagram);
                } else{
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
                }
                break;

            case R.id.btn_call:
                String phone = map.get("support_phone");
                if (phone == null || phone.isEmpty()) {
                    phone = "+982332300362";
                }
                Uri number = Uri.parse("tel:".concat(phone));
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
                break;
        }
    }

    @Override
    public void onRequestCompleted() {

    }

    @Override
    public void onRequestFailed() {

    }
}
