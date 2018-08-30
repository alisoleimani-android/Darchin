package co.tinab.darchin.model.address;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by A.S.R on 3/22/2018.
 */

public class Province implements Parcelable {
    @Expose
    @SerializedName("id")
    private int provinceId;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("country")
    private Country country;

    private Province(Parcel in) {
        provinceId = in.readInt();
        name = in.readString();
        country = in.readParcelable(Country.class.getClassLoader());
    }

    public static final Creator<Province> CREATOR = new Creator<Province>() {
        @Override
        public Province createFromParcel(Parcel in) {
            return new Province(in);
        }

        @Override
        public Province[] newArray(int size) {
            return new Province[size];
        }
    };

    public int getProvinceId() {
        return provinceId;
    }

    public String getName() {
        if (name == null) {
            return "";
        }
        return name;
    }

    public Country getCountry() {
        return country;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(provinceId);
        dest.writeString(name);
        dest.writeParcelable(country, flags);
    }
}
