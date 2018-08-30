package co.tinab.darchin.model.network.interfaces;

import co.tinab.darchin.model.network.resources.BasicResource;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by A.S.R on 4/1/2018.
 */

public interface SuggestsInterface {
    @POST("suggest")
    Call<BasicResource> suggestStore(@Body RequestBody body);
}
