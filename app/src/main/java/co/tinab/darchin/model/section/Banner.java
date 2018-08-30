package co.tinab.darchin.model.section;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import co.tinab.darchin.model.Constant;

/**
 * Created by A.S.R on 3/29/2018.
 */

public class Banner {

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("description")
    private String description;

    @Expose
    @SerializedName("picture")
    private String picture;

    @Expose
    @SerializedName("link")
    private String link;

    @Expose
    @SerializedName("link_type")
    private String linkType;

    public String getDescription() {
        return description;
    }

    public String getLinkType() {
        return linkType;
    }

    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return Constant.DOMAIN.concat("uploads/banner/medium/").concat(picture);
    }
}
