package co.tinab.darchin.model.network.resources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.order.Order;

public class OrderResource extends BasicResource {
    @Expose
    @SerializedName(Constant.DATA)
    private Order order;

    public Order getOrder() {
        return order;
    }
}
