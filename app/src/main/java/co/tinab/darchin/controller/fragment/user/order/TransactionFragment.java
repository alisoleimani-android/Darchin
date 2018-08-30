package co.tinab.darchin.controller.fragment.user.order;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.adapter.TransactionListAdapter;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.User;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.OrderRequestHelper;
import co.tinab.darchin.model.network.resources.TransactionResource;
import co.tinab.darchin.model.order.Transaction;
import co.tinab.darchin.view.component.EmptyView;
import co.tinab.darchin.view.component.LoadingView;
import co.tinab.darchin.view.toolbox.MyRecyclerView;
import co.tinab.darchin.view.toolbox.MySnackbar;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends Fragment {
    private boolean isFragmentLoaded = false;
    private EmptyView emptyView;
    private LoadingView loadingView;
    private List<Transaction> transactionList = new ArrayList<>();
    private TransactionListAdapter adapter;

    public static TransactionFragment newInstance(){
        return new TransactionFragment();
    }

    public TransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction, container, false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isFragmentLoaded) {
            isFragmentLoaded = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isAdded()) getTransactions();
                }
            },50);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MyRecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new TransactionListAdapter(transactionList);
        recyclerView.setAdapter(adapter);

        emptyView = view.findViewById(R.id.empty_view);
        loadingView = view.findViewById(R.id.loading_view);
    }

    private void getTransactions(){
        if (isAdded()) loadingView.show();

        OrderRequestHelper requestHelper = OrderRequestHelper.getInstance();
        requestHelper.getTransactions(User.getToken(getContext())).enqueue(new MyCallback<TransactionResource>() {
            @Override
            public void onRequestSuccess(Response<TransactionResource> response) {
                if (isAdded()) {
                    loadingView.hide();

                    TransactionResource transactionResource = response.body();
                    if (FunctionHelper.isSuccess(getView(), transactionResource) && transactionResource.getTransactions() != null) {
                        bind(transactionResource.getTransactions());

                    }else {
                        MySnackbar.make(getView(),MySnackbar.Failure,R.string.request_failed).show();
                    }
                }
            }

            @Override
            public void onRequestFailed(@Nullable List<String> messages) {
                if (isAdded()) loadingView.hide();
                FunctionHelper.showMessages(getView(),messages);
            }

            @Override
            public void unAuthorizedDetected() {
                if (isAdded()) loadingView.hide();
                if (getActivity() != null) ((MainActivity)getActivity()).logout();
            }
        });
    }

    private void bind(List<Transaction> transactionList){
        if (isAdded()) {
            this.transactionList.clear();
            this.transactionList.addAll(transactionList);
            adapter.notifyDataSetChanged();
            if (adapter.getItemCount() > 0) {
                emptyView.hide();
            }else {
                emptyView.show();
            }
        }
    }

}
