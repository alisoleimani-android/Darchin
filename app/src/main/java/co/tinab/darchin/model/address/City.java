package co.tinab.darchin.model.address;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by A.S.R on 3/22/2018.
 */

public class City implements Parcelable {
    @Expose
    @SerializedName("id")
    private int cityId;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("order_active")
    private boolean orderActive;

    @Expose
    @SerializedName("province")
    private Province province;

    private City(Parcel in) {
        cityId = in.readInt();
        name = in.readString();
        orderActive = in.readByte() != 0;
        province = in.readParcelable(Province.class.getClassLoader());
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    public String getName() {
        if (name == null) {
            return "";
        }
        return name;
    }

    public int getCityId() {
        return cityId;
    }

    public Province getProvince() {
        return province;
    }

    public boolean isOrderActive() {
        return orderActive;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cityId);
        dest.writeString(name);
        dest.writeByte((byte) (orderActive ? 1 : 0));
        dest.writeParcelable(province, flags);
    }
}