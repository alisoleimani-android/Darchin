package co.tinab.darchin.model.store;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import co.tinab.darchin.model.Constant;

/**
 * Created by A.S.R on 1/2/2018.
 */

public class Product implements Parcelable{
    @Expose
    @SerializedName("store_id")
    private int storeId;

    @Expose
    @SerializedName("id")
    private int productId;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("description")
    private String description;

    @Expose
    @SerializedName("category_id")
    private int categoryID;

    @Expose
    @SerializedName("type")
    private ProductType type;

    @Expose
    @SerializedName("inventory")
    private int inventory;

    @Expose
    @SerializedName("daily_inventory")
    private int dailyInventory;

    @Expose
    @SerializedName("items")
    private List<ProductItem> items = new ArrayList<>();

    private List<ProductItem> selectedItems = new ArrayList<>();

    public Product(){}

    protected Product(Parcel in) {
        productId = in.readInt();
        name = in.readString();
        description = in.readString();
        categoryID = in.readInt();
        type = in.readParcelable(ProductType.class.getClassLoader());
        in.readTypedList(items,ProductItem.CREATOR);
        in.readTypedList(selectedItems,ProductItem.CREATOR);
        inventory = in.readInt();
        dailyInventory = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getName() {
        if (name == null) {
            return "";
        }
        return name;
    }

    public String getDescription() {
        if (description == null) {
            return "";
        }
        return description;
    }

    public String getLessDescription(){
        if (description == null) {
            return "";
        }else {
            if (description.length() > 20) {
                return description.substring(0,20).concat("...");
            }else {
                return description;
            }
        }
    }

    public String getPicture() {
        String name = null;
        for (ProductItem item : items){
            if (item.getImage() != null) {
                name = item.getImage();
                break;
            }
        }
        if (name != null) {
            return Constant.PRODUCT_THUMBNAIL_PATH.concat(name);
        }else {
            return Constant.PRODUCT_THUMBNAIL_PATH;
        }
    }

    public String getMeduimImage(){
        String name = null;
        for (ProductItem item : items){
            if (item.getImage() != null) {
                name = item.getImage();
                break;
            }
        }
        return name;
    }

    public List<ProductItem> getSelectedItems() {
        if (selectedItems == null) {
            return new ArrayList<>();
        }
        return selectedItems;
    }

    public boolean hasManyItems(){
        return items != null && items.size() > 1;
    }

    public boolean hasAnItem(){
        return items != null && items.size() == 1;
    }

    public int getProductId() {
        return productId;
    }

    public int getStoreId() {
        return storeId;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public int getDailyInventory() {
        return dailyInventory;
    }

    public int getInventory() {
        return inventory;
    }

    public List<ProductItem> getItems() {
        if (items == null) {
            return new ArrayList<>();
        }
        return items;
    }

    public ProductType getType() {
        if (type == null) {
            return new ProductType();
        }
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(productId);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(categoryID);
        dest.writeParcelable(type, flags);
        dest.writeInt(inventory);
        dest.writeInt(dailyInventory);
        dest.writeTypedList(items);
        dest.writeTypedList(selectedItems);
    }
}
