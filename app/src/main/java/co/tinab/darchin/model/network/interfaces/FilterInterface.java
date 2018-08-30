package co.tinab.darchin.model.network.interfaces;

import co.tinab.darchin.model.network.resources.FilterResource;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by A.S.R on 3/31/2018.
 */

public interface FilterInterface {
    @GET("filter")
    Call<FilterResource> getFilterList();
}