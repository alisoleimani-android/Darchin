package co.tinab.darchin.model.network.resources;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.address.City;

/**
 * Created by A.S.R on 3/23/2018.
 */

public class CityResource extends BasicResource {
    @Expose
    @SerializedName(Constant.DATA)
    private List<City> cityList;

    @Nullable
    public List<City> getCityList() {
        return cityList;
    }
}
