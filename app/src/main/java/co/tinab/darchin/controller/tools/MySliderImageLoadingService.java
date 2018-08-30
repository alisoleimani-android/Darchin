package co.tinab.darchin.controller.tools;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import ss.com.bannerslider.ImageLoadingService;

/**
 * Created by A.S.R on 4/14/2018.
 */

public class MySliderImageLoadingService implements ImageLoadingService {
    public Context context;

    public MySliderImageLoadingService(Context context){
        this.context = context;
    }

    @Override
    public void loadImage(String url, ImageView imageView) {
        Picasso.with(context)
                .load(url)
                .resize(Display.getWidthOfDevice(context),0)
                .onlyScaleDown()
                .into(imageView);
    }

    @Override
    public void loadImage(int resource, ImageView imageView) {
        Picasso.with(context)
                .load(resource)
                .resize(Display.getWidthOfDevice(context),0)
                .onlyScaleDown()
                .into(imageView);
    }

    @Override
    public void loadImage(String url, int placeHolder, int errorDrawable, ImageView imageView) {
        Picasso.with(context)
                .load(url)
                .resize(Display.getWidthOfDevice(context),0)
                .onlyScaleDown()
                .placeholder(placeHolder)
                .error(errorDrawable)
                .into(imageView);
    }
}
