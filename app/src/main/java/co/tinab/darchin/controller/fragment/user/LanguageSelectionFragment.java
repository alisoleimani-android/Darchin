package co.tinab.darchin.controller.fragment.user;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.LinkedHashMap;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.adapter.LanguageListAdapter;
import co.tinab.darchin.controller.tools.SimpleDividerItemDecoration;
import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.User;
import co.tinab.darchin.view.toolbox.ButtonNormal;
import co.tinab.darchin.view.toolbox.MyRecyclerView;
import co.tinab.darchin.view.toolbox.TextViewLight;

/**
 * A simple {@link Fragment} subclass.
 */
public class LanguageSelectionFragment extends Fragment implements View.OnClickListener {
    private LinkedHashMap<String,String> hashMap = new LinkedHashMap<>();
    private String language = "فارسی";

    public static LanguageSelectionFragment newInstance(){
        return new LanguageSelectionFragment();
    }

    public LanguageSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_language_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.container_toolbar).findViewById(R.id.toolbar);
        ImageButton btnBack = toolbar.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

        TextViewLight txtTitle = toolbar.findViewById(R.id.txt_title);
        txtTitle.setText(getString(R.string.selection_language));

        fillLanguageMap();

        MyRecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        LanguageListAdapter adapter = new LanguageListAdapter(hashMap);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemSelectedListener(new LanguageListAdapter.ItemSelectedListener() {
            @Override
            public void onItemSelected(String name) {
                language = name;
            }
        });

        ButtonNormal btnConfirm = view.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);
    }

    private void fillLanguageMap(){
        hashMap.put("فارسی","fa");
        hashMap.put("English","en");
    }

    private void changeLocale(String language){
        if (getActivity() != null && language != null) {
            if (!getActivity().getResources().getConfiguration().locale.getLanguage().equals(language)) {
                // Save language to default preference
                SharedPreferences settings = getActivity().getSharedPreferences(Constant.Pref_Name, Context.MODE_PRIVATE);
                settings.edit().putString(Constant.Pref_Locale,language).apply();

                // Restart application
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
            }else {
                getActivity().onBackPressed();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;

            case R.id.btn_confirm:
                changeLocale(hashMap.get(language));
                break;
        }
    }

    @Override
    public void onDestroyView() {
        // set has selected language to pref
        User.getInstance(getContext()).setSelectedLanguage(getContext());

        super.onDestroyView();
    }
}
