package co.tinab.darchin.model.network.request_helpers;

import com.google.gson.JsonObject;

import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.network.interfaces.OrderInterface;
import co.tinab.darchin.model.network.MyRetrofit;
import co.tinab.darchin.model.network.resources.BasicResource;
import co.tinab.darchin.model.network.resources.OrderCollectionResource;
import co.tinab.darchin.model.network.resources.OrderResource;
import co.tinab.darchin.model.network.resources.TransactionResource;
import co.tinab.darchin.model.store.Cart;
import co.tinab.darchin.model.store.ProductItem;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by A.S.R on 4/11/2018.
 */

public class OrderRequestHelper {
    public static OrderRequestHelper getInstance(){
        return new OrderRequestHelper();
    }

    public Call<OrderCollectionResource> getOrders(String token,int take){
        OrderInterface orderInterface = MyRetrofit.getRetrofitInstance().create(OrderInterface.class);
        return orderInterface.getOrders(token,"0",String.valueOf(take));
    }

    public Call<BasicResource> comment(String token,int orderId,String text,int productQuality
            , int packingQuality,int courierSpeed,int courierEthics){
        // Create Body
        JsonObject voteObject = new JsonObject();
        voteObject.addProperty("food_quality",productQuality);
        voteObject.addProperty("packing_quality",packingQuality);
        voteObject.addProperty("courier_speed",courierSpeed);
        voteObject.addProperty("courier_ethics",courierEthics);

        JsonObject mainObject = new JsonObject();
        mainObject.addProperty("order_id",orderId);
        mainObject.addProperty("text",text);
        mainObject.add("votes",voteObject);

        RequestBody requestBody = RequestBody.create(MediaType.parse(Constant.JSON), mainObject.toString());

        OrderInterface orderInterface = MyRetrofit.getRetrofitInstance().create(OrderInterface.class);
        return orderInterface.comment(token,requestBody);
    }

    public Call<TransactionResource> getTransactions(String token){
        OrderInterface orderInterface = MyRetrofit.getRetrofitInstance().create(OrderInterface.class);
        return orderInterface.getTransactions(token);
    }

    public Call<OrderResource> createOrder(String token, Cart cart){
        JsonObject itemsObject = new JsonObject();
        for (ProductItem item : cart.getItems()){
            itemsObject.addProperty(String.valueOf(item.getId()),item.getCount());
        }

        JsonObject mainObject = new JsonObject();
        mainObject.addProperty("type","order");
        mainObject.addProperty("deliver_type",cart.getDeliverType());
        mainObject.addProperty("store_id",cart.getStore().getStoreId());
        mainObject.addProperty("deliver_time",cart.getSelectedPeriod().getOrderDateTime());
        mainObject.add("items",itemsObject);

        if (cart.getSelectedAddress() != null) {
            mainObject.addProperty("address_id",cart.getSelectedAddress().getId());
        }

        if (cart.getCouponCode() != null){
            mainObject.addProperty("coupon_code",cart.getCouponCode());
        }

        if (cart.getDescription() != null) {
            mainObject.addProperty("description",cart.getDescription());
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse(Constant.JSON), mainObject.toString());

        OrderInterface orderInterface = MyRetrofit.getRetrofitInstance().create(OrderInterface.class);
        return orderInterface.createPayment(token,requestBody);
    }

    public Call<BasicResource> cancelOrder(String token,int orderId){
        // Create Body
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status","cancel");
        RequestBody requestBody = RequestBody.create(MediaType.parse(Constant.JSON), jsonObject.toString());

        OrderInterface orderInterface = MyRetrofit.getRetrofitInstance().create(OrderInterface.class);
        return orderInterface.changeStatus(token,String.valueOf(orderId),requestBody);
    }
}
