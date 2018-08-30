package co.tinab.darchin.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.List;

import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.Filter;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.FilterRequestHelper;
import co.tinab.darchin.model.network.resources.FilterResource;
import co.tinab.darchin.R;
import co.tinab.darchin.view.component.FilterContainer;
import co.tinab.darchin.view.component.LoadingView;
import co.tinab.darchin.view.toolbox.MySnackbar;
import co.tinab.darchin.view.toolbox.TextViewLight;
import retrofit2.Response;

/**
 * Created by A.S.R on 1/14/2018.
 */

public class FilterDialog extends Dialog {
    private LinearLayout containerFilters;
    private LoadingView loadingView;

    public FilterDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        buildView();
    }

    private void buildView(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_filter);

        TextViewLight txtTitle = findViewById(R.id.txt_title);
        txtTitle.setText(getContext().getString(R.string.filter_your_choice));

        ImageButton btnClose = findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterDialog.this.dismiss();
            }
        });

        containerFilters = findViewById(R.id.container_filters);

        loadingView = findViewById(R.id.loading_view);

        getFilters();
    }

    private void getFilters(){
        loadingView.show();
        FilterRequestHelper requestHelper = FilterRequestHelper.getInstance();
        requestHelper.getFilterList().enqueue(new MyCallback<FilterResource>() {
            @Override
            public void onRequestSuccess(Response<FilterResource> response) {
                loadingView.hide();

                FilterResource filterResponse = response.body();
                if (FunctionHelper.isSuccess(null,filterResponse) && filterResponse.getFilterList() != null) {
                    bind(filterResponse.getFilterList());

                }else {
                    dismiss();
                    MySnackbar.make(getCurrentFocus(),MySnackbar.Failure,R.string.request_failed).show();
                }
            }

            @Override
            public void onRequestFailed(@Nullable List<String> messages) {
                loadingView.hide();
                dismiss();
                FunctionHelper.showMessages(getCurrentFocus(),messages);
            }

            @Override
            public void unAuthorizedDetected() {
                loadingView.hide();
                dismiss();
                if (getContext() instanceof MainActivity) {
                    ((MainActivity)getContext()).logout();
                }
            }
        });
    }

    private void bind(List<Filter> filterList){
        for (int i = 0 ; i < filterList.size() ; i++){
            Filter filter = filterList.get(i);
            FilterContainer filterContainer = new FilterContainer(containerFilters);
            containerFilters.addView(filterContainer.getView(), i, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            filterContainer.bind(filter);
        }
    }
}
