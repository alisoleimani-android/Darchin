package co.tinab.darchin.view.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import co.tinab.darchin.R;

/**
 * Created by A.S.R on 12/31/2017.
 */

public class SearchContainer extends FrameLayout {

    public SearchContainer(@NonNull Context context) {
        this(context,null,0);
    }

    public SearchContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public SearchContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        buildView(context);
    }

    private void buildView(Context context){
        LayoutInflater.from(context).inflate(R.layout.layout_container_search,this,true);
    }
}
