package co.tinab.darchin.controller.fragment.order;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.fragment.MainFragment;
import co.tinab.darchin.controller.fragment.authentication.LoginOrRegisterFragment;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.User;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.OrderRequestHelper;
import co.tinab.darchin.model.network.resources.OrderResource;
import co.tinab.darchin.model.store.Cart;
import co.tinab.darchin.view.dialog.CartInfoModal;
import co.tinab.darchin.view.dialog.QuestionDialog;
import co.tinab.darchin.view.dialog.WaitingDialog;
import co.tinab.darchin.view.toolbox.ButtonNormal;
import co.tinab.darchin.view.toolbox.EditTextNormal;
import co.tinab.darchin.view.toolbox.MoneyTextView;
import co.tinab.darchin.view.toolbox.MySnackbar;
import co.tinab.darchin.view.toolbox.TextViewLight;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryTypeFragment extends Fragment implements View.OnClickListener {
    private Typeface fontLight;
    private int selectedID = -1;
    private Cart cart;
    private RadioButton btnIsDelivery,btnIsInside;
    private ButtonNormal btnTime;
    private MoneyTextView txtSumTotal;
    private EditTextNormal txtInputCoupon;

    public static DeliveryTypeFragment newInstance(Cart cart){
        DeliveryTypeFragment fragment = new DeliveryTypeFragment();
        Bundle args = new Bundle();
        args.putParcelable("cart",cart);
        fragment.setArguments(args);
        return fragment;
    }

    public DeliveryTypeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContext() != null)
            fontLight = Typeface.createFromAsset(getContext().getAssets(),"fonts/Light.ttf");

        if (getArguments() != null) {
            cart = getArguments().getParcelable("cart");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delivery_type, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextViewLight txtTitle = view.findViewById(R.id.txt_title);
        txtTitle.setText(getString(R.string.how_to_take_order));

        txtSumTotal = view.findViewById(R.id.txt_sum_total);
        txtInputCoupon = view.findViewById(R.id.txt_input_coupon_code);

        view.findViewById(R.id.btn_back).setOnClickListener(this);
        view.findViewById(R.id.container_info).setOnClickListener(this);

        final ButtonNormal btnPayment = view.findViewById(R.id.btn_action);
        btnPayment.setOnClickListener(this);

        btnTime = view.findViewById(R.id.btn_time);
        btnTime.setOnClickListener(this);

        btnIsDelivery = view.findViewById(R.id.btn_radio_delivery);
        btnIsDelivery.setTypeface(fontLight);

        btnIsInside = view.findViewById(R.id.btn_radio_serve_inside);
        btnIsInside.setTypeface(fontLight);

        RadioGroup radioGroup = view.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedID = group.getCheckedRadioButtonId();
                if (selectedID == R.id.btn_radio_delivery) {
                    btnPayment.setText(getString(R.string.choose_address));
                }

                if (selectedID == R.id.btn_radio_serve_inside) {
                    btnPayment.setText(getString(R.string.online_payment));
                }

                btnTime.setText(R.string.choose_time);
                cart.setSelectedPeriod(null);
            }
        });
        selectedID = radioGroup.getCheckedRadioButtonId();
    }

    @Override
    public void onResume() {
        super.onResume();
        txtSumTotal.setText(String.valueOf(cart.getSumTotal()));
    }

    @Override
    public void onDestroyView() {
        cart.setSelectedPeriod(null);
        cart.setDeliverType(null);
        cart.setCouponCode(null);
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;

            case R.id.btn_action:
                btnActionClicked();
                break;

            case R.id.container_info:
                CartInfoModal.newInstance(cart).show(getChildFragmentManager(),CartInfoModal.class.getName());
                break;

            case R.id.btn_time:
                boolean isDelivery = btnIsDelivery.isChecked();
                boolean isInside = btnIsInside.isChecked();

                if (getActivity() != null) ((MainActivity)getActivity())
                        .pushFragment(TimeSelectionFragment.newInstance(cart,isDelivery,isInside),
                                DeliveryTypeFragment.class.getName(),0);
                break;
        }
    }

    private void btnActionClicked(){
        if (getContext() != null) {
            if (User.getInstance(getContext()).hasLoggedIn(getContext())) {
                if (cart.getSelectedPeriod() != null) {
                    // check coupon code
                    if (!txtInputCoupon.getText().toString().isEmpty()) {
                        cart.setCouponCode(txtInputCoupon.getText().toString());
                    }else {
                        cart.setCouponCode(null);
                    }

                    if (selectedID == R.id.btn_radio_delivery) {
                        // set deliver type to cart
                        cart.setDeliverType("deliver");

                        if (getActivity() != null) ((MainActivity)getActivity())
                                .pushFragment(AddressSelectionFragment.newInstance(cart));
                    }

                    if (selectedID == R.id.btn_radio_serve_inside) {
                        // set deliver type to cart
                        cart.setDeliverType("inside");

                        // goto create payment:
                        createPayment(getContext());
                    }

                }else {
                    MySnackbar.make(getView(),MySnackbar.Alert,R.string.please_choose_your_order_time).show();
                }
            }else {
                String tag = DeliveryTypeFragment.class.getName();
                if (getActivity() != null) ((MainActivity)getActivity())
                        .pushFragment(LoginOrRegisterFragment.newInstance(tag));
            }
        }
    }

    private void createPayment(@NonNull final Context context){
        final WaitingDialog waitingDialog = new WaitingDialog(context);
        waitingDialog.show();

        OrderRequestHelper requestHelper = OrderRequestHelper.getInstance();
        requestHelper.createOrder(User.getToken(getContext()),cart).enqueue(new MyCallback<OrderResource>() {
            @Override
            public void onRequestSuccess(Response<OrderResource> response) {
                waitingDialog.dismiss();

                final OrderResource orderResource = response.body();
                if (FunctionHelper.isSuccess(getView(), orderResource) && orderResource.getOrder() != null) {

                    User user = User.getInstance(getContext());
                    int totalPrice = Integer.parseInt(orderResource.getOrder().getTotalPrice());
                    int credit = Integer.parseInt(user.getCredit());
                    int difference = totalPrice-credit;

                    String payment;
                    String cancel;
                    String description;
                    if (difference > 0) {
                        payment = getString(R.string.payment);
                        cancel = getString(R.string.cancel);

                        String name = user.getName();
                        String creditStr = user.getCredit();
                        String differenceStr = String.valueOf(totalPrice-credit);
                        String couponPriceStr = orderResource.getOrder().getCouponPrice();

                        description = getString(R.string.payment_info,name,creditStr,couponPriceStr,differenceStr);
                    }else {
                        payment = getString(R.string.confirm);
                        cancel = null;
                        description = getString(R.string.you_do_not_need_to_pay);
                    }

                    QuestionDialog dialog = QuestionDialog.newInstance(payment,cancel,description);
                    dialog.show(getChildFragmentManager(),"QuestionDialog");

                    dialog.setListener(new QuestionDialog.ClickListener() {
                        @Override
                        public void onPositiveBtnClicked(DialogFragment dialogFragment) {
                            dialogFragment.dismiss();

                            // goto payment gate
                            String url = Constant.BASE_URL.concat("payment/")
                                    .concat(String.valueOf(orderResource.getOrder().getOrderId()));
                            gotoPayment(url);

                            // pop to main
                            if (getActivity() != null) ((MainActivity)getActivity())
                                    .popFragment(MainFragment.class.getName());
                        }

                        @Override
                        public void onNegativeBtnClicked(DialogFragment dialogFragment) {
                            dialogFragment.dismiss();
                        }
                    });

                }else {
                    MySnackbar.make(getView(),MySnackbar.Failure,R.string.request_failed).show();
                }

            }

            @Override
            public void onRequestFailed(@Nullable List<String> messages) {
                waitingDialog.dismiss();
                FunctionHelper.showMessages(getView(),messages);
            }

            @Override
            public void unAuthorizedDetected() {
                waitingDialog.dismiss();
                if (context instanceof MainActivity) ((MainActivity)context).logout();
            }
        });
    }

    private void gotoPayment(String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (cart.getSelectedPeriod() != null) {
                btnTime.setText(cart.getSelectedPeriod().getOrderTimeText(getContext()));
            }else {
                btnTime.setText(R.string.choose_time);
            }
        }
    }
}
