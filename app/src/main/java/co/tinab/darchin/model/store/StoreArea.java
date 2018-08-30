package co.tinab.darchin.model.store;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import co.tinab.darchin.model.address.Area;

/**
 * Created by A.S.R on 4/7/2018.
 */

public class StoreArea implements Parcelable{
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("price")
    private String price;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("area")
    private Area area;

    private StoreArea(Parcel in) {
        id = in.readInt();
        price = in.readString();
        name = in.readString();
        area = in.readParcelable(Area.class.getClassLoader());
    }

    public static final Creator<StoreArea> CREATOR = new Creator<StoreArea>() {
        @Override
        public StoreArea createFromParcel(Parcel in) {
            return new StoreArea(in);
        }

        @Override
        public StoreArea[] newArray(int size) {
            return new StoreArea[size];
        }
    };

    public String getName() {
        if (name == null) {
            return "";
        }
        return name;
    }

    public int getId() {
        return id;
    }

    String getPriceStr() {
        if (price == null) {
            return "";
        }
        return price;
    }

    int getPriceInt(){
        if (price == null || price.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(price);
    }

    public Area getArea() {
        return area;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(price);
        dest.writeString(name);
        dest.writeParcelable(area, flags);
    }
}
