package co.tinab.darchin.controller.adapter;

import java.util.List;

import co.tinab.darchin.model.section.Slider;
import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

/**
 * Created by A.S.R on 4/14/2018.
 */

public class MySliderAdapter extends SliderAdapter {
    private List<Slider> sliders;

    public MySliderAdapter(List<Slider> sliders){
        this.sliders = sliders;
    }

    @Override
    public int getItemCount() {
        return sliders.size();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {
        imageSlideViewHolder.bindImageSlide(sliders.get(position).getPicture());
    }
}
