package co.tinab.darchin.model.section;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import co.tinab.darchin.R;

/**
 * Created by A.S.R on 3/29/2018.
 */

public class Action {

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("description")
    private String description;

    @Expose
    @SerializedName("degree")
    private String degree;

    @Expose
    @SerializedName("link")
    private String link;

    @Expose
    @SerializedName("link_type")
    private String linkType;

    @Expose
    @SerializedName("button_name")
    private String buttonName;

    public String getName() {
        return name;
    }

    public String getDegree() {
        return degree;
    }

    public String getDescription() {
        return description;
    }

    public String getButtonName() {
        return buttonName;
    }

    public Drawable getButtonBackground(Context context){
        int resID;
        switch (degree){
            case "info":
                resID = R.drawable.btn_call_to_action_blue;
                break;

            case "warning":
                resID = R.drawable.btn_call_to_action_yellow;
                break;

            case "danger":
                resID = R.drawable.btn_call_to_action_red;
                break;

            case "success":
                resID = R.drawable.btn_call_to_action_green;
                break;

            default:
                resID = R.drawable.btn_call_to_action_blue;
                break;
        }
        return ContextCompat.getDrawable(context,resID);
    }

    public String getLink() {
        return link;
    }

    public String getLinkType() {
        return linkType;
    }
}
