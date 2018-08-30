package co.tinab.darchin.controller.fragment;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;

import co.tinab.darchin.controller.activity.MainActivity;
import co.tinab.darchin.controller.fragment.authentication.VerificationFragment;
import co.tinab.darchin.model.User;

/**
 * Created by A.S.R on 4/3/2018.
 */

public class BaseFragment extends Fragment {
    private Context context;

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();

        // check user activation
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkActivation();
            }
        },500);
    }

    private void checkActivation(){
        if (context != null && context instanceof MainActivity) {
            User user = User.getInstance(context);
            if (user.hasLoggedIn(context) && user.getStatus() != null && !user.getStatus().equals("active") && user.getUsername() != null) {
                String targetTag = ((MainActivity)context).getLastFragment().getClass().getName();
                ((MainActivity)context).pushFragment(VerificationFragment
                        .newInstance(VerificationFragment.VERIFICATION_ONLY,user.getUsername(),targetTag));
            }
        }
    }
}
