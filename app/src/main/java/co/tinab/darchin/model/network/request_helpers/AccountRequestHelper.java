package co.tinab.darchin.model.network.request_helpers;

import com.google.gson.JsonObject;

import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.network.interfaces.AccountInterface;
import co.tinab.darchin.model.network.MyRetrofit;
import co.tinab.darchin.model.network.resources.BasicResource;
import co.tinab.darchin.model.network.resources.FavoriteResource;
import co.tinab.darchin.model.network.resources.UserResource;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by A.S.R on 3/22/2018.
 */

public class AccountRequestHelper {
    public static AccountRequestHelper getInstance(){
        return new AccountRequestHelper();
    }

    public Call<UserResource> getAccountInfo(String token){
        AccountInterface accountInterface = MyRetrofit.getRetrofitInstance().create(AccountInterface.class);
        return accountInterface.getAccountInfo(token);
    }

    public Call<BasicResource> editAccountInfo(String token, String name, String mobile, String phone){
        // Create Body
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name",name);
        jsonObject.addProperty("username",mobile);
        jsonObject.addProperty("phone",phone);
        RequestBody requestBody = RequestBody.create(MediaType.parse(Constant.JSON), jsonObject.toString());

        AccountInterface accountInterface = MyRetrofit.getRetrofitInstance().create(AccountInterface.class);
        return accountInterface.editAccountInfo(token,requestBody);
    }

    public Call<BasicResource> createFavorite(String token, int storeID){
        // Create Body
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("store_id", storeID);
        RequestBody requestBody = RequestBody.create(MediaType.parse(Constant.JSON), jsonObject.toString());

        AccountInterface accountInterface = MyRetrofit.getRetrofitInstance().create(AccountInterface.class);
        return accountInterface.createFavorite(token,requestBody);
    }

    public Call<BasicResource> deleteFavorite(String token, int storeID){
        AccountInterface accountInterface = MyRetrofit.getRetrofitInstance().create(AccountInterface.class);
        return accountInterface.deleteFavorite(token, String.valueOf(storeID));
    }

    public Call<FavoriteResource> getFavoriteList(String token){
        AccountInterface accountInterface = MyRetrofit.getRetrofitInstance().create(AccountInterface.class);
        return accountInterface.getFavoriteList(token);
    }

    public Call<BasicResource> changePassword(String token, String currentPass, String newPass){
        // Create Body
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("old_password", currentPass);
        jsonObject.addProperty("new_password", newPass);
        jsonObject.addProperty("new_password_confirmation", newPass);
        RequestBody requestBody = RequestBody.create(MediaType.parse(Constant.JSON), jsonObject.toString());

        AccountInterface accountInterface = MyRetrofit.getRetrofitInstance().create(AccountInterface.class);
        return accountInterface.changePassword(token,requestBody);
    }
}
