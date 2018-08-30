package co.tinab.darchin.model.network.resources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.order.Order;

/**
 * Created by A.S.R on 4/11/2018.
 */

public class OrderCollectionResource extends BasicResource {
    @Expose
    @SerializedName(Constant.DATA)
    private List<Order> orders;

    public List<Order> getOrders() {
        return orders;
    }
}
