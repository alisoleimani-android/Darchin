package co.tinab.darchin.controller.tools;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import co.tinab.darchin.R;
import co.tinab.darchin.view.toolbox.ButtonNormal;
import co.tinab.darchin.view.toolbox.TextViewLight;
import co.tinab.darchin.view.toolbox.TextViewNormal;

public class AppIntroSlideFragment extends Fragment implements View.OnClickListener {
    private int drawableRes;
    private String title,desc;
    private boolean isLastOne;

    public static AppIntroSlideFragment newInstance(boolean isLastOne,int drawableRes,String title,String desc) {
        AppIntroSlideFragment appIntroSlideFragment = new AppIntroSlideFragment();
        Bundle args = new Bundle();
        args.putInt("drawableRes",drawableRes);
        args.putBoolean("isLastOne",isLastOne);
        args.putString("title",title);
        args.putString("desc",desc);
        appIntroSlideFragment.setArguments(args);
        return appIntroSlideFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            drawableRes = getArguments().getInt("drawableRes");
            isLastOne = getArguments().getBoolean("isLastOne",false);
            title = getArguments().getString("title");
            desc = getArguments().getString("desc");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_intro_slide, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Picasso.with(getContext())
                .load(drawableRes)
                .resizeDimen(R.dimen.intro_img_width,R.dimen.intro_img_height)
                .onlyScaleDown()
                .into((ImageView) view.findViewById(R.id.img));

        TextViewNormal txtTitle = view.findViewById(R.id.txt_title);
        txtTitle.setText(title);

        TextViewLight txtDesc = view.findViewById(R.id.txt_desc);
        txtDesc.setText(desc);

        if (isLastOne) {
            ButtonNormal btnAction = view.findViewById(R.id.btn_action);
            btnAction.setVisibility(View.VISIBLE);
            btnAction.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_action) {
            if (getActivity() != null) getActivity().onBackPressed();
        }
    }
}
