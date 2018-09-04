package co.tinab.darchin.controller.tools;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import co.tinab.darchin.R;
import co.tinab.darchin.view.toolbox.MyRecyclerView;

public abstract class NestedScrollListener implements NestedScrollView.OnScrollChangeListener {

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    // The current offset index of data you have loaded
    private int currentPage = 0;
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;
    // Sets the starting page index
    private int startingPageIndex = 0;

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (v.findViewById(R.id.recycler_view) != null) {
            if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                    scrollY > oldScrollY) {
                MyRecyclerView recyclerView = v.findViewById(R.id.recycler_view);
                LinearLayoutManager layoutManager = recyclerView.getLayoutManager();

                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                // If the total item count is zero and the previous isn't, assume the
                // list is invalidated and should be reset back to initial state
                if (totalItemCount < previousTotalItemCount) {
                    this.currentPage = this.startingPageIndex;
                    this.previousTotalItemCount = totalItemCount;
                    if (totalItemCount == 0) {
                        this.loading = true;
                    }
                }
                // If it’s still loading, we check to see if the dataset count has
                // changed, if so we conclude it has finished loading and update the current page
                // number and total item count.
                if (loading && (totalItemCount > previousTotalItemCount)) {
                    loading = false;
                    previousTotalItemCount = totalItemCount;
                }

                // If it isn’t currently loading, we check to see if we have breached
                // the visibleThreshold and need to reload more data.
                // If we do need to reload some more data, we execute onLoadMore to fetch the data.
                // threshold should reflect how many total columns there are too
                if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
                    currentPage++;
                    onLoadMore(currentPage, totalItemCount, recyclerView);
                    loading = true;
                }
            }
        }
    }

    // Call this method whenever performing new searches
    public void resetState() {
        this.currentPage = this.startingPageIndex;
        this.previousTotalItemCount = 0;
        this.loading = true;
    }

    // Defines the process for actually loading more data based on page
    public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);
}
