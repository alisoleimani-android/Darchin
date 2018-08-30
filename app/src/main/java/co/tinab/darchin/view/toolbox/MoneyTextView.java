package co.tinab.darchin.view.toolbox;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import co.tinab.darchin.R;

/**
 * Created by A.S.R on 3/2/2018.
 */

public class MoneyTextView extends android.support.v7.widget.AppCompatTextView {
    public MoneyTextView(Context context) {
        super(context);
        setFont(context);
    }

    public MoneyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context,attrs);
        setFont(context);
    }

    public MoneyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

    @Override
    public void setText(CharSequence text, BufferType type) {
        String moneyStr = "";
        if (text != null && !text.toString().isEmpty()) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');

            DecimalFormat decimalFormat = new DecimalFormat(
                    "###,### ".concat(getContext().getString(R.string.money_unit)), symbols);
            moneyStr = decimalFormat.format(Long.parseLong(text.toString()));
        }
        super.setText(moneyStr, type);
    }
}
