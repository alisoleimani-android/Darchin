package co.tinab.darchin.model.network.request_helpers;

import com.google.gson.JsonObject;

import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.network.interfaces.SuggestsInterface;
import co.tinab.darchin.model.network.MyRetrofit;
import co.tinab.darchin.model.network.resources.BasicResource;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by A.S.R on 4/1/2018.
 */

public class SuggestsRequestHelper {
    public static SuggestsRequestHelper getInstance(){
        return new SuggestsRequestHelper();
    }

    public Call<BasicResource> suggestStore(String name, String city, String address, String phone,
                                            String creatorName, String creatorMobile, boolean isManager){
        // Create Body
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name",name);
        jsonObject.addProperty("city",city);
        jsonObject.addProperty("address",address);
        jsonObject.addProperty("phone",phone);
        jsonObject.addProperty("creator_name",creatorName);
        jsonObject.addProperty("creator_mobile",creatorMobile);
        jsonObject.addProperty("is_manager",String.valueOf(isManager));
        RequestBody requestBody = RequestBody.create(MediaType.parse(Constant.JSON), jsonObject.toString());

        SuggestsInterface suggestsInterface = MyRetrofit.getRetrofitInstance().create(SuggestsInterface.class);
        return suggestsInterface.suggestStore(requestBody);
    }
}
