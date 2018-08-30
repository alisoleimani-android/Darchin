package co.tinab.darchin.model.network.resources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.Setting;

/**
 * Created by A.S.R on 4/15/2018.
 */

public class SettingsResource extends BasicResource {
    @Expose
    @SerializedName(Constant.DATA)
    private List<Setting> settings;

    public List<Setting> getSettings() {
        return settings;
    }
}
