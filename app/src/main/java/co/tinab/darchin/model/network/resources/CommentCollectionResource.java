package co.tinab.darchin.model.network.resources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.order.Comment;

/**
 * Created by A.S.R on 4/12/2018.
 */

public class CommentCollectionResource extends BasicResource {
    @Expose
    @SerializedName(Constant.DATA)
    private List<Comment> comments;

    public List<Comment> getComments() {
        return comments;
    }
}
