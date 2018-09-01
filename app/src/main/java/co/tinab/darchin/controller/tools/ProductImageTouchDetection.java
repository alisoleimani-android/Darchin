package co.tinab.darchin.controller.tools;

import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public abstract class ProductImageTouchDetection implements View.OnTouchListener {
    protected abstract void onLongPress();
    protected abstract void onRelease();

    private Timer timer = new Timer();

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getActionMasked();

        if (action == MotionEvent.ACTION_DOWN) {
            // touch & hold started
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // touch & hold was long
                    onLongPress();
                }
            }, 400);
            return true;

        }else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            // touch & hold stopped
            onRelease();
            timer.cancel();
            timer = new Timer();
            return true;
        }
        return false;
    }
}
