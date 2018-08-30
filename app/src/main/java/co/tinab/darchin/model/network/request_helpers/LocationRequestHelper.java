package co.tinab.darchin.model.network.request_helpers;

import com.google.gson.JsonObject;

import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.network.interfaces.LocationInterface;
import co.tinab.darchin.model.network.MyRetrofit;
import co.tinab.darchin.model.network.resources.AddressResource;
import co.tinab.darchin.model.network.resources.AreaResource;
import co.tinab.darchin.model.network.resources.BasicResource;
import co.tinab.darchin.model.network.resources.CityResource;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by A.S.R on 3/23/2018.
 */

public class LocationRequestHelper {
    public static LocationRequestHelper getInstance(){
        return new LocationRequestHelper();
    }

    public Call<CityResource> getCityList(String url){
        LocationInterface locationInterface = MyRetrofit.getRetrofitInstance().create(LocationInterface.class);
        return locationInterface.getCityList(url);
    }

    public Call<AddressResource> getAddressList(String token){
        LocationInterface locationInterface = MyRetrofit.getRetrofitInstance().create(LocationInterface.class);
        return locationInterface.getAddressList(token);
    }

    public Call<AreaResource> searchAddress(String cityID, String address){
        LocationInterface locationInterface = MyRetrofit.getRetrofitInstance().create(LocationInterface.class);
        return locationInterface.searchAddress(cityID,address);
    }

    public Call<BasicResource> editAddress(String token, int addressID, String name, String tag, int locationID){
        // Create Body
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name",name);
        jsonObject.addProperty("location_id",locationID);
        jsonObject.addProperty("tag",tag);
        RequestBody requestBody = RequestBody.create(MediaType.parse(Constant.JSON), jsonObject.toString());

        LocationInterface locationInterface = MyRetrofit.getRetrofitInstance().create(LocationInterface.class);
        return locationInterface.editAddress(token,String.valueOf(addressID),requestBody);
    }

    public Call<BasicResource> deleteAddress(String token, int addressID){
        LocationInterface locationInterface = MyRetrofit.getRetrofitInstance().create(LocationInterface.class);
        return locationInterface.deleteAddress(token,String.valueOf(addressID));
    }

    public Call<BasicResource> createAddress(String token, String name, int locationID, String tag){
        // Create Body
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name",name);
        jsonObject.addProperty("location_id",locationID);
        jsonObject.addProperty("tag",tag);
        RequestBody requestBody = RequestBody.create(MediaType.parse(Constant.JSON), jsonObject.toString());

        LocationInterface locationInterface = MyRetrofit.getRetrofitInstance().create(LocationInterface.class);
        return locationInterface.createAddress(token,requestBody);
    }
}
