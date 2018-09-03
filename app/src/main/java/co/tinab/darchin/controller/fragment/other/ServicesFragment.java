package co.tinab.darchin.controller.fragment.other;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.User;
import co.tinab.darchin.view.component.EmptyView;
import co.tinab.darchin.view.component.LoadingView;
import co.tinab.darchin.view.component.SectionView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServicesFragment extends Fragment implements View.OnClickListener, SectionView.RequestCompleteListener {

    private EmptyView emptyView;
    private LoadingView loadingView;

    public static ServicesFragment newInstance(){
        return new ServicesFragment();
    }

    public ServicesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_services, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((TextView)view.findViewById(R.id.txt_title)).setText(R.string.services_packages);
        view.findViewById(R.id.btn_back).setOnClickListener(this);

        emptyView = view.findViewById(R.id.empty_view);
        loadingView = view.findViewById(R.id.loading_view);
        loadingView.show();

        SectionView sectionView = view.findViewById(R.id.container_section);
        sectionView.setOnRequestCompleteListener(this);
        if (FunctionHelper.isConnected(getContext())) {
            sectionView.requestData("services","top");
        }else {
            sectionView.bind(User.getInstance(getContext()).getSections(getContext(),"services","top"));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;
        }
    }

    @Override
    public void onRequestCompleted() {
        loadingView.hide();
    }

    @Override
    public void onRequestFailed() {
        loadingView.hide();
        emptyView.show();
    }
}
