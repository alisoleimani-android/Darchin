package co.tinab.darchin.model.network.resources;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.Filter;

/**
 * Created by A.S.R on 3/31/2018.
 */

public class FilterResource extends BasicResource {
    @Expose
    @SerializedName(Constant.DATA)
    private List<Filter> filterList;

    @Nullable
    public List<Filter> getFilterList() {
        return filterList;
    }
}
