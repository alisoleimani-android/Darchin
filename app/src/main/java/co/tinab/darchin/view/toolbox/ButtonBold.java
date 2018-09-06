package co.tinab.darchin.view.toolbox;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import java.util.Locale;

public class ButtonBold extends android.support.v7.widget.AppCompatButton {
    public ButtonBold(Context context) {
        super(context);
        setFont(context);
    }

    public ButtonBold(Context context, AttributeSet attrs) {
        super(context,attrs);
        setFont(context);
    }

    public ButtonBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont(context);
    }

    private void setFont(Context context){
        if (Locale.getDefault().getLanguage().equals("fa")) {
            setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/Bold.ttf"));
        }else {
            setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/LatinBold.ttf"));
        }
    }
}

