package co.tinab.darchin.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import co.tinab.darchin.model.database.DbHelper;

/**
 * Created by A.S.R on 4/15/2018.
 */

public class Setting implements Parcelable {
    @Expose
    @SerializedName("key")
    private String key;

    @Expose
    @SerializedName("value")
    private String value;

    private Setting(Parcel in) {
        key = in.readString();
        value = in.readString();
    }

    public static final Creator<Setting> CREATOR = new Creator<Setting>() {
        @Override
        public Setting createFromParcel(Parcel in) {
            return new Setting(in);
        }

        @Override
        public Setting[] newArray(int size) {
            return new Setting[size];
        }
    };

    private String getKey() {
        if (key == null) {
            return "";
        }
        return key;
    }

    private String getValue() {
        if (value == null) {
            return "";
        }
        return value;
    }

    public static Map<String,String> getSettings(Context context){
        Map<String,String> stringMap = new LinkedHashMap<>();

        Gson gson = new Gson();
        String json = DbHelper.getInstance().select(context,"settings");
        Type type = new TypeToken<List<Setting>>(){}.getType();
        List<Setting> settings = gson.fromJson(json, type);
        if (settings == null) {
            settings = new ArrayList<>();
        }

        for (Setting setting : settings){
            stringMap.put(setting.getKey(),setting.getValue());
        }

        return stringMap;
    }

    public static void setSettings(Context context,List<Setting> settings){
        Gson gson = new Gson();
        String json = gson.toJson(settings);
        DbHelper.getInstance().insert(context,"settings",json);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(value);
    }
}
