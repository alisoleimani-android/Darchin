package co.tinab.darchin.controller.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.model.store.Product;
import co.tinab.darchin.view.toolbox.TextViewLight;

/**
 * Created by A.S.R on 4/10/2018.
 */

public class SearchProductListAdapter extends RecyclerView.Adapter<SearchProductListAdapter.ViewHolder> {
    private List<Product> products;

    public SearchProductListAdapter(List<Product> products){
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_search_result,parent,false));
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
        private ImageView imgName;
        private TextViewLight txtName;

        private ViewHolder(View itemView) {
            super(itemView);
            imgName = itemView.findViewById(R.id.ic_name);
            txtName = itemView.findViewById(R.id.txt_name);
        }

        private void bind(Product product){
            imgName.setImageResource(R.drawable.ic_product_24dp);
            txtName.setText(product.getName());
        }
    }
}
