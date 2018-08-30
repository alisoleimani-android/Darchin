package co.tinab.darchin.model.address;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by A.S.R on 4/17/2018.
 */

public class AreaSearch implements Parcelable{
    @Expose
    @SerializedName("value")
    private String value;

    @Expose
    @SerializedName("location")
    private int location;

    private AreaSearch(Parcel in) {
        value = in.readString();
        location = in.readInt();
    }

    public static final Creator<AreaSearch> CREATOR = new Creator<AreaSearch>() {
        @Override
        public AreaSearch createFromParcel(Parcel in) {
            return new AreaSearch(in);
        }

        @Override
        public AreaSearch[] newArray(int size) {
            return new AreaSearch[size];
        }
    };

    public String getValue() {
        return value;
    }

    public int getLocation() {
        return location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(value);
        dest.writeInt(location);
    }
}
