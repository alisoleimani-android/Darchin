package co.tinab.darchin.model.store;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by A.S.R on 4/5/2018.
 */

public class ProductType implements Parcelable {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("name")
    private String name;

    ProductType(){}

    private ProductType(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<ProductType> CREATOR = new Creator<ProductType>() {
        @Override
        public ProductType createFromParcel(Parcel in) {
            return new ProductType(in);
        }

        @Override
        public ProductType[] newArray(int size) {
            return new ProductType[size];
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
