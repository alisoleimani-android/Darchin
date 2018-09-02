package co.tinab.darchin.model.network.resources;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import co.tinab.darchin.model.Constant;

/**
 * Created by A.S.R on 3/20/2018.
 */

public class RegisterCheckResource extends BasicResource {
    @Expose
    @SerializedName(Constant.DATA)
    private Data data;

    @Nullable
    public Data getData() {
        return data;
    }

    public class Data{
        @Expose
        @SerializedName("register")
        private boolean register;

        @Expose
        @SerializedName("name")
        private String name;

        @Expose
        @SerializedName("status")
        private String status;

        public String getStatus() {
            return status;
        }

        public boolean isRegister() {
            return register;
        }

        @Nullable
        public String getName() {
            return name;
        }
    }
}
