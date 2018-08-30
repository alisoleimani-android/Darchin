package co.tinab.darchin.model.network.resources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.store.Store;

/**
 * Created by A.S.R on 4/9/2018.
 */

public class StoreResource extends BasicResource {
    @Expose
    @SerializedName(Constant.DATA)
    private Store store;

    public Store getStore() {
        return store;
    }
}
