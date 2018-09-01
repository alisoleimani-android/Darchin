package co.tinab.darchin.view.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.tools.Display;
import co.tinab.darchin.view.component.LoadingView;

public class ProductImageDialog extends DialogFragment {

    private String address;

    public static ProductImageDialog newInstance(String address){
        ProductImageDialog dialog = new ProductImageDialog();
        Bundle args = new Bundle();
        args.putString("address",address);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            address = getArguments().getString("address");
        }

        setStyle(DialogFragment.STYLE_NO_TITLE,R.style.ProductImageDialogTheme);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_product_image,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final LoadingView loadingView = view.findViewById(R.id.loading_view);
        loadingView.show();

        ImageView imageView = view.findViewById(R.id.img);
        Picasso.with(view.getContext())
                .load(address.replace("small","large"))
                .resize(Display.getWidthOfDevice(view.getContext()),0)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        loadingView.hide();
                    }

                    @Override
                    public void onError() {
                        loadingView.hide();
                        dismiss();
                    }
                });
    }
}
