package co.tinab.darchin.model.address;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import co.tinab.darchin.R;

/**
 * Created by A.S.R on 2/19/2018.
 */

public class Address implements Parcelable{
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("tag")
    private String tag;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("location")
    private Location location;

    private AreaSearch areaSearch;

    public Address(){}

    private Address(Parcel in) {
        id = in.readInt();
        tag = in.readString();
        name = in.readString();
        location = in.readParcelable(Location.class.getClassLoader());
    }

    public AreaSearch getAreaSearch() {
        return areaSearch;
    }

    public void setAreaSearch(AreaSearch areaSearch) {
        this.areaSearch = areaSearch;
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
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

    public String getFullAddress(Context context){
        if (getLocation() != null
                && getLocation().getArea() != null
                && getLocation().getArea().getCity() != null
                && getLocation().getArea().getCity().getName() != null) {

            return getLocation().getArea().getCity().getName()
                    .concat(" , ")
                    .concat(getLocation().getArea().getName())
                    .concat(" , ")
                    .concat(getLocation().getName())
                    .concat(" , ")
                    .concat(getName());
        }
        return context.getString(R.string.unknown);
    }

    public String getAreaName(){
        return location.getArea().getCity().getName()
                .concat(" , ")
                .concat(location.getArea().getName())
                .concat(" , ")
                .concat(location.getName());
    }

    public String getTag() {
        if (tag == null) {
            return "";
        }
        return tag;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tag);
        dest.writeString(name);
        dest.writeParcelable(location,flags);
    }
}
