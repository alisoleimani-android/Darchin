package co.tinab.darchin.model.network.interfaces;

import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.network.resources.BasicResource;
import co.tinab.darchin.model.network.resources.FavoriteResource;
import co.tinab.darchin.model.network.resources.UserResource;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by A.S.R on 3/22/2018.
 */

public interface AccountInterface {
    @GET("account/info")
    Call<UserResource> getAccountInfo(@Header(Constant.Auth) String token);

    @PUT("account/info")
    Call<BasicResource> editAccountInfo(@Header(Constant.Auth) String token, @Body RequestBody body);

    @POST("account/favourites")
    Call<BasicResource> createFavorite(@Header(Constant.Auth) String token, @Body RequestBody body);

    @DELETE("account/favourites/{store_id}")
    Call<BasicResource> deleteFavorite(@Header(Constant.Auth) String token, @Path("store_id") String storeID);

    @GET("account/favourites")
    Call<FavoriteResource> getFavoriteList(@Header(Constant.Auth) String token);

    @PUT("account/password")
    Call<BasicResource> changePassword(@Header(Constant.Auth) String token, @Body RequestBody body);
}
