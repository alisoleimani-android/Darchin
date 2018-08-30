package co.tinab.darchin.model.order;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by A.S.R on 4/11/2018.
 */

public class Vote implements Parcelable {
    @Expose
    @SerializedName("food_quality")
    private int foodQuality;

    @Expose
    @SerializedName("packing_quality")
    private int packingQuality;

    @Expose
    @SerializedName("courier_speed")
    private int courierSpeed;

    @Expose
    @SerializedName("courier_ethics")
    private int courierEthics;

    @Expose
    @SerializedName("comment")
    Comment comment;

    Vote(){}

    private Vote(Parcel in) {
        foodQuality = in.readInt();
        packingQuality = in.readInt();
        courierSpeed = in.readInt();
        courierEthics = in.readInt();
        comment = in.readParcelable(Comment.class.getClassLoader());
    }

    public static final Creator<Vote> CREATOR = new Creator<Vote>() {
        @Override
        public Vote createFromParcel(Parcel in) {
            return new Vote(in);
        }

        @Override
        public Vote[] newArray(int size) {
            return new Vote[size];
        }
    };

    public Comment getComment() {
        if (comment == null) {
            return new Comment();
        }
        return comment;
    }

    public int getCourierEthics() {
        return courierEthics;
    }

    public int getCourierSpeed() {
        return courierSpeed;
    }

    public int getFoodQuality() {
        return foodQuality;
    }

    public int getPackingQuality() {
        return packingQuality;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(foodQuality);
        dest.writeInt(packingQuality);
        dest.writeInt(courierSpeed);
        dest.writeInt(courierEthics);
        dest.writeParcelable(comment, flags);
    }
}
