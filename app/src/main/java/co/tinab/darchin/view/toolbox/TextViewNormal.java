package co.tinab.darchin.view.toolbox;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import java.util.Locale;

/**
 * Created by A.S.R on 12/30/2017.
 */

public class TextViewNormal extends AppCompatTextView {
    public TextViewNormal(Context context) {
        super(context);
        setFont(context);
    }

    public TextViewNormal(Context context, AttributeSet attrs) {
        super(context,attrs);
        setFont(context);
    }

    public TextViewNormal(Context context, AttributeSet attrs, int defStyleAttr) {
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
