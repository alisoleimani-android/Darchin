package co.tinab.darchin.model.order;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

import co.tinab.darchin.controller.tools.MyDateTime;
import co.tinab.darchin.R;

/**
 * Created by A.S.R on 3/7/2018.
 */

public class Transaction {
    @Nullable
    @Expose
    @SerializedName("type")
    private String type;

    @Nullable
    @Expose
    @SerializedName("description")
    private String description;

    @Nullable
    @Expose
    @SerializedName("price")
    private String price;

    @Nullable
    @Expose
    @SerializedName("created_at")
    private String date;

    public String getType(Context context){
        String string = "";
        if (type == null) {
            return string;
        }else {
            switch (type){
                case "charge":
                    string = context.getString(R.string.transaction_type_charge);
                    break;

                case "gift":
                    string = context.getString(R.string.transaction_type_gift);
                    break;

                case "order":
                    string = context.getString(R.string.transaction_type_order);
                    break;

                case "order_cancel":
                    string = context.getString(R.string.transaction_type_order_cancel);
                    break;

                case "order_credit":
                    string = context.getString(R.string.transaction_type_order_charge);
                    break;
            }
            return string;
        }
    }

    public int getPriceColorRes(Context context){
        int resId = ContextCompat.getColor(context,R.color.secondaryText);
        if (type == null) {
            return resId;
        }else {
            switch (type){
                case "charge":
                    resId = ContextCompat.getColor(context,R.color.success);
                    break;

                case "gift":
                    resId = ContextCompat.getColor(context,R.color.success);
                    break;

                case "order":
                    resId = ContextCompat.getColor(context,R.color.failure);
                    break;

                case "order_cancel":
                    resId = ContextCompat.getColor(context,R.color.success);
                    break;

                case "order_credit":
                    resId = ContextCompat.getColor(context,R.color.failure);
                    break;
            }
            return resId;
        }
    }

    public String getPrice() {
        if (price == null) {
            return "0";
        }
        return price;
    }

    public String getDate() {
        if (date == null) {
            return "";
        }else {
            if (Locale.getDefault().getLanguage().equals("fa")) {
                return MyDateTime.convertGeorgianToShamsi(date);
            }
            return date;
        }
    }

    public String getDescription() {
        if (description == null) {
            return "";
        }
        return description;
    }
}
