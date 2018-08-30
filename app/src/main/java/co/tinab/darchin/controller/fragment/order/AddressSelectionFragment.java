package co.tinab.darchin.controller.fragment.order;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.adapter.AddressSelectionListAdapter;
import co.tinab.darchin.controller.fragment.BaseFragment;
import co.tinab.darchin.controller.fragment.MainFragment;
import co.tinab.darchin.controller.fragment.user.address.AreaSelectionFragment;
import co.tinab.darchin.controller.fragment.user.address.EditAddressFragment;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.User;
import co.tinab.darchin.model.address.Address;
import co.tinab.darchin.model.address.Area;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.LocationRequestHelper;
import co.tinab.darchin.model.network.request_helpers.OrderRequestHelper;
import co.tinab.darchin.model.network.resources.AddressResource;
import co.tinab.darchin.model.network.resources.BasicResource;
import co.tinab.darchin.model.network.resources.OrderResource;
import co.tinab.darchin.model.store.Cart;
import co.tinab.darchin.model.store.StoreArea;
import co.tinab.darchin.view.component.EmptyView;
import co.tinab.darchin.view.component.LoadingView;
import co.tinab.darchin.view.dialog.CartInfoModal;
import co.tinab.darchin.view.dialog.QuestionDialog;
import co.tinab.darchin.view.dialog.WaitingDialog;
import co.tinab.darchin.view.toolbox.MoneyTextView;
import co.tinab.darchin.view.toolbox.MyRecyclerView;
import co.tinab.darchin.view.toolbox.MySnackbar;
import co.tinab.darchin.view.toolbox.TextViewLight;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddressSelectionFragment extends BaseFragment
        implements View.OnClickListener, AddressSelectionListAdapter.AddressListListener{
    private List<Address> addressList = new ArrayList<>();
    private AddressSelectionListAdapter adapter;
    private EmptyView emptyView;
    private LoadingView loadingView;
    private WaitingDialog waitingDialog;
    private Cart cart;
    private MoneyTextView txtSumTotal;
    private MoneyTextView txtDeliveryCost;

    public static AddressSelectionFragment newInstance(Cart cart){
        AddressSelectionFragment fragment = new AddressSelectionFragment();
        Bundle args = new Bundle();
        args.putParcelable("cart",cart);
        fragment.setArguments(args);
        return fragment;
    }

    public AddressSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cart = getArguments().getParcelable("cart");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_address_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextViewLight txtTitle = view.findViewById(R.id.txt_title);
        txtTitle.setText(getString(R.string.choose_address));

        txtSumTotal = view.findViewById(R.id.txt_sum_total);
        txtDeliveryCost = view.findViewById(R.id.txt_delivery_cost);

        MyRecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new AddressSelectionListAdapter(addressList);
        adapter.addOnAddressListListener(this);
        recyclerView.setAdapter(adapter);

        final FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0){
                    fab.hide();
                } else{
                    fab.show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        view.findViewById(R.id.btn_back).setOnClickListener(this);
        view.findViewById(R.id.btn_payment).setOnClickListener(this);
        view.findViewById(R.id.container_info).setOnClickListener(this);

        // Empty view for empty list
        emptyView = view.findViewById(R.id.empty_view);
        loadingView = view.findViewById(R.id.loading_view);
        if (getContext() != null) waitingDialog = new WaitingDialog(getContext());

        // get address list from sever
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getAddressList();
            }
        },500);
    }

    @Override
    public void onResume() {
        super.onResume();
        recalculate();
    }

    private void recalculate(){
        // recalculate sum total
        txtSumTotal.setText(String.valueOf(cart.getSumTotal()));

        // calculate delivery cost:
        txtDeliveryCost.setText(String.valueOf(cart.getShippingCost()));
    }

    private void getAddressList(){
        if (isAdded()) loadingView.show();
        LocationRequestHelper requestHandler = LocationRequestHelper.getInstance();
        requestHandler.getAddressList(User.getToken(getContext())).enqueue(new MyCallback<AddressResource>() {
            @Override
            public void onRequestSuccess(Response<AddressResource> response) {
                if (isAdded()){
                    loadingView.hide();
                    AddressResource addressResource = response.body();
                    if (FunctionHelper.isSuccess(null,addressResource) && addressResource.getAddressList() != null) {
                        bind(addressResource.getAddressList());
                    }
                }
            }

            @Override
            public void onRequestFailed(@Nullable List<String> messages) {
                if (isAdded()){
                    loadingView.hide();
                    FunctionHelper.showMessages(getView(),messages);
                }
            }

            @Override
            public void unAuthorizedDetected() {
                if (isAdded()) {
                    loadingView.hide();
                }
                if (getActivity() != null) ((MainActivity)getActivity()).logout();
            }
        });
    }

    private void bind(List<Address> addressList){
        this.addressList.clear();
        if (!addressList.isEmpty()) {
            emptyView.hide();
            this.addressList.addAll(addressList);
        }else {
            emptyView.show();
        }
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                if (getActivity() != null) getActivity().onBackPressed();
                break;

            case R.id.btn_payment:
                if (cart.getSelectedAddress() != null) {
                    if (getContext() != null) createPayment(getContext());
                }else {
                    MySnackbar.make(getView(),MySnackbar.Alert,R.string.please_choose_your_address).show();
                }
                break;

            case R.id.container_info:
                // open cart info dialog
                CartInfoModal.newInstance(cart).show(getChildFragmentManager(),CartInfoModal.class.getName());
                break;

            case R.id.fab:
                // add a new address:
                if (getActivity() != null)
                    ((MainActivity)getActivity()).pushFragment(AreaSelectionFragment
                            .newInstance(AreaSelectionFragment.CREATE_FROM_ORDERS,new Address()));
                break;
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
    public void onDestroyView() {
        cart.setSelectedAddress(null);
        super.onDestroyView();
    }

    @Override
    public void onItemSelected(Address address) {
        // check this address in store service areas:
        if (isAddressValid(address)) {
            // add this address to selected address in store
            cart.setSelectedAddress(address);

            // recalculate
            recalculate();

        }else {
            clearSelectedAddress();
            MySnackbar.make(getView(),MySnackbar.Alert,
                    R.string.unfortunately_is_not_within_the_scope_of_service).show();
        }
    }

    public void clearSelectedAddress(){
        cart.setSelectedAddress(null);
        adapter.setSelectedItem(-1);
        adapter.notifyItemRangeChanged(0,adapter.getItemCount());
    }

    private boolean isAddressValid(Address address){
        Area selectedArea = address.getLocation().getArea();
        for (StoreArea storeArea : cart.getStore().getAreas()){
            if (storeArea.getArea().getId() == selectedArea.getId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBtnEditClicked(Address address) {
        if (getActivity() != null) ((MainActivity)getActivity()).pushFragment(
                EditAddressFragment.newInstance(EditAddressFragment.EDIT_FROM_ORDERS,address),
                AddressSelectionFragment.class.getName(), 0);
    }

    @Override
    public void onBtnDeleteClicked(Address address) {
        deleteAddress(address);
    }

    private void deleteAddress(Address address){
        if (waitingDialog != null) waitingDialog.show();

        LocationRequestHelper requestHandler = LocationRequestHelper.getInstance();
        requestHandler.deleteAddress(User.getToken(getContext()),address.getId()).enqueue(new MyCallback<BasicResource>() {
            @Override
            public void onRequestSuccess(Response<BasicResource> response) {
                if (waitingDialog != null) waitingDialog.dismiss();

                BasicResource basicResource = response.body();
                if (FunctionHelper.isSuccess(getView(),basicResource)) {
                    clearSelectedAddress();
                    recalculate();
                    getAddressList();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            clearSelectedAddress();
            recalculate();
            getAddressList();
        }
    }
}
