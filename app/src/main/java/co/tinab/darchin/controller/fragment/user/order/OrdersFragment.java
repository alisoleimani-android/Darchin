package co.tinab.darchin.controller.fragment.user.order;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.adapter.OrderListAdapter;
import co.tinab.darchin.controller.fragment.store.StoreFragment;
import co.tinab.darchin.controller.fragment.user.ProfileFragment;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.controller.tools.NestedScrollListener;
import co.tinab.darchin.model.User;
import co.tinab.darchin.model.address.City;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.OrderRequestHelper;
import co.tinab.darchin.model.network.request_helpers.StoreRequestHelper;
import co.tinab.darchin.model.network.resources.BasicResource;
import co.tinab.darchin.model.network.resources.OrderCollectionResource;
import co.tinab.darchin.model.network.resources.StoreResource;
import co.tinab.darchin.model.order.Order;
import co.tinab.darchin.view.component.EmptyView;
import co.tinab.darchin.view.component.HorizontalLoadingView;
import co.tinab.darchin.view.component.LoadingView;
import co.tinab.darchin.view.component.SectionView;
import co.tinab.darchin.view.dialog.QuestionDialog;
import co.tinab.darchin.view.dialog.WaitingDialog;
import co.tinab.darchin.view.toolbox.MyRecyclerView;
import co.tinab.darchin.view.toolbox.MySnackbar;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment implements OrderListAdapter.Listener, SwipeRefreshLayout.OnRefreshListener, SectionView.RequestCompleteListener {
    private List<Order> orderList = new ArrayList<>();
    private OrderListAdapter adapter;
    private EmptyView emptyView;
    private LoadingView loadingView;
    private WaitingDialog waitingDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isFragmentLoaded = false,isFragmentLeaved = false;
    private SectionView sectionView;
    private int totalItemsCount = 10;
    private HorizontalLoadingView horizontalLoadingView;

    public static OrdersFragment newInstance(){
        return new OrdersFragment();
    }

    public OrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);

        MyRecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new OrderListAdapter(orderList);
        adapter.setOrderListAdapterListener(this);
        recyclerView.setAdapter(adapter);

        NestedScrollView nestedScrollView = view.findViewById(R.id.nested_scroll);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollListener(recyclerView) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                horizontalLoadingView.show();
                OrdersFragment.this.totalItemsCount += totalItemsCount;
                getOrders(OrdersFragment.this.totalItemsCount);
            }
        });

        sectionView = view.findViewById(R.id.container_section);
        sectionView.setOnRequestCompleteListener(this);

        emptyView = view.findViewById(R.id.empty_view);
        loadingView = view.findViewById(R.id.loading_view);
        horizontalLoadingView = view.findViewById(R.id.horizontal_loading);
        if (getContext() != null) waitingDialog = new WaitingDialog(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFragmentLeaved) {
            isFragmentLeaved = false;

            // get orders
            loadingView.show();
            getOrders(totalItemsCount);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isFragmentLoaded) {
            isFragmentLoaded = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // get orders
                    loadingView.show();
                    getOrders(totalItemsCount);

                    // get sections
                    City city = User.getInstance(getContext()).getCity(getContext());
                    if (city != null) {
                        if (!city.getName().isEmpty()) {
                            if (FunctionHelper.isConnected(getContext())) {
                                sectionView.requestData("orders","top");
                            }else {
                                sectionView.bind(User.getInstance(getContext()).getSections(getContext(),"orders","top"));
                            }
                        }
                    }

                }
            },50);
        }
    }

    private void getOrders(int take){
        String token = User.getToken(getContext());

        OrderRequestHelper requestHelper = OrderRequestHelper.getInstance();
        requestHelper.getOrders(token,take).enqueue(new MyCallback<OrderCollectionResource>() {
            @Override
            public void onRequestSuccess(Response<OrderCollectionResource> response) {
                if (isAdded()) {
                    swipeRefreshLayout.setRefreshing(false);
                    loadingView.hide();
                    horizontalLoadingView.hide();

                    final OrderCollectionResource resource = response.body();
                    if (resource != null && FunctionHelper.isSuccess(getView(), resource) && resource.getOrders() != null) {
                        bind(resource.getOrders());

                    }else {
                        MySnackbar.make(getView(),MySnackbar.Failure,R.string.request_failed).show();
                    }
                }
            }

            @Override
            public void onRequestFailed(@Nullable List<String> messages) {
                if (isAdded()) {
                    swipeRefreshLayout.setRefreshing(false);
                    loadingView.hide();
                    horizontalLoadingView.hide();
                    FunctionHelper.showMessages(getView(),messages);
                }
            }

            @Override
            public void unAuthorizedDetected() {
                if (isAdded()) {
                    swipeRefreshLayout.setRefreshing(false);
                    loadingView.hide();
                    horizontalLoadingView.hide();
                }
                if (getActivity() != null) ((MainActivity)getActivity()).logout();
            }
        });
    }

    private void bind(List<Order> orderList){
        this.orderList.clear();
        this.orderList.addAll(orderList);
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount() > 0) {
            if (isAdded()) emptyView.hide();
        }else {
            if (isAdded()) emptyView.show();
        }
    }

    @Override
    public void onBtnCommentClicked(Order order) {
        if (order.hasComment()) {
            ViewCommentDialog dialog = ViewCommentDialog.newInstance(order.getVote().getComment());
            dialog.show(getChildFragmentManager(),"ViewCommentDialog");

        }else {
            isFragmentLeaved = true;
            if (getActivity() != null) ((MainActivity)getActivity())
                    .pushFragment(PostCommentFragment.newInstance(order),
                            OrdersFragment.class.getName(),0);
        }
    }

    @Override
    public void onBtnStatusClicked(final Order order) {
        switch (order.getStatus()){
            case "wait":
                break;

            case "cashier":
                String desc = getString(R.string.are_you_sure_to_cancel_the_order);
                String posName = getString(R.string.yes);
                String negativeName = getString(R.string.no);

                QuestionDialog dialog = QuestionDialog.newInstance(posName,negativeName,desc);
                dialog.setListener(new QuestionDialog.ClickListener() {

                    @Override
                    public void onPositiveBtnClicked(DialogFragment dialogFragment) {
                        dialogFragment.dismiss();

                        // cancel the order
                        cancelOrder(order.getOrderId());
                    }

                    @Override
                    public void onNegativeBtnClicked(DialogFragment dialogFragment) {
                        dialogFragment.dismiss();
                    }
                });
                dialog.show(getChildFragmentManager(),"QuestionDialog");
                break;
        }
    }

    @Override
    public void onGoingToStore(Order order) {
        if (order.getStore() != null) openStore(order.getStore().getStoreId());
    }

    @Override
    public void onRefresh() {
        getOrders(totalItemsCount);
    }

    // open store:
    private void openStore(int storeId){
        if (waitingDialog != null) waitingDialog.show();

        StoreRequestHelper requestHelper = StoreRequestHelper.getInstance();
        requestHelper.getStore(storeId).enqueue(new MyCallback<StoreResource>() {
            @Override
            public void onRequestSuccess(Response<StoreResource> response) {
                if (waitingDialog != null) waitingDialog.dismiss();

                StoreResource storeResource = response.body();
                if (FunctionHelper.isSuccess(getView(), storeResource) && storeResource.getStore() != null) {

                    isFragmentLeaved = true;
                    if (getActivity() != null) ((MainActivity)getActivity())
                            .pushFragment(StoreFragment.newInstance(storeResource.getStore()));

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

    private void cancelOrder(int orderId){
        if (waitingDialog != null) waitingDialog.show();
        String token = User.getToken(getContext());

        OrderRequestHelper requestHelper = OrderRequestHelper.getInstance();
        requestHelper.cancelOrder(token,orderId).enqueue(new MyCallback<BasicResource>() {
            @Override
            public void onRequestSuccess(Response<BasicResource> response) {
                if (waitingDialog != null) waitingDialog.hide();

                if (FunctionHelper.isSuccess(getView(), response.body())) {
                    getOrders(totalItemsCount); // update orders
                    if (getParentFragment() instanceof ProfileFragment) {
                        ((ProfileFragment)getParentFragment()).getAccountInfo();
                    }

                }else {
                    MySnackbar.make(getView(),MySnackbar.Failure,R.string.request_failed).show();
                }
            }

            @Override
            public void onRequestFailed(@Nullable List<String> messages) {
                if (waitingDialog != null) waitingDialog.hide();
                FunctionHelper.showMessages(getView(),messages);
            }

            @Override
            public void unAuthorizedDetected() {
                if (waitingDialog != null) waitingDialog.hide();
                if (getContext() instanceof MainActivity) ((MainActivity)getContext()).logout();
            }
        });
    }

    @Override
    public void onRequestCompleted() {

    }

    @Override
    public void onRequestFailed() {

    }
}
