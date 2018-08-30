package co.tinab.darchin.controller.fragment.order;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.tinab.darchin.model.store.Cart;
import co.tinab.darchin.R;
import co.tinab.darchin.view.toolbox.EditTextLight;
import co.tinab.darchin.view.toolbox.TextViewLight;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDescriptionFragment extends Fragment implements View.OnClickListener {
    private EditTextLight txtInputDesc;
    private Cart cart;

    public static OrderDescriptionFragment newInstance(Cart cart){
        OrderDescriptionFragment fragment = new OrderDescriptionFragment();
        Bundle args = new Bundle();
        args.putParcelable("cart",cart);
        fragment.setArguments(args);
        return fragment;
    }

    public OrderDescriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cart = getArguments().getParcelable("cart");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_description, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextViewLight txtTitle = view.findViewById(R.id.txt_title);
        txtTitle.setText(getString(R.string.order_description));

        txtInputDesc = view.findViewById(R.id.txt_input_desc);
        if (cart.getDescription() != null && !cart.getDescription().isEmpty()) {
            txtInputDesc.setText(cart.getDescription());
        }

        view.findViewById(R.id.btn_back).setOnClickListener(this);
        view.findViewById(R.id.btn_submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;

            case R.id.btn_submit:
                if (getActivity() != null) getActivity().onBackPressed();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        if (!txtInputDesc.getText().toString().isEmpty()) {
            cart.setDescription(txtInputDesc.getText().toString());
        }else {
            cart.setDescription(null);
        }

        super.onDestroyView();
    }
}
