package co.tinab.darchin.view.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import co.tinab.darchin.R;
import co.tinab.darchin.view.toolbox.TextViewLight;

/**
 * Created by A.S.R on 2/13/2018.
 */

public class EmptyView extends FrameLayout{
    private String content;
    private int resId;
    private TextViewLight text;
    private ImageView imageView;

    public EmptyView(@NonNull Context context) {
        this(context,null,0);
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EmptyView);
        int count = typedArray.getIndexCount();
        try{
            for (int i = 0; i < count; ++i) {
                int attr = typedArray.getIndex(i);
                // the attr corresponds to the title attribute
                if(attr == R.styleable.EmptyView_custom_text) {
                    content = typedArray.getString(attr);
                }
                if (attr == R.styleable.EmptyView_custom_icon) {
                    resId = typedArray.getResourceId(attr,R.drawable.ic_empty);
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
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.layout_empty_view, this, true);
        text = view.findViewById(R.id.txt);
        text.setText(content);

        imageView = view.findViewById(R.id.ic_empty);
        if (resId != 0) {
            imageView.setImageResource(resId);
        }else {
            imageView.setImageResource(R.drawable.ic_empty);
        }
    }

    public void show(){
        if (getVisibility() != View.VISIBLE) {
            setVisibility(View.VISIBLE);
        }
    }

    public void hide(){
        setVisibility(View.GONE);
    }

    public void setText(String text){
        this.text.setText(text);
    }

    public void setImage(int resId){
        imageView.setImageResource(resId);
    }
}
