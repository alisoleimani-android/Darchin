package co.tinab.darchin.controller.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.model.order.Comment;
import co.tinab.darchin.view.toolbox.MyRecyclerView;

/**
 * Created by A.S.R on 2/12/2018.
 */

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {
    private List<Comment> commentList = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_comment_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(commentList.get(position));
    }

    public void setNewData(List<Comment> commentList){
        this.commentList.addAll(commentList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtName,txtDate,txtComment,txtReply;
        private ViewGroup containerReply;
        private MyRecyclerView recyclerView;
        private View row;

        private ViewHolder(View itemView) {
            super(itemView);
            row = itemView;
            this.txtName = itemView.findViewById(R.id.txt_name);
            this.txtDate = itemView.findViewById(R.id.txt_date);
            this.txtComment = itemView.findViewById(R.id.txt_comment);
            this.txtReply = itemView.findViewById(R.id.txt_reply);
            this.containerReply = itemView.findViewById(R.id.container_reply);
            this.recyclerView = itemView.findViewById(R.id.recycler_view);
        }
        private void bind(Comment comment){
            txtName.setText(comment.getUser().getName());
            txtDate.setText(comment.getCreatedAt(row.getContext()));
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
}
