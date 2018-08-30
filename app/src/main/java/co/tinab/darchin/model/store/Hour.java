package co.tinab.darchin.model.store;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by A.S.R on 3/29/2018.
 */

public class Hour implements Parcelable{
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("day")
    private String day;

    @Expose
    @SerializedName("open")
    private String open;

    @Expose
    @SerializedName("close")
    private String close;

    @Expose
    @SerializedName("is_delivery")
    private boolean isDelivery;

    @Expose
    @SerializedName("is_inside")
    private boolean isInside;

    @Expose
    @SerializedName("order_active")
    private boolean orderActive;

    private Hour(Parcel in) {
        id = in.readInt();
        name = in.readString();
        day = in.readString();
        open = in.readString();
        close = in.readString();
        isDelivery = in.readByte() != 0;
        isInside = in.readByte() != 0;
    }

    public static final Creator<Hour> CREATOR = new Creator<Hour>() {
        @Override
        public Hour createFromParcel(Parcel in) {
            return new Hour(in);
        }

        @Override
        public Hour[] newArray(int size) {
            return new Hour[size];
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

    Date getClose() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.US);
            return format.parse(close);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    String getDay() {
        if (day == null) {
            return "";
        }
        return day;
    }

    Date getOpen() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.US);
            return format.parse(open);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getOpenStr(){
        if (open == null) {
            return "";
        }
        return open;
    }

    public String getCloseStr(){
        if (close == null) {
            return "";
        }
        return close;
    }

    boolean isDelivery() {
        return isDelivery;
    }

    boolean isInside() {
        return isInside;
    }

    boolean isOrderActive() {
        return orderActive;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(day);
        dest.writeString(open);
        dest.writeString(close);
        dest.writeByte((byte) (isDelivery ? 1 : 0));
        dest.writeByte((byte) (isInside ? 1 : 0));
    }
}
