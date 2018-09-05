package co.tinab.darchin.controller.fragment.store;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;

import java.util.ArrayList;
import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.adapter.CommentListAdapter;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.controller.tools.NestedScrollListener;
import co.tinab.darchin.model.network.MyCallback;
import co.tinab.darchin.model.network.request_helpers.StoreRequestHelper;
import co.tinab.darchin.model.network.resources.BasicResource;
import co.tinab.darchin.model.network.resources.CommentCollectionResource;
import co.tinab.darchin.model.order.Comment;
import co.tinab.darchin.model.store.Store;
import co.tinab.darchin.model.store.Vote;
import co.tinab.darchin.view.component.LoadingView;
import co.tinab.darchin.view.toolbox.MyRecyclerView;
import co.tinab.darchin.view.toolbox.TextViewLight;
import co.tinab.darchin.view.toolbox.TextViewNormal;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends Fragment {
    private Store store;
    private boolean isFragmentLoaded = false;
    private List<Comment> commentList = new ArrayList<>();
    private CommentListAdapter commentListAdapter = new CommentListAdapter();
    private LoadingView loadingView;
    private BasicResource.Link link;

    public static CommentFragment newInstance(Store store){
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putParcelable("store",store);
        fragment.setArguments(args);
        return fragment;
    }

    public CommentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            store = getArguments().getParcelable("store");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        loadingView = view.findViewById(R.id.loading_view);

        Vote vote = store.getVote();

        TextViewNormal txtScore = view.findViewById(R.id.txt_score);
        txtScore.setBackground(vote.getScoreBackground(getContext()));
        txtScore.setText(vote.getAverageString());

        RatingBar ratingBar = view.findViewById(R.id.rating_bar);
        ratingBar.setRating(vote.getAverage());

        TextViewLight txtCount = view.findViewById(R.id.txt_count);
        txtCount.setText(getString(R.string.person_count,String.valueOf(vote.getCount())));

        ProgressBar progressBarProductQuality = view.findViewById(R.id.progress_product_quality);
        progressBarProductQuality.setProgressDrawable(vote.getProgressBackground(vote.getProductQuality(),getContext()));
        progressBarProductQuality.setProgress((int) vote.getProductQuality());

        TextViewLight txtProductQuality = view.findViewById(R.id.txt_product_quality);
        txtProductQuality.setText(String.valueOf(vote.getProductQualityStr()));

        ProgressBar progressBarCourierSpeed = view.findViewById(R.id.progress_courier_speed);
        progressBarCourierSpeed.setProgressDrawable(vote.getProgressBackground(vote.getCourierSpeed(),getContext()));
        progressBarCourierSpeed.setProgress((int) vote.getCourierSpeed());

        TextViewLight txtCourierSpeed = view.findViewById(R.id.txt_courier_speed);
        txtCourierSpeed.setText(String.valueOf(vote.getCourierSpeedStr()));

        ProgressBar progressBarPackingQuality = view.findViewById(R.id.progress_packing_quality);
        progressBarPackingQuality.setProgressDrawable(vote.getProgressBackground(vote.getPackingQuality(),getContext()));
        progressBarPackingQuality.setProgress((int) vote.getPackingQuality());

        TextViewLight txtPackingQuality = view.findViewById(R.id.txt_packing_quality);
        txtPackingQuality.setText(String.valueOf(vote.getPackingQualityStr()));

        ProgressBar progressBarCourierEthics = view.findViewById(R.id.progress_courier_ethics);
        progressBarCourierEthics.setProgressDrawable(vote.getProgressBackground(vote.getCourierEthics(),getContext()));
        progressBarCourierEthics.setProgress((int) vote.getCourierEthics());

        TextViewLight txtCourierEthics = view.findViewById(R.id.txt_courier_ethics);
        txtCourierEthics.setText(String.valueOf(vote.getCourierEthicsStr()));

        // comments
        MyRecyclerView recyclerViewComment = view.findViewById(R.id.recycler_view);
        recyclerViewComment.setAdapter(commentListAdapter);

        NestedScrollView nestedScrollView = view.findViewById(R.id.nested_scroll);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollListener(recyclerViewComment) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (link != null && link.getNext() != null) {
                    getComments(link.getNext());
                }
            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isFragmentLoaded) {
            isFragmentLoaded = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (loadingView != null) loadingView.show();
                    String url = "comment/".concat(String.valueOf(store.getStoreId()));
                    getComments(url);
                }
            },50);
        }
    }
    // request to get comments
    private void getComments(String url){
        StoreRequestHelper requestHelper = StoreRequestHelper.getInstance();
        requestHelper.getComments(url).enqueue(new MyCallback<CommentCollectionResource>() {
            @Override
            public void onRequestSuccess(Response<CommentCollectionResource> response) {
                if (loadingView != null) loadingView.hide();

                CommentCollectionResource resource = response.body();
                if (FunctionHelper.isSuccess(getView(), resource) && resource.getComments() != null) {
                    link = resource.getLink();
                    bindComments(resource.getComments());
                }

            }

            @Override
            public void onRequestFailed(@Nullable List<String> messages) {
                loadingView.hide();
            }

            @Override
            public void unAuthorizedDetected() {
                if (loadingView != null) loadingView.hide();
                if (getActivity() != null) ((MainActivity)getActivity()).logout();
            }
        });
    }

    private void bindComments(List<Comment> commentList){
        if (isAdded() && this.commentList != null && commentListAdapter != null) {
            commentListAdapter.setNewData(commentList);
        }
    }
}
