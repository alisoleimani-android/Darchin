package co.tinab.darchin.model.network.interfaces;

import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.network.resources.BasicResource;
import co.tinab.darchin.model.network.resources.OrderCollectionResource;
import co.tinab.darchin.model.network.resources.OrderResource;
import co.tinab.darchin.model.network.resources.TransactionResource;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by A.S.R on 4/11/2018.
 */

public interface OrderInterface {
    @GET("order")
    Call<OrderCollectionResource> getOrders(@Header(Constant.Auth) String token);

    @POST("comment")
    Call<BasicResource> comment(@Header(Constant.Auth) String token, @Body RequestBody body);

    @GET("order/accounting")
    Call<TransactionResource> getTransactions(@Header(Constant.Auth) String token);

    @POST("payment/create")
    Call<OrderResource> createPayment(@Header(Constant.Auth) String token, @Body RequestBody body);

    @PUT("order/{order_id}")
    Call<BasicResource> changeStatus(@Header(Constant.Auth) String token, @Path("order_id") String orderId, @Body RequestBody body);
}
