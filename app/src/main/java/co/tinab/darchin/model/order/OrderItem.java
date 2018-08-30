package co.tinab.darchin.model.order;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by A.S.R on 4/11/2018.
 */

public class OrderItem implements Parcelable{
    private String name;
    private int count;
    private String price;

    OrderItem(String name, String price, int count){
        this.name = name;
        this.price = price;
        this.count = count;
    }

    private OrderItem(Parcel in) {
        name = in.readString();
        count = in.readInt();
        price = in.readString();
    }

    public static final Creator<OrderItem> CREATOR = new Creator<OrderItem>() {
        @Override
        public OrderItem createFromParcel(Parcel in) {
            return new OrderItem(in);
        }

        @Override
        public OrderItem[] newArray(int size) {
            return new OrderItem[size];
        }
    };

    public String getName() {
        if (name == null) {
            return "";
        }
        return name;
    }

    public int getCount() {
        return count;
    }

    public String getPrice() {
        if (price == null) {
            return "";
        }
        return price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(count);
        dest.writeString(price);
    }
}
