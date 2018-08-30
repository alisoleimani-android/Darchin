package co.tinab.darchin.model.order;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by A.S.R on 4/11/2018.
 */

public class Coupon implements Parcelable {
    @Expose
    @SerializedName("id")
    private int couponId;

    @Expose
    @SerializedName("code")
    private String code;

    @Expose
    @SerializedName("description")
    private String description;

    @Expose
    @SerializedName("amount")
    private long amount;

    @Expose
    @SerializedName("discount_type")
    private String discountType;

    @Expose
    @SerializedName("is_company")
    private boolean company;

    Coupon(){}

    private Coupon(Parcel in) {
        couponId = in.readInt();
        code = in.readString();
        description = in.readString();
        amount = in.readLong();
        discountType = in.readString();
        company = in.readByte() != 0;
    }

    public static final Creator<Coupon> CREATOR = new Creator<Coupon>() {
        @Override
        public Coupon createFromParcel(Parcel in) {
            return new Coupon(in);
        }

        @Override
        public Coupon[] newArray(int size) {
            return new Coupon[size];
        }
    };

    public int getCouponId() {
        return couponId;
    }

    public String getDescription() {
        if (description == null) {
            return "";
        }
        return description;
    }

    public long getAmount() {
        return amount;
    }

    public String getCode() {
        if (code == null) {
            return "";
        }
        return code;
    }

    public String getDiscountType() {
        if (discountType == null) {
            return "";
        }
        return discountType;
    }

    public boolean isCompany() {
        return company;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(couponId);
        dest.writeString(code);
        dest.writeString(description);
        dest.writeLong(amount);
        dest.writeString(discountType);
        dest.writeByte((byte) (company ? 1 : 0));
    }
}
