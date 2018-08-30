package co.tinab.darchin.model.network.interfaces;

import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.network.resources.BasicResource;
import co.tinab.darchin.model.network.resources.RegisterCheckResource;
import co.tinab.darchin.model.network.resources.UserResource;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by A.S.R on 3/19/2018.
 */

public interface AuthenticationInterface {
    @POST("login")
    Call<UserResource> login(@Body RequestBody body);

    @POST("register/check")
    Call<RegisterCheckResource> registerCheck(@Body RequestBody body);

    @POST("register")
    Call<UserResource> register(@Body RequestBody body);

    @POST("register/activation/check")
    Call<BasicResource> activationCheck(@Body RequestBody body);

    @POST("register/activation/resend")
    Call<BasicResource> activationResend(@Body RequestBody body);

    @POST("login/forget/send")
    Call<BasicResource> forgetSend(@Body RequestBody body);

    @POST("login/forget/check")
    Call<BasicResource> forgetCheck(@Body RequestBody body);

    @GET("account/logout")
    Call<BasicResource> logout(@Header(Constant.Auth) String api_token);
}
