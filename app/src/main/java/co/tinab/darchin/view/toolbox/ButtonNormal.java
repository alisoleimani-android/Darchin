package co.tinab.darchin.view.toolbox;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import java.util.Locale;

/**
 * Created by ali.soleimani on 8/5/2017.
 */

public class ButtonNormal extends android.support.v7.widget.AppCompatButton {
    public ButtonNormal(Context context) {
        super(context);
        setFont(context);
    }

    public ButtonNormal(Context context, AttributeSet attrs) {
        super(context,attrs);
        setFont(context);
    }

    public ButtonNormal(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont(context);
    }

    private void setFont(Context context){
        if (Locale.getDefault().getLanguage().equals("fa")) {
            setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/Normal.ttf"));
        }else {
            setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/LatinNormal.ttf"));
        }
    }
}
