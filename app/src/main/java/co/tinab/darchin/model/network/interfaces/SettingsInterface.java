package co.tinab.darchin.model.network.interfaces;

import co.tinab.darchin.model.network.resources.SettingsResource;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by A.S.R on 4/15/2018.
 */

public interface SettingsInterface {
    @GET("setting")
    Call<SettingsResource> getSettings();
}
