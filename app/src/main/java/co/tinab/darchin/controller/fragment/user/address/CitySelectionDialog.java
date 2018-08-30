package co.tinab.darchin.controller.fragment.user.address;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.adapter.CityListAdapter;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.controller.tools.SimpleDividerItemDecoration;
import co.tinab.darchin.model.address.City;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.LocationRequestHelper;
import co.tinab.darchin.model.network.resources.BasicResource;
import co.tinab.darchin.model.network.resources.CityResource;
import co.tinab.darchin.view.component.LoadingView;
import co.tinab.darchin.view.toolbox.MyRecyclerView;
import co.tinab.darchin.view.toolbox.MySnackbar;
import co.tinab.darchin.view.toolbox.TextViewLight;
import retrofit2.Response;

/**
 * Created by A.S.R on 3/5/2018.
 */

public class CitySelectionDialog extends Dialog
        implements View.OnClickListener, CityListAdapter.ItemSelectedListener, MyRecyclerView.EndReachedListener {
    private List<City> cityList = new ArrayList<>();
    private CityListAdapter adapter;
    private LoadingView loadingView;
    private City selectedCity = null;
    private BasicResource.Link link;

    @Override
    public void onEndReached() {
        if (link != null && link.getNext() != null) {
            getCities(link.getNext());
        }
    }

    public interface CitySelectionListener{
        void onCitySelected(City city);
    }
    private CitySelectionListener listener;
    public void setListener(CitySelectionListener listener){
        this.listener = listener;
    }

    public CitySelectionDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        buildView();
    }

    private void buildView(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_city_selection);

        TextViewLight txtTitle = findViewById(R.id.txt_title);
        txtTitle.setText(getContext().getString(R.string.city_selection));

        findViewById(R.id.btn_choose).setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);
        loadingView = findViewById(R.id.loading_view);

        MyRecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new CityListAdapter(cityList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        adapter.setOnItemSelectedListener(this);
        recyclerView.setOnEndReachedListener(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingView.show();
                getCities("city");
            }
        },500);
    }

    private void getCities(String url){
        LocationRequestHelper requestHandler = LocationRequestHelper.getInstance();
        requestHandler.getCityList(url).enqueue(new MyCallback<CityResource>() {
            @Override
            public void onRequestSuccess(Response<CityResource> response) {
                loadingView.hide();
                CityResource cityResponse = response.body();
                if (FunctionHelper.isSuccess(null,cityResponse) && cityResponse.getCityList() != null) {
                    link = cityResponse.getLink();
                    bind(cityResponse.getCityList());
                }
            }

            @Override
            public void onRequestFailed(@Nullable List<String> messages) {
                loadingView.hide();
                FunctionHelper.showMessages(getCurrentFocus(),messages);
            }

            @Override
            public void unAuthorizedDetected() {
                loadingView.hide();
                if (getContext() instanceof Activity) {
                    ((MainActivity)getContext()).logout();
                }
            }
        });
    }

    private void bind(List<City> cityList){
        int positionStart = adapter.getItemCount();
        this.cityList.addAll(cityList);
        adapter.notifyItemRangeInserted(positionStart,cityList.size());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_close:
                dismiss();
                break;

            case R.id.btn_choose:
                if (selectedCity != null) {
                    listener.onCitySelected(selectedCity);
                    dismiss();
                }else {
                    MySnackbar.make(getCurrentFocus(),MySnackbar.Alert,R.string.please_select_a_city).show();
                }
                break;
        }
    }

    @Override
    public void onItemSelected(City city) {
        selectedCity = city;
    }
}
