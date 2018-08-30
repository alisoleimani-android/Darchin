package co.tinab.darchin.model.network.request_helpers;

import com.google.gson.JsonObject;

import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.network.MyRetrofit;
import co.tinab.darchin.model.network.interfaces.CreditInterface;
import co.tinab.darchin.model.network.resources.BasicResource;
import co.tinab.darchin.model.network.resources.OrderResource;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by A.S.R on 4/9/2018.
 */

public class CreditRequestHelper {
    public static CreditRequestHelper getInstance(){
        return new CreditRequestHelper();
    }

    public Call<BasicResource> registerGift(String token,String code){
        // Create Body
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code",code);
        RequestBody requestBody = RequestBody.create(MediaType.parse(Constant.JSON), jsonObject.toString());

        CreditInterface creditInterface = MyRetrofit.getRetrofitInstance().create(CreditInterface.class);
        return creditInterface.registerGift(token,requestBody);
    }

    public Call<OrderResource> charge(String token,String amount){
        JsonObject mainObject = new JsonObject();
        mainObject.addProperty("type","charge");
        mainObject.addProperty("amount",amount);

        RequestBody requestBody = RequestBody.create(MediaType.parse(Constant.JSON), mainObject.toString());

        CreditInterface creditInterface = MyRetrofit.getRetrofitInstance().create(CreditInterface.class);
        return creditInterface.charge(token,requestBody);
    }
}
