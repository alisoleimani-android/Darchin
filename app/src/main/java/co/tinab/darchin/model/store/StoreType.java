package co.tinab.darchin.model.store;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by A.S.R on 3/29/2018.
 */

class StoreType implements Parcelable {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("name")
    private String name;

    private StoreType(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<StoreType> CREATOR = new Creator<StoreType>() {
        @Override
        public StoreType createFromParcel(Parcel in) {
            return new StoreType(in);
        }

        @Override
        public StoreType[] newArray(int size) {
            return new StoreType[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        if (name == null) {
            return "";
        }
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }
}
