package co.tinab.darchin.controller.fragment.user.order;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import java.util.List;

import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.fragment.BaseFragment;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.OrderRequestHelper;
import co.tinab.darchin.model.network.resources.BasicResource;
import co.tinab.darchin.model.order.Order;
import co.tinab.darchin.model.User;
import co.tinab.darchin.R;
import co.tinab.darchin.view.dialog.WaitingDialog;
import co.tinab.darchin.view.toolbox.EditTextLight;
import co.tinab.darchin.view.toolbox.MySnackbar;
import co.tinab.darchin.view.toolbox.TextViewLight;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostCommentFragment extends BaseFragment implements View.OnClickListener{
    private Order order;
    private RatingBar ratingBarProductQuality,ratingBarPackingQuality,
            ratingBarCourierSpeed,ratingBarCourierEthics;
    private EditTextLight txtInputComment;
    private WaitingDialog waitingDialog;

    public static PostCommentFragment newInstance(Order order){
        PostCommentFragment postCommentFragment = new PostCommentFragment();
        Bundle args = new Bundle();
        args.putParcelable("order",order);
        postCommentFragment.setArguments(args);
        return postCommentFragment;
    }

    public PostCommentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            order = getArguments().getParcelable("order");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((TextViewLight) view.findViewById(R.id.txt_title)).setText(getString(R.string.poll));
        ((TextViewLight)view.findViewById(R.id.txt_name))
                .setText(getString(R.string.your_opinion_about_this_order,order.getStore().getName()));

        ratingBarProductQuality = view.findViewById(R.id.rating_bar_product_quality);
        ratingBarPackingQuality = view.findViewById(R.id.rating_bar_packing_quality);
        ratingBarCourierSpeed = view.findViewById(R.id.rating_bar_courier_speed);
        ratingBarCourierEthics = view.findViewById(R.id.rating_bar_courier_ethics);
        txtInputComment = view.findViewById(R.id.txt_input_comment);

        if (getContext() != null) waitingDialog = new WaitingDialog(getContext());

        view.findViewById(R.id.btn_back).setOnClickListener(this);
        view.findViewById(R.id.btn_submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;

            case R.id.btn_submit:
                postComment();
                break;
        }
    }

    private void postComment(){
        if (waitingDialog != null) waitingDialog.show();

        String token = User.getToken(getContext());
        int orderId = order.getOrderId();
        int productQuality = (int) ratingBarProductQuality.getRating();
        int packingQuality = (int) ratingBarPackingQuality.getRating();
        int courierSpeed = (int) ratingBarCourierSpeed.getRating();
        int courierEthics = (int) ratingBarCourierEthics.getRating();
        String text = txtInputComment.getText().toString();

        OrderRequestHelper requestHelper = OrderRequestHelper.getInstance();
        requestHelper.comment(token,orderId,text,productQuality,packingQuality,courierSpeed,courierEthics).enqueue(new MyCallback<BasicResource>() {
            @Override
            public void onRequestSuccess(Response<BasicResource> response) {
                if (waitingDialog != null) waitingDialog.dismiss();

                BasicResource basicResource = response.body();
                if (FunctionHelper.isSuccess(getView(), basicResource)) {
                    if (getActivity() != null) ((MainActivity)getActivity())
                            .popFragment(null,OrdersFragment.class.getName());

                }else {
                    MySnackbar.make(getView(),MySnackbar.Failure,R.string.request_failed).show();
                }
            }

            @Override
            public void onRequestFailed(@Nullable List<String> messages) {
                if (waitingDialog != null) waitingDialog.dismiss();
                FunctionHelper.showMessages(getView(),messages);
            }

            @Override
            public void unAuthorizedDetected() {
                if (waitingDialog != null) waitingDialog.dismiss();
                if (getActivity() != null) ((MainActivity)getActivity()).logout();
            }
        });
    }
}
