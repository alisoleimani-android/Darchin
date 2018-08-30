package co.tinab.darchin.view.toolbox;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import java.util.Locale;

/**
 * Created by A.S.R on 1/14/2018.
 */

public class ButtonLight extends AppCompatButton {
    public ButtonLight(Context context) {
        super(context);
        setFont(context);
    }

    public ButtonLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    public ButtonLight(Context context, AttributeSet attrs, int defStyleAttr) {
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
