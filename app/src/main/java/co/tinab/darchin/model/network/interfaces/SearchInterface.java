package co.tinab.darchin.model.network.interfaces;

import co.tinab.darchin.model.network.resources.SearchResultResource;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by A.S.R on 4/10/2018.
 */

public interface SearchInterface {
    @GET("search/all/{city_id}/{string}")
    Call<SearchResultResource> search(@Path("city_id") String cityId, @Path("string") String string);
}
