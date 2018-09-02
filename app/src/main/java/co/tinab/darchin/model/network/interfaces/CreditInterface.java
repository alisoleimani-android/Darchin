package co.tinab.darchin.model.network.interfaces;

import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.network.resources.BasicResource;
import co.tinab.darchin.model.network.resources.OrderResource;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by A.S.R on 4/9/2018.
 */

public interface CreditInterface {
    @POST("gift/register")
    Call<BasicResource> registerGift(@Header(Constant.Auth) String token, @Body RequestBody body);

    @POST("payment/create")
    Call<OrderResource> charge(@Header(Constant.Auth) String token, @Body RequestBody body);

    @POST("account/transfer_credit")
    Call<BasicResource> transfer(@Header(Constant.Auth) String token, @Body RequestBody body);
}
