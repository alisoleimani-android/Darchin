package co.tinab.darchin.controller.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.interfaces.ProductItemClickListener;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.store.Product;
import co.tinab.darchin.model.store.ProductItem;
import co.tinab.darchin.view.toolbox.MoneyTextView;
import co.tinab.darchin.view.toolbox.MyRecyclerView;

/**
 * Created by A.S.R on 1/8/2018.
 */

public class ProductSearchListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Product> productList;
    private final short Multi = 1;

    public ProductSearchListAdapter(List<Product> productList){
        this.productList = productList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case Multi:
                return new MultiItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_multi_product_list,parent,false));

            default:
                return new SingleItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_single_product_list,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case Multi:
                ((MultiItemViewHolder)holder).bind(productList.get(position));
                break;

            default:
                ((SingleItemViewHolder)holder).bind(productList.get(position));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        Product product = productList.get(position);
        if (product.hasManyItems()) {
            return Multi;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class MultiItemViewHolder extends RecyclerView.ViewHolder implements ProductItemClickListener {
        RoundedImageView imgFood;
        TextView txtName,txtDescription;
        MyRecyclerView recyclerView;
        List<ProductItem> items = new ArrayList<>();
        ProductItemsListAdapter adapter;
        View row;
        private MultiItemViewHolder(View itemView) {
            super(itemView);
            row = itemView;
            txtDescription = itemView.findViewById(R.id.txt_desc);
            imgFood = itemView.findViewById(R.id.img);
            txtName = itemView.findViewById(R.id.txt_name);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            adapter = new ProductItemsListAdapter(items);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(this);
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onClick(getAdapterPosition());
                }
            });
        }
        void bind(Product product){
            if (FunctionHelper.isConnected(row.getContext())) {
                Picasso.with(row.getContext())
                        .load(product.getPicture())
                        .resizeDimen(R.dimen.product_image_width,R.dimen.product_image_height)
                        .centerCrop()
                        .error(R.drawable.ic_product_placeholder)
                        .placeholder(R.drawable.ic_product_placeholder)
                        .into(imgFood);
            }else {
                Picasso.with(row.getContext())
                        .load(product.getPicture())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .resizeDimen(R.dimen.product_image_width,R.dimen.product_image_height)
                        .centerCrop()
                        .error(R.drawable.ic_product_placeholder)
                        .placeholder(R.drawable.ic_product_placeholder)
                        .into(imgFood);
            }
            txtName.setText(product.getName());
            txtDescription.setText(product.getDescription());

            // bind items
            if (!items.isEmpty()) items.clear();
            items.addAll(product.getItems());
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onAddItem(ProductItem item, int position) {
            if (listener != null) listener.onClick(getAdapterPosition());
        }

        @Override
        public void onRemoveItem(ProductItem item, int position) {}
    }

    class SingleItemViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView imgFood;
        private TextView txtName,txtDescription;
        private MoneyTextView txtPrice,txtDiscount;
        private ImageButton btnAdd;
        View row;

        private SingleItemViewHolder(View itemView) {
            super(itemView);
            row = itemView;
            txtDescription = itemView.findViewById(R.id.txt_desc);
            imgFood = itemView.findViewById(R.id.img);
            txtName = itemView.findViewById(R.id.txt_name);
            txtPrice = itemView.findViewById(R.id.txt_price);
            txtDiscount = itemView.findViewById(R.id.txt_discount);
            btnAdd = itemView.findViewById(R.id.btn_add);
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onClick(getAdapterPosition());
                }
            });
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onClick(getAdapterPosition());
                }
            });
        }
        void bind(Product product){
            if (product.hasAnItem()) {
                if (FunctionHelper.isConnected(row.getContext())) {
                    Picasso.with(row.getContext())
                            .load(product.getPicture())
                            .resizeDimen(R.dimen.product_image_width,R.dimen.product_image_height)
                            .centerCrop()
                            .error(R.drawable.ic_product_placeholder)
                            .placeholder(R.drawable.ic_product_placeholder)
                            .into(imgFood);
                }else {
                    Picasso.with(row.getContext())
                            .load(product.getPicture())
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .resizeDimen(R.dimen.product_image_width,R.dimen.product_image_height)
                            .centerCrop()
                            .error(R.drawable.ic_product_placeholder)
                            .placeholder(R.drawable.ic_product_placeholder)
                            .into(imgFood);
                }

                ProductItem item = product.getItems().get(0);
                txtName.setText(product.getName());
                txtDescription.setText(product.getDescription());
                txtPrice.setText(item.getPrice());

                if (item.hasDiscount()) {
                    txtPrice.setBackgroundResource(R.drawable.strike_through_bk);
                    txtDiscount.setVisibility(View.VISIBLE);
                    txtDiscount.setText(item.getPriceWithDiscountStr());
                }else {
                    txtPrice.setBackground(null);
                    txtDiscount.setVisibility(View.GONE);
                    txtDiscount.setText("");
                }
            }
        }
    }

    public interface ClickListener{
        void onClick(int productPosition);
    }
    private ClickListener listener;
    public void setOnClickListener(ClickListener listener){
        this.listener = listener;
    }
}
