package co.tinab.darchin.model.order;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import co.tinab.darchin.model.Constant;

/**
 * Created by A.S.R on 4/11/2018.
 */

public class Courier implements Parcelable{
    @Expose
    @SerializedName("id")
    private int courierId;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("vehicle")
    private String vehicle;

    @Expose
    @SerializedName("picture")
    private String picture;

    private Courier(Parcel in) {
        courierId = in.readInt();
        name = in.readString();
        vehicle = in.readString();
        picture = in.readString();
    }

    public static final Creator<Courier> CREATOR = new Creator<Courier>() {
        @Override
        public Courier createFromParcel(Parcel in) {
            return new Courier(in);
        }

        @Override
        public Courier[] newArray(int size) {
            return new Courier[size];
        }
    };

    public String getName() {
        if (name == null) {
            return "";
        }
        return name;
    }

    public int getCourierId() {
        return courierId;
    }

    public String getVehicle() {
        if (vehicle == null) {
            return "";
        }
        return vehicle;
    }

    public String getPicture() {
        if (picture != null) {
            return Constant.COURIER_PATH.concat(picture);
        }else {
            return Constant.COURIER_PATH;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(courierId);
        dest.writeString(name);
        dest.writeString(vehicle);
        dest.writeString(picture);
    }
}
