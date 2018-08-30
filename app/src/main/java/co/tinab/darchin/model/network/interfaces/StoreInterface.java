package co.tinab.darchin.model.network.interfaces;

import co.tinab.darchin.model.network.resources.CommentCollectionResource;
import co.tinab.darchin.model.network.resources.StoreCollectionResource;
import co.tinab.darchin.model.network.resources.StoreResource;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by A.S.R on 4/9/2018.
 */

public interface StoreInterface {
    @GET("store/{id}")
    Call<StoreResource> getStore(@Path("id") String id);

    // Get stores that service to a location
    @GET("store/service/{location_id}")
    Call<StoreCollectionResource> getStoreServiceByLocation(@Path("location_id") String locationId);

    @GET()
    Call<CommentCollectionResource> getComments(@Url String url);
}
