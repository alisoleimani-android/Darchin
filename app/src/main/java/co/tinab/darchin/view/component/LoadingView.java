package co.tinab.darchin.view.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import co.tinab.darchin.R;

/**
 * Created by A.S.R on 3/23/2018.
 */

public class LoadingView extends FrameLayout {
    public LoadingView(@NonNull Context context) {
        this(context,null,0);
    }

    public LoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public LoadingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(getContext())
                .inflate(R.layout.layout_loading_view, this, true);
    }

    public void show(){
        if (getVisibility() != View.VISIBLE) {
            setVisibility(View.VISIBLE);
        }
    }

    public void hide(){
        setVisibility(View.GONE);
    }

}
