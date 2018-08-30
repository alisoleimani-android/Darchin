package co.tinab.darchin.model.address;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by A.S.R on 3/23/2018.
 */

public class Country implements Parcelable {
    @Expose
    @SerializedName("id")
    private int countryId;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("money_unit")
    private String moneyUnit;

    private Country(Parcel in) {
        countryId = in.readInt();
        name = in.readString();
        moneyUnit = in.readString();
    }

    public static final Creator<Country> CREATOR = new Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel in) {
            return new Country(in);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };

    public int getCountryId() {
        return countryId;
    }

    public String getName() {
        if (name == null) {
            return "";
        }
        return name;
    }

    public String getMoneyUnit() {
        if (moneyUnit == null) {
            return "";
        }
        return moneyUnit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(countryId);
        dest.writeString(name);
        dest.writeString(moneyUnit);
    }
}
