package co.tinab.darchin.controller.fragment.store;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import co.tinab.darchin.controller.adapter.CommentListAdapter;
import co.tinab.darchin.controller.adapter.HorizontalProductPictureListAdapter;
import co.tinab.darchin.controller.fragment.BaseFragment;
import co.tinab.darchin.controller.tools.Display;
import co.tinab.darchin.controller.tools.ItemClickSupport;
import co.tinab.darchin.model.store.Product;
import co.tinab.darchin.model.order.Comment;
import co.tinab.darchin.R;
import co.tinab.darchin.view.toolbox.MyRecyclerView;
import co.tinab.darchin.view.toolbox.TextViewLight;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetailFragment extends BaseFragment implements View.OnClickListener {
    private Product product;
    private List<Product> pictureList = new ArrayList<>();
    private List<Comment> commentList = new ArrayList<>();
    private HorizontalProductPictureListAdapter pictureListAdapter;
    private CommentListAdapter commentListAdapter;
    private ImageView img;

    public static ProductDetailFragment newInstance(Product product){
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("product", product);
        fragment.setArguments(args);
        return fragment;
    }

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = getArguments().getParcelable("product");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextViewLight txtTitle = view.findViewById(R.id.txt_title);
        txtTitle.setText(product.getName());

        view.findViewById(R.id.btn_back).setOnClickListener(this);
        view.findViewById(R.id.btn_add).setOnClickListener(this);

        img = view.findViewById(R.id.img);
        Picasso.with(getContext())
                .load(product.getPicture())
                .resize(Display.getWidthOfDevice(getContext()),0)
                .onlyScaleDown()
                .error(R.drawable.ic_product_placeholder)
                .placeholder(R.drawable.ic_product_placeholder)
                .into(img);

        MyRecyclerView recyclerViewPictures = view.findViewById(R.id.recycler_view_user_pictures);
        pictureListAdapter = new HorizontalProductPictureListAdapter(pictureList);
        recyclerViewPictures.setAdapter(pictureListAdapter);
        ItemClickSupport.addTo(recyclerViewPictures).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Picasso.with(getContext())
                        .load(pictureList.get(position).getPicture())
                        .resize(Display.getWidthOfDevice(getContext()),0)
                        .onlyScaleDown()
                        .error(R.drawable.ic_product_placeholder)
                        .placeholder(R.drawable.ic_product_placeholder)
                        .into(img);
            }
        });

        MyRecyclerView recyclerViewComments = view.findViewById(R.id.recycler_view_comments);
        commentListAdapter = new CommentListAdapter(commentList);
        recyclerViewComments.setAdapter(commentListAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestData();
            }
        },500);
    }

    // request to get Data
    private void requestData(){}

    private void bindPictureSentByUserView(List<Product> pictureList){
        this.pictureList.addAll(pictureList);
        this.pictureListAdapter.notifyDataSetChanged();
    }

    private void bindCommentListView(List<Comment> commentList){
        this.commentList.addAll(commentList);
        this.commentListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;

            case R.id.btn_add:
                break;
        }
    }
}
