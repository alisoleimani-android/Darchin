package co.tinab.darchin.view.component;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;

import java.util.List;

import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.address.City;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.SectionRequestHelper;
import co.tinab.darchin.model.network.resources.SectionResource;
import co.tinab.darchin.model.section.Section;
import co.tinab.darchin.model.User;
import retrofit2.Response;

/**
 * Created by A.S.R on 1/1/2018.
 */

public class SectionView extends LinearLayoutCompat {

    public SectionView(Context context) {
        this(context, null, 0);
    }

    public SectionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SectionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // requesting data listener
    public interface RequestCompleteListener{
        void onRequestCompleted();
        void onRequestFailed();
    }
    private RequestCompleteListener listener;
    public void setOnRequestCompleteListener(RequestCompleteListener listener){
        this.listener = listener;
    }

    // request section list
    public void requestData(final String page, final String position){
        City city = User.getInstance(getContext()).getCity(getContext());
        if (city != null) {
            String cityId = String.valueOf(city.getCityId());

            SectionRequestHelper requestHelper = SectionRequestHelper.getInstance();
            requestHelper.getSections(page, position, cityId).enqueue(new MyCallback<SectionResource>() {
                @Override
                public void onRequestSuccess(Response<SectionResource> response) {
                    SectionResource resource = response.body();
                    if (FunctionHelper.isSuccess(getRootView(), resource) && resource.getSectionList() != null) {
                        // save home sections to pref:
                        User.getInstance(getContext()).setSections(getContext(),resource.getSectionList(),page,position);

                        // binding sections
                        bind(resource.getSectionList());

                    }else {
                        if (listener != null) listener.onRequestFailed();
                    }
                }

                @Override
                public void onRequestFailed(@Nullable List<String> messages) {
                    if (listener != null) listener.onRequestFailed();
                    FunctionHelper.showMessages(getRootView(),messages);
                }

                @Override
                public void unAuthorizedDetected() {
                    if (listener != null) listener.onRequestFailed();
                    if (getContext() != null && getContext() instanceof MainActivity) {
                        ((MainActivity)getContext()).logout();
                    }
                }
            });
        }
    }

    // building view after getting section list
    public void bind(final List<Section> sectionList){
        // clear container if messed up before
        if (getChildCount() > 0) removeAllViews();

        // bind sections
        if (!sectionList.isEmpty()) {
            if (listener != null) listener.onRequestCompleted();
            setVisibility(VISIBLE);

            // iterate sections
            for (int i = sectionList.size() - 1 ; i >= 0 ; i--){

                final int finalI = i;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Section section = sectionList.get(finalI);
                        addSection(section);
                    }
                },300 * i);
            }
        }else {
            if (listener != null) listener.onRequestFailed();
            setVisibility(INVISIBLE);
        }
    }

    // method for adding section to parent container:
    private void addSection(Section section){
        if (section.getType() != null && !section.getContents().isJsonNull() && section.getContents().size() > 0) {
            switch (section.getType()){
                case "slider":
                    SliderSectionView sliderSectionView = new SliderSectionView(SectionView.this);
                    addView(sliderSectionView.getView());
                    sliderSectionView.requestData(section);
                    break;

                case "product":
                    HorizontalListSectionView listSectionView = new HorizontalListSectionView(SectionView.this);
                    addView(listSectionView.getView());
                    listSectionView.requestData(section);
                    break;

                case "store":
                    listSectionView = new HorizontalListSectionView(SectionView.this);
                    addView(listSectionView.getView());
                    listSectionView.requestData(section);
                    break;

                case "banner":
                    BannerSectionView bannerSectionView = new BannerSectionView(SectionView.this);
                    addView(bannerSectionView.getView());
                    bannerSectionView.requestData(section);
                    break;

                case "notification":
                    NotificationSectionView notificationSectionView = new NotificationSectionView(SectionView.this);
                    addView(notificationSectionView.getView());
                    notificationSectionView.requestData(section);
                    break;

                case "call_to_action":
                    CallToActionSectionView callToActionSectionView = new CallToActionSectionView(SectionView.this);
                    addView(callToActionSectionView.getView());
                    callToActionSectionView.requestData(section);
                    break;
            }
        }
    }
}
