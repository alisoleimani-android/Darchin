package co.tinab.darchin.controller.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.store.Product;
import co.tinab.darchin.view.toolbox.TextViewLight;
import co.tinab.darchin.view.toolbox.TextViewNormal;

/**
 * Created by A.S.R on 1/2/2018.
 */

public class HorizontalProductListAdapter extends RecyclerView.Adapter<HorizontalProductListAdapter.ViewHolder>{
    private List<Product> productList;

    public HorizontalProductListAdapter(List<Product> productList){
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_horizontal_product_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
    private ImageView imgFood;
    private TextViewNormal txtName;
    private TextViewLight txtDesc;
    private View row;
    class ViewHolder extends RecyclerView.ViewHolder {
        private ViewHolder(View itemView) {
            super(itemView);
            row = itemView;
            imgFood = itemView.findViewById(R.id.img);
            txtName = itemView.findViewById(R.id.txt_name);
            txtDesc = itemView.findViewById(R.id.txt_desc);
        }
        private void bind(Product product){
            if (FunctionHelper.isConnected(row.getContext())) {
                Picasso.with(row.getContext())
                        .load(product.getPicture())
                        .resizeDimen(R.dimen.horizontal_product_image_width,R.dimen.horizontal_product_image_height)
                        .centerCrop()
                        .error(R.drawable.ic_product_placeholder)
                        .placeholder(R.drawable.ic_product_placeholder)
                        .into(imgFood);
            }else {
                Picasso.with(row.getContext())
                        .load(product.getPicture())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .resizeDimen(R.dimen.horizontal_product_image_width,R.dimen.horizontal_product_image_height)
                        .centerCrop()
                        .error(R.drawable.ic_product_placeholder)
                        .placeholder(R.drawable.ic_product_placeholder)
                        .into(imgFood);
            }
            txtName.setText(product.getName());
            txtDesc.setText(product.getLessDescription());
        }
    }
}
