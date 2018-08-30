package co.tinab.darchin.model.store;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by A.S.R on 4/5/2018.
 */

public class ProductItem implements Parcelable{
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("price")
    private String price;

    @Expose
    @SerializedName("image")
    private String image;

    @Expose
    @SerializedName("packing_price")
    private String packingPrice;

    @Expose
    @SerializedName("count")
    private int count;

    @Expose
    @SerializedName("tax_price")
    private String taxPrice;

    @Expose
    @SerializedName("discount")
    private int discount;

    @Expose
    @SerializedName("minute")
    private int minute;

    // these field are usable in cart page:
    private String label;
    private String description;
    private Product product;

    private ProductItem(Parcel in) {
        id = in.readInt();
        discount = in.readInt();
        minute = in.readInt();
        name = in.readString();
        price = in.readString();
        image = in.readString();
        packingPrice = in.readString();
        count = in.readInt();
    }

    public static final Creator<ProductItem> CREATOR = new Creator<ProductItem>() {
        @Override
        public ProductItem createFromParcel(Parcel in) {
            return new ProductItem(in);
        }

        @Override
        public ProductItem[] newArray(int size) {
            return new ProductItem[size];
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

    public String getPrice() {
        if (price == null) {
            return "";
        }
        return price;
    }

    int getPriceInt(){
        return Integer.parseInt(price);
    }

    String getImage() {
        return image;
    }

    public String getLabel() {
        if (label == null) {
            return "";
        }
        return label;
    }

    public Product getProduct() {
        return product;
    }

    public String getDescription() {
        if (description == null) {
            return "";
        }
        return description;
    }

    // price
    public String getPriceWithDiscountStr(){
        return String.valueOf(getPriceInt() - (getPriceInt() * discount) / 100);
    }

    int getPriceWithDiscountInt(){
        return (getPriceInt() - (getPriceInt() * discount) / 100);
    }

    int getPriceDiscountInt(){
        return (getPriceInt() * discount) / 100;
    }

    public long getPackingPrice() {
        if (packingPrice == null || packingPrice.isEmpty()) {
            return 0;
        }
        return Long.parseLong(packingPrice);
    }

    int getPackingPriceInt(){
        if (packingPrice == null || packingPrice.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(packingPrice);
    }

    public boolean hasDiscount(){
        return discount > 0;
    }

    public long getTaxPrice() {
        if (taxPrice == null || taxPrice.isEmpty()) {
            return 0;
        }
        return Long.parseLong(taxPrice);
    }

    public int getCount() {
        return count;
    }

    public int getDiscountInt() {
        return discount;
    }

    public String getDiscountStr(){
        return String.valueOf(discount);
    }

    public int getMinuteInt() {
        return minute;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setDescription(String productDescription) {
        this.description = productDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(discount);
        dest.writeInt(minute);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(image);
        dest.writeString(packingPrice);
        dest.writeInt(count);
    }
}
