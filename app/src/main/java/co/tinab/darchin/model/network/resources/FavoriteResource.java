package co.tinab.darchin.model.network.resources;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.Favorite;

/**
 * Created by A.S.R on 3/29/2018.
 */

public class FavoriteResource extends BasicResource {
    @Expose
    @SerializedName(Constant.DATA)
    private List<Favorite> favoriteList;

    @Nullable
    public List<Favorite> getFavoriteList() {
        return favoriteList;
    }
}
