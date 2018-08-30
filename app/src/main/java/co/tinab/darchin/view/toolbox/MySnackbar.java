package co.tinab.darchin.view.toolbox;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import co.tinab.darchin.R;

/**
 * Created by A.S.R on 1/29/2018.
 */

public class MySnackbar {
    public static short Success = 0;
    public static short Alert = 1;
    public static short Failure = 2;
    public static short Info = 3;

    public static Snackbar make(View parent,short type,int messageResID){
        Snackbar snackbar = Snackbar
                .make(parent, parent.getContext().getString(messageResID), Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setPadding(0,0,0,0);

        TypedArray styledAttributes = parent.getContext().getTheme().obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        snackbarView.setMinimumHeight(actionBarSize);

        switch (type){
            case 0: // Success
                snackbarView.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.success));
                break;
            case 1: // Alert
                snackbarView.setBackgroundColor(ContextCompat.getColor(parent.getContext(),R.color.alert));
                break;
            case 2: // Failure
                snackbarView.setBackgroundColor(ContextCompat.getColor(parent.getContext(),R.color.failure));
                break;
            case 3: // Info
                snackbarView.setBackgroundColor(ContextCompat.getColor(parent.getContext(),R.color.info));
                break;
        }

        TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
        textView.setGravity(Gravity.START|Gravity.CENTER_VERTICAL);
        textView.setTextSize(14);
        if (Locale.getDefault().getLanguage().equals("fa")) {
            textView.setTypeface(Typeface.createFromAsset(parent.getContext().getAssets(),"fonts/Normal.ttf"));
        }else {
            textView.setTypeface(Typeface.createFromAsset(parent.getContext().getAssets(),"fonts/LatinNormal.ttf"));
        }

        snackbar.setActionTextColor(Color.WHITE);
        Button btnAction = snackbarView.findViewById(android.support.design.R.id.snackbar_action);
        btnAction.setGravity(Gravity.END|Gravity.CENTER_VERTICAL);
        btnAction.setTextSize(10);
        if (Locale.getDefault().getLanguage().equals("fa")) {
            btnAction.setTypeface(Typeface.createFromAsset(parent.getContext().getAssets(),"fonts/Normal.ttf"));
        }else {
            btnAction.setTypeface(Typeface.createFromAsset(parent.getContext().getAssets(),"fonts/LatinNormal.ttf"));
        }
        return snackbar;
    }

    public static Snackbar make(View parent,short type,String message){
        Snackbar snackbar = Snackbar
                .make(parent, message, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setPadding(0,0,0,0);

        TypedArray styledAttributes = parent.getContext().getTheme().obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        snackbarView.setMinimumHeight(actionBarSize);

        switch (type){
            case 0: // Success
                snackbarView.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.success));
                break;
            case 1: // Alert
                snackbarView.setBackgroundColor(ContextCompat.getColor(parent.getContext(),R.color.alert));
                break;
            case 2: // Failure
                snackbarView.setBackgroundColor(ContextCompat.getColor(parent.getContext(),R.color.failure));
                break;
            case 3: // Info
                snackbarView.setBackgroundColor(ContextCompat.getColor(parent.getContext(),R.color.info));
                break;
        }

        TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
        textView.setGravity(Gravity.START|Gravity.CENTER_VERTICAL);
        textView.setTextSize(14);
        if (Locale.getDefault().getLanguage().equals("fa")) {
            textView.setTypeface(Typeface.createFromAsset(parent.getContext().getAssets(),"fonts/Normal.ttf"));
        }else {
            textView.setTypeface(Typeface.createFromAsset(parent.getContext().getAssets(),"fonts/LatinNormal.ttf"));
        }

        snackbar.setActionTextColor(Color.WHITE);
        Button btnAction = snackbarView.findViewById(android.support.design.R.id.snackbar_action);
        btnAction.setGravity(Gravity.END|Gravity.CENTER_VERTICAL);
        btnAction.setTextSize(10);
        if (Locale.getDefault().getLanguage().equals("fa")) {
            btnAction.setTypeface(Typeface.createFromAsset(parent.getContext().getAssets(),"fonts/Normal.ttf"));
        }else {
            btnAction.setTypeface(Typeface.createFromAsset(parent.getContext().getAssets(),"fonts/LatinNormal.ttf"));
        }
        return snackbar;
    }
}