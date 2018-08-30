package co.tinab.darchin.view.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import co.tinab.darchin.controller.adapter.MySliderAdapter;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.controller.tools.MySliderImageLoadingService;
import co.tinab.darchin.model.section.Section;
import co.tinab.darchin.model.section.Slider;
import co.tinab.darchin.R;
import ss.com.bannerslider.event.OnSlideClickListener;

import static android.view.View.GONE;

/**
 * Created by A.S.R on 1/1/2018.
 */

class SliderSectionView {
    private ViewGroup parent;
    private View view;

    SliderSectionView(ViewGroup parent) {
        this.parent = parent;
    }
    public View getView(){
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_slider_section_view,parent,false);
        view.setPadding(0,0,0,5);
        return view;
    }

    void requestData(Section section){
        List<Slider> sliders = getSliders(section.getContents());
        if (!sliders.isEmpty()) {
            bind(sliders);

        }else {
            view.setVisibility(GONE);
        }
    }

    private List<Slider> getSliders(JsonArray jsonArray){
        Gson gson = new Gson();
        List<Slider> sliders = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray){
            Slider slider = gson.fromJson(jsonElement,Slider.class);
            sliders.add(slider);
        }
        return sliders;
    }

    private void bind(final List<Slider> sliders){
        ss.com.bannerslider.Slider slider = view.findViewById(R.id.slider);
        ss.com.bannerslider.Slider.init(new MySliderImageLoadingService(parent.getContext()));
        slider.setAdapter(new MySliderAdapter(sliders));
        slider.setOnSlideClickListener(new OnSlideClickListener() {
            @Override
            public void onSlideClick(int position) {
                Slider slider = sliders.get(position);
                FunctionHelper.gotoLink(parent.getContext(),
                        slider.getLinkType(),slider.getLink(),slider.getName());
            }
        });

        if (sliders.size() > 1) {
            slider.setLoopSlides(true);
        } else if (sliders.size() == 1) {
            slider.setLoopSlides(false);
        }
    }
}
