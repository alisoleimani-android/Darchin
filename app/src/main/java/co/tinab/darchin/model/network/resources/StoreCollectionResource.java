package co.tinab.darchin.model.network.resources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.store.Store;

/**
 * Created by A.S.R on 4/10/2018.
 */

public class StoreCollectionResource extends BasicResource {
    @Expose
    @SerializedName(Constant.DATA)
    private List<Store> stores;

    public List<Store> getStores() {
        return stores;
    }
}
