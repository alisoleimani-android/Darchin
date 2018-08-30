package co.tinab.darchin.model.network.resources;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.User;

/**
 * Created by A.S.R on 3/19/2018.
 */

public class UserResource extends BasicResource {
    @Expose
    @SerializedName(Constant.DATA)
    private User user;

    @Nullable
    public User getUser() {
        return user;
    }
}
