package co.tinab.darchin.model.network.interfaces;

import java.util.Map;

import co.tinab.darchin.model.network.resources.SectionResource;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by A.S.R on 4/8/2018.
 */

public interface SectionInterface {
    @GET("section")
    Call<SectionResource> getSections(@QueryMap Map<String,String> params);
}
