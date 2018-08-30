package co.tinab.darchin.model.network.request_helpers;

import com.google.gson.JsonObject;

import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.network.interfaces.AuthenticationInterface;
import co.tinab.darchin.model.network.MyRetrofit;
import co.tinab.darchin.model.network.resources.BasicResource;
import co.tinab.darchin.model.network.resources.RegisterCheckResource;
import co.tinab.darchin.model.network.resources.UserResource;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by A.S.R on 3/19/2018.
 */

public class AuthRequestHelper {
    public static AuthRequestHelper getInstance(){
        return new AuthRequestHelper();
    }

    public Call<UserResource> login(String username, String password) {
        // Create Body
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username",username);
        jsonObject.addProperty("password",password);
        RequestBody requestBody = RequestBody.create(MediaType.parse(Constant.JSON), jsonObject.toString());

        AuthenticationInterface authenticationInterface = MyRetrofit.getRetrofitInstance().create(AuthenticationInterface.class);
        return authenticationInterface.login(requestBody);
    }

    public Call<RegisterCheckResource> registerCheck(String username){
        // Create Body
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username",username);
        RequestBody requestBody = RequestBody.create(MediaType.parse(Constant.JSON), jsonObject.toString());

        AuthenticationInterface authenticationInterface = MyRetrofit.getRetrofitInstance().create(AuthenticationInterface.class);
        return authenticationInterface.registerCheck(requestBody);
    }

    public Call<UserResource> register(String name, String username, String password){
        // Create Body
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("password", password);
        jsonObject.addProperty("password_confirmation", password);
        RequestBody requestBody = RequestBody.create(MediaType.parse(Constant.JSON), jsonObject.toString());

        AuthenticationInterface authenticationInterface = MyRetrofit.getRetrofitInstance().create(AuthenticationInterface.class);
        return authenticationInterface.register(requestBody);
    }

    public Call<BasicResource> activationCheck(String username, String activationCode){
        // Create Body
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("activation_code", activationCode);
        RequestBody requestBody = RequestBody.create(MediaType.parse(Constant.JSON), jsonObject.toString());

        AuthenticationInterface authenticationInterface = MyRetrofit.getRetrofitInstance().create(AuthenticationInterface.class);
        return authenticationInterface.activationCheck(requestBody);
    }

    public Call<BasicResource> activationResend(String username){
        // Create Body
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        RequestBody requestBody = RequestBody.create(MediaType.parse(Constant.JSON), jsonObject.toString());

        AuthenticationInterface authenticationInterface = MyRetrofit.getRetrofitInstance().create(AuthenticationInterface.class);
        return authenticationInterface.activationResend(requestBody);
    }

    public Call<BasicResource> forgetSend(String username){
        // Create Body
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username",username);
        RequestBody requestBody = RequestBody.create(MediaType.parse(Constant.JSON), jsonObject.toString());

        AuthenticationInterface authenticationInterface = MyRetrofit.getRetrofitInstance().create(AuthenticationInterface.class);
        return authenticationInterface.forgetSend(requestBody);
    }

    public Call<BasicResource> forgetCheck(String username, String password, String code){
        // Create Body
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username",username);
        jsonObject.addProperty("password",password);
        jsonObject.addProperty("password_confirmation",password);
        jsonObject.addProperty("forgetpassword_code",code);
        RequestBody requestBody = RequestBody.create(MediaType.parse(Constant.JSON), jsonObject.toString());

        AuthenticationInterface authenticationInterface = MyRetrofit.getRetrofitInstance().create(AuthenticationInterface.class);
        return authenticationInterface.forgetCheck(requestBody);
    }

    public Call<BasicResource> logout(String token){
        AuthenticationInterface authenticationInterface = MyRetrofit.getRetrofitInstance()
                .create(AuthenticationInterface.class);
        return authenticationInterface.logout(token);
    }
}
