package co.tinab.darchin.model.order;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.tools.MyDateTime;
import co.tinab.darchin.model.User;
import co.tinab.darchin.model.store.Product;

/**
 * Created by A.S.R on 3/29/2018.
 */

public class Comment implements Parcelable {
    @Expose
    @SerializedName("id")
    private int commentId;

    @Expose
    @SerializedName("text")
    private String text;

    @Expose
    @SerializedName("user")
    private User user;

    @Expose
    @SerializedName("reply")
    private Reply reply;

    @Expose
    @SerializedName("products")
    private List<Product> products;

    @Expose
    @SerializedName("created_at")
    private String createdAt;

    Comment(){}

    private Comment(Parcel in) {
        commentId = in.readInt();
        text = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        reply = in.readParcelable(Reply.class.getClassLoader());
        products = in.createTypedArrayList(Product.CREATOR);
        createdAt = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getCreatedAt(Context context) {
        if (createdAt != null) {
            if (Locale.getDefault().getLanguage().equals("fa")) {
                return MyDateTime.convertGeorgianToShamsi(createdAt);
            }

            return createdAt;
        }else {
            return context.getString(R.string.not_registered);
        }
    }

    public int getCommentId() {
        return commentId;
    }

    public String getText() {
        if (text == null) {
            return "";
        }
        return text;
    }

    public List<Product> getProducts() {
        if (products == null) {
            return new ArrayList<>();
        }
        return products;
    }

    public User getUser() {
        if (user == null) {
            return new User();
        }
        return user;
    }

    public Reply getReply() {
        if (reply == null) {
            return new Reply();
        }
        return reply;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(commentId);
        dest.writeString(text);
        dest.writeParcelable(user, flags);
        dest.writeParcelable(reply, flags);
        dest.writeTypedList(products);
        dest.writeString(createdAt);
    }

    public static class Reply implements Parcelable {
        @Expose
        @SerializedName("id")
        private int replyId;

        @Expose
        @SerializedName("text")
        private String text;

        Reply(){}

        Reply(Parcel in) {
            replyId = in.readInt();
            text = in.readString();
        }

        public static final Creator<Reply> CREATOR = new Creator<Reply>() {
            @Override
            public Reply createFromParcel(Parcel in) {
                return new Reply(in);
            }

            @Override
            public Reply[] newArray(int size) {
                return new Reply[size];
            }
        };

        public int getReplyId() {
            return replyId;
        }

        public String getText() {
            if (text == null) {
                return "";
            }
            return text;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(replyId);
            dest.writeString(text);
        }
    }
}
