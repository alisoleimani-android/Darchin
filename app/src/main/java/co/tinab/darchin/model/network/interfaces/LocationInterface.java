package co.tinab.darchin.model.network.interfaces;

import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.network.resources.AddressResource;
import co.tinab.darchin.model.network.resources.AreaResource;
import co.tinab.darchin.model.network.resources.BasicResource;
import co.tinab.darchin.model.network.resources.CityResource;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by A.S.R on 3/23/2018.
 */

public interface LocationInterface {
    @GET()
    Call<CityResource> getCityList(@Url String url);

    @GET("account/addresses")
    Call<AddressResource> getAddressList(@Header(Constant.Auth) String token);

    @GET("search/address/{city_id}/{address}")
    Call<AreaResource> searchAddress(@Path("city_id") String cityID, @Path("address") String address);

    @PUT("account/addresses/{id}")
    Call<BasicResource> editAddress(@Header(Constant.Auth) String token, @Path("id") String addressID, @Body RequestBody body);

    @DELETE("account/addresses/{id}")
    Call<BasicResource> deleteAddress(@Header(Constant.Auth) String token, @Path("id") String addressID);

    @POST("account/addresses")
    Call<BasicResource> createAddress(@Header(Constant.Auth) String token, @Body RequestBody body);
}
