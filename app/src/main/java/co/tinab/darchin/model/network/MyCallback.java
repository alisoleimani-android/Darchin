package co.tinab.darchin.model.network;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by A.S.R on 3/19/2018.
 */

public abstract class MyCallback<T> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, final Response<T> response) {
        if (response != null) {
            if (!response.isSuccessful()) { // unsuccessful
                try {
                    if (response.code() == 401) { // unAuthorized
                        unAuthorizedDetected();

                    }else {
                        Gson gson = new Gson();

                        if (response.errorBody() != null) {
                            JsonReader reader = new JsonReader(new StringReader(response.errorBody().string()));
                            reader.setLenient(true);

                            JsonElement jsonElement = gson.fromJson(reader,JsonElement.class);
                            if ( !(jsonElement instanceof JsonNull) && jsonElement.isJsonObject()) {
                                JsonObject errorBody = jsonElement.getAsJsonObject();

                                // object is native server response
                                if (errorBody.has("status") && errorBody.has("errors") && errorBody.has("data")) {
                                    JsonElement errorsElement = errorBody.get("errors");
                                    List<String> messageList = new ArrayList<>();

                                    if (!(errorsElement instanceof JsonNull)) {
                                        JsonObject errors = errorsElement.getAsJsonObject();

                                        Set<Map.Entry<String, JsonElement>> entrySet = errors.entrySet();
                                        for(Map.Entry<String,JsonElement> entry : entrySet){
                                            if (errors.get(entry.getKey()).isJsonArray()) {
                                                JsonArray jsonArray = errors.get(entry.getKey()).getAsJsonArray();
                                                for (int i = 0 ; i < jsonArray.size() ; i++){
                                                    messageList.add(jsonArray.get(i).getAsString());
                                                }
                                            }
                                        }
                                        onRequestFailed(messageList);

                                    }else {
                                        JsonElement messageElement = errorBody.get("message");
                                        if (!(messageElement instanceof JsonNull)) {
                                            messageList.add(messageElement.getAsString());
                                            onRequestFailed(messageList);

                                        }else {
                                            onRequestFailed(null);
                                        }
                                    }

                                }else { // object is NOT native server response
                                    onRequestFailed(null);
                                }
                            }else {
                                onRequestFailed(null);
                            }

                        }else {
                            onRequestFailed(null);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    onRequestFailed(null);
                }

            }else { // 200 successful
                // todo ******* Response is Clear & NotNull *******
                onRequestSuccess(response);
            }
        }else {
            onRequestFailed(null);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        t.printStackTrace();
        onRequestFailed(null);
    }

    public abstract void onRequestSuccess(Response<T> response);

    public abstract void onRequestFailed(@Nullable List<String> messages);

    public abstract void unAuthorizedDetected();
}
