package co.tinab.darchin.controller.fragment.user.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.tinab.darchin.controller.adapter.CommentProductsAdapter;
import co.tinab.darchin.model.order.Comment;
import co.tinab.darchin.R;
import co.tinab.darchin.view.toolbox.MyRecyclerView;
import co.tinab.darchin.view.toolbox.TextViewLight;
import co.tinab.darchin.view.toolbox.TextViewNormal;

/**
 * Created by A.S.R on 4/14/2018.
 */

public class ViewCommentDialog extends DialogFragment {
    private Comment comment;

    public static ViewCommentDialog newInstance(Comment comment){
        ViewCommentDialog dialog = new ViewCommentDialog();
        Bundle args = new Bundle();
        args.putParcelable("comment",comment);
        dialog.setArguments(args);
        return dialog;
    }

    public ViewCommentDialog(){
        // requires empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            comment = getArguments().getParcelable("comment");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_view_comment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextViewLight txtTitle = view.findViewById(R.id.txt_title);
        txtTitle.setText(R.string.view_comment);

        view.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        TextViewNormal txtName = view.findViewById(R.id.txt_name);
        TextViewLight txtDate = view.findViewById(R.id.txt_date);
        TextViewLight txtComment = view.findViewById(R.id.txt_comment);
        TextViewLight txtReply = view.findViewById(R.id.txt_reply);
        ViewGroup containerReply = view.findViewById(R.id.container_reply);
        MyRecyclerView recyclerView = view.findViewById(R.id.recycler_view);


        txtName.setText(comment.getUser().getName());
        txtDate.setText(comment.getCreatedAt(getContext()));
        txtComment.setText(comment.getText());
        if (!comment.getReply().getText().isEmpty()) {
            containerReply.setVisibility(View.VISIBLE);
            txtReply.setText(comment.getReply().getText());
        }else {
            containerReply.setVisibility(View.GONE);
        }
        recyclerView.setAdapter(new CommentProductsAdapter(comment.getProducts()));
    }
}
