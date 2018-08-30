package co.tinab.darchin.view.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import co.tinab.darchin.controller.tools.Display;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.section.Banner;
import co.tinab.darchin.model.section.Section;
import co.tinab.darchin.R;

/**
 * Created by A.S.R on 1/2/2018.
 */

class BannerSectionView implements View.OnClickListener {
    private ViewGroup parent;
    private View view;
    private Banner banner;

    BannerSectionView(ViewGroup parent) {
        this.parent = parent;
    }

    public View getView(){
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_banner_section_view,parent,false);
        // set layout params
        view.setPadding(0,0,0,5);
        view.setOnClickListener(this);
        return view;
    }

    void requestData(Section section){
        List<Banner> banners = getBanner(section.getContents());
        if (!banners.isEmpty()) {
            bind(banners.get(0));
        }else {
            view.setVisibility(View.GONE);
        }
    }

    private List<Banner> getBanner(JsonArray jsonArray){
        Gson gson = new Gson();
        List<Banner> banners = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray){
            Banner banner = gson.fromJson(jsonElement,Banner.class);
            banners.add(banner);
        }
        return banners;
    }

    private void bind(final Banner banner){
        this.banner = banner;
        ImageView imgBanner = view.findViewById(R.id.img);

        if (FunctionHelper.isConnected(parent.getContext())) {
            Picasso.with(parent.getContext())
                    .load(banner.getPicture())
                    .resize(Display.getWidthOfDevice(parent.getContext()),0)
                    .onlyScaleDown()
                    .placeholder(R.drawable.ic_banner_placeholder)
                    .error(R.drawable.ic_banner_placeholder)
                    .into(imgBanner);
        }else {
            Picasso.with(parent.getContext())
                    .load(banner.getPicture())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .resize(Display.getWidthOfDevice(parent.getContext()),0)
                    .onlyScaleDown()
                    .placeholder(R.drawable.ic_banner_placeholder)
                    .error(R.drawable.ic_banner_placeholder)
                    .into(imgBanner);
        }
    }

    @Override
    public void onClick(View v) {
        FunctionHelper.gotoLink(parent.getContext(),
                banner.getLinkType(),banner.getLink(),banner.getName());
    }
}
