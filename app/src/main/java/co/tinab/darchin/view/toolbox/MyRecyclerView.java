package co.tinab.darchin.view.toolbox;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import co.tinab.darchin.controller.tools.WrapContentLinearLayoutManager;
import co.tinab.darchin.R;

/**
 * Created by A.S.R on 1/2/2018.
 */

public class MyRecyclerView extends RecyclerView {
    String orientation = "vertical";
    boolean reverse,isNestedScrollingEnable = true;
    private WrapContentLinearLayoutManager layoutManager;

    // for endless recycler view:
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    private int firstVisibleItem, visibleItemCount, totalItemCount;

    public MyRecyclerView(Context context) {
        this(context,null,0);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyRecyclerView);
        int count = typedArray.getIndexCount();
        try{
            for (int i = 0; i < count; ++i) {
                int attr = typedArray.getIndex(i);
                // the attr corresponds to the title attribute
                if(attr == R.styleable.MyRecyclerView_orientation) {
                    orientation = typedArray.getString(attr);
                } else if (attr == R.styleable.MyRecyclerView_isReverse) {
                    reverse = typedArray.getBoolean(attr,false);
                } else if (attr == R.styleable.MyRecyclerView_isNestedScrollingEnable) {
                    isNestedScrollingEnable = typedArray.getBoolean(attr,true);
                }
            }
        }
        // the recycle() will be setOnAPICallBackListener obligatorily
        finally {
            // for reuse
            typedArray.recycle();
        }
        buildView();
    }

    private void buildView(){
        setItemAnimator(new DefaultItemAnimator());
        setHasFixedSize(true);
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
        setItemViewCacheSize(20);
        setDrawingCacheEnabled(true);
        setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        layoutManager = new WrapContentLinearLayoutManager(getContext());
        if (orientation.equals("horizontal")) {
            layoutManager.setOrientation(HORIZONTAL);
        } else if (orientation.equals("vertical")) {
            layoutManager.setOrientation(VERTICAL);
        }
        layoutManager.setReverseLayout(reverse);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        layoutManager.setItemPrefetchEnabled(true);
        layoutManager.setInitialPrefetchItemCount(2);
        if (!isNestedScrollingEnable) {
            setNestedScrollingEnabled(false);
        }
        setLayoutManager(layoutManager);
    }

    public interface EndReachedListener {
        void onEndReached();
    }
    public void setOnEndReachedListener(final EndReachedListener listener){
        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    // End has been reached
                    // Do something
                    listener.onEndReached();

                    loading = true;
                }
            }
        });
    }
}
