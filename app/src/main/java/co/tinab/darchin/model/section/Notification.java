package co.tinab.darchin.model.section;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by A.S.R on 3/29/2018.
 */

public class Notification {

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

    public String getName() {
        return name;
    }

    public String getDegree() {
        return degree;
    }

    public String getLink() {
        return link;
    }

    public String getLinkType() {
        return linkType;
    }

    public String getDescription() {
        return description;
    }
}
