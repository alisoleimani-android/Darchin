package co.tinab.darchin.view.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.tinab.darchin.R;

/**
 * Created by A.S.R on 3/22/2018.
 */

public class ForceUpdateDialog extends DialogFragment implements View.OnClickListener {
    public interface ClickListener{
        void onBtnClicked(DialogFragment dialog);
    }
    private ClickListener listener;
    public void setListener(ClickListener listener){
        this.listener = listener;
    }

    public static ForceUpdateDialog newInstance(){
        return new ForceUpdateDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE,R.style.MyDialogTheme);
        setCancelable(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_force_update,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn:
                listener.onBtnClicked(this);
                break;
        }
    }
}
