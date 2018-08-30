package co.tinab.darchin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import co.tinab.darchin.model.store.Store;

/**
 * Created by A.S.R on 3/29/2018.
 */

public class Favorite {

    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("store")
    private Store store;

    // Getters:
    public int getId() {
        return id;
    }

    public Store getStore() {
        return store;
    }
}
