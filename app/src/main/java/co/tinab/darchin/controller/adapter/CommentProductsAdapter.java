package co.tinab.darchin.controller.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.model.store.Product;
import co.tinab.darchin.view.toolbox.TextViewLight;

/**
 * Created by A.S.R on 4/12/2018.
 */

public class CommentProductsAdapter extends RecyclerView.Adapter<CommentProductsAdapter.ViewHolder> {
    private List<Product> products;

    public CommentProductsAdapter(List<Product> products){
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_comment_products,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextViewLight txtName;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
        }

        private void bind(Product product){
            txtName.setText(product.getName());
        }
    }
}
