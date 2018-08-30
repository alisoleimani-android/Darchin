package co.tinab.darchin.model.network.resources;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import co.tinab.darchin.model.address.AreaSearch;
import co.tinab.darchin.model.Constant;

/**
 * Created by A.S.R on 3/23/2018.
 */

public class AreaResource extends BasicResource {
    @Expose
    @SerializedName(Constant.DATA)
    private List<AreaSearch> areaList;

    @Nullable
    public List<AreaSearch> getAreaList() {
        return areaList;
    }

}
