package co.tinab.darchin.view.toolbox;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import co.tinab.darchin.R;

/**
 * Created by A.S.R on 1/28/2018.
 */

public class ButtonCart extends FrameLayout {
    private TextView txtCount;
    private int count;

    public ButtonCart(@NonNull Context context) {
        this(context,null,0);
    }

    public ButtonCart(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public ButtonCart(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        buildView();
    }


    private void buildView(){
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.layout_cart_button, this, true);
        txtCount = view.findViewById(R.id.txt_count);
    }

    public void increase(){
        if (this.count == 0) setVisibility(View.VISIBLE);
        this.txtCount.setText(String.valueOf(++this.count));
    }

    public void decrease(){
        this.txtCount.setText(String.valueOf(--this.count));
        if (this.count == 0) hide();
    }

    public void notify(int count){
        if (count == 0){
            hide();
        }else {
            this.count = count;
            this.txtCount.setText(String.valueOf(count));
        }
    }

    private void hide(){
        this.count = 0;
        this.txtCount.setText("");
        setVisibility(View.GONE);
    }
}