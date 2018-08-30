package co.tinab.darchin.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Window;

import co.tinab.darchin.R;

/**
 * Created by A.S.R on 12/25/2017.
 */

public class WaitingDialog extends Dialog {

    public WaitingDialog(@NonNull Context context) {
        super(context, R.style.AppCompatAlertDialogStyle);
        buildView();
    }

    private void buildView(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_waiting);
        setCancelable(false);
    }
}
