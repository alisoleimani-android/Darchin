package co.tinab.darchin.view.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.tinab.darchin.R;
import co.tinab.darchin.view.toolbox.ButtonNormal;
import co.tinab.darchin.view.toolbox.TextViewLight;

/**
 * Created by A.S.R on 3/22/2018.
 */

public class QuestionDialog extends DialogFragment implements View.OnClickListener {
    public interface ClickListener{
        void onPositiveBtnClicked(DialogFragment dialogFragment);
        void onNegativeBtnClicked(DialogFragment dialogFragment);
    }
    private ClickListener listener;
    public void setListener(ClickListener listener){
        this.listener = listener;
    }


    private String description;
    private String positiveBtnName,negativeBtnName;

    public static QuestionDialog newInstance(String positiveBtnName,String negativeBtnName,String description){
        QuestionDialog dialog = new QuestionDialog();
        Bundle args = new Bundle();
        args.putString("description",description);
        args.putString("positiveBtnName",positiveBtnName);
        args.putString("negativeBtnName",negativeBtnName);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            positiveBtnName = getArguments().getString("positiveBtnName");
            negativeBtnName = getArguments().getString("negativeBtnName");
            description = getArguments().getString("description");
        }

        setStyle(DialogFragment.STYLE_NO_TITLE,R.style.MyDialogTheme);
        setCancelable(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_question,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextViewLight txtDesc = view.findViewById(R.id.txt_desc);
        txtDesc.setText(description);

        ButtonNormal btnPositive = view.findViewById(R.id.btn_positive);
        btnPositive.setText(positiveBtnName);
        btnPositive.setOnClickListener(this);

        ButtonNormal btnNegative = view.findViewById(R.id.btn_negative);
        btnNegative.setText(negativeBtnName);
        btnNegative.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_positive:
                listener.onPositiveBtnClicked(this);
                break;

            case R.id.btn_negative:
                listener.onNegativeBtnClicked(this);
                break;
        }
    }
}
