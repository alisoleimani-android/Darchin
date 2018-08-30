package co.tinab.darchin.view.toolbox;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.util.Locale;

/**
 * Created by ali.soleimani on 7/31/2017.
 */

public class TextViewLight extends android.support.v7.widget.AppCompatTextView {
    public TextViewLight(Context context) {
        super(context);
        setFont(context);
    }

    public TextViewLight(Context context, @Nullable AttributeSet attrs) {
        super(context,attrs);
        setFont(context);
    }

    public TextViewLight(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont(context);
    }

    private void setFont(Context context){
        if (Locale.getDefault().getLanguage().equals("fa")) {
            setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/Light.ttf"));
        }else {
            setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/LatinLight.ttf"));
        }
    }
}
