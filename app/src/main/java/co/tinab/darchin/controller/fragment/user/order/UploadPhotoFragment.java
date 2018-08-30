package co.tinab.darchin.controller.fragment.user.order;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.adapter.OrderItemsSingleChoiceAdapter;
import co.tinab.darchin.controller.tools.SimpleDividerItemDecoration;
import co.tinab.darchin.model.store.ProductItem;
import co.tinab.darchin.R;
import co.tinab.darchin.view.toolbox.MyRecyclerView;
import co.tinab.darchin.view.toolbox.MySnackbar;
import co.tinab.darchin.view.toolbox.TextViewLight;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadPhotoFragment extends Fragment implements View.OnClickListener {
    private List<ProductItem> itemList;
    private ProductItem selectedItem;
    private String path;

    public static UploadPhotoFragment newInstance(String path,List<ProductItem> itemList){
        UploadPhotoFragment fragment = new UploadPhotoFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("item_list", (ArrayList<? extends Parcelable>) itemList);
        args.putString("path",path);
        fragment.setArguments(args);
        return fragment;
    }

    public UploadPhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemList = getArguments().getParcelableArrayList("item_list");
            path = getArguments().getString("path");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload_photo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RoundedImageView img = view.findViewById(R.id.img);
        // set photo
        Bitmap bitmapImage = BitmapFactory.decodeFile(path);
        int nh = (int) ( bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()) );
        Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
        img.setImageBitmap(scaled);

        MyRecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        OrderItemsSingleChoiceAdapter adapter = new OrderItemsSingleChoiceAdapter(itemList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        adapter.setOnItemSelectedListener(new OrderItemsSingleChoiceAdapter.ItemSelectedListener() {
            @Override
            public void onItemSelected(ProductItem item) {
                selectedItem = item;
            }
        });

        view.findViewById(R.id.btn_submit).setOnClickListener(this);
        view.findViewById(R.id.btn_back).setOnClickListener(this);

        TextViewLight txtTitle = view.findViewById(R.id.txt_title);
        txtTitle.setText(getString(R.string.upload_photo));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                if (getActivity() != null){
                    if (selectedItem != null) {
                        Intent intent = new Intent();
                        intent.putExtra("item",selectedItem);
                        ((MainActivity)getActivity())
                                .popFragment(intent,PostCommentFragment.class.getName());
                    }else {
                        Snackbar snackbar = MySnackbar
                                .make(getView(),MySnackbar.Alert,R.string.please_select_an_item);
                        snackbar.show();
                    }
                }
                break;

            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;
        }
    }
}
