package co.tinab.darchin.model.network.request_helpers;

import co.tinab.darchin.model.network.interfaces.SettingsInterface;
import co.tinab.darchin.model.network.MyRetrofit;
import co.tinab.darchin.model.network.resources.SettingsResource;
import retrofit2.Call;

/**
 * Created by A.S.R on 4/15/2018.
 */

public class SettingsRequestHelper {
    private static SettingsRequestHelper requestHelper = new SettingsRequestHelper();
    public static SettingsRequestHelper getInstance(){
        return requestHelper;
    }

    public Call<SettingsResource> getSettings(){
        SettingsInterface settingsInterface = MyRetrofit.getRetrofitInstance().create(SettingsInterface.class);
        return settingsInterface.getSettings();
    }
}
