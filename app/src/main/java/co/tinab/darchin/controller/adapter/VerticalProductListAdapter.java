package co.tinab.darchin.controller.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.interfaces.ProductClickListener;
import co.tinab.darchin.controller.interfaces.ProductItemClickListener;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.controller.tools.ProductImageTouchDetection;
import co.tinab.darchin.model.store.Product;
import co.tinab.darchin.model.store.ProductItem;
import co.tinab.darchin.view.toolbox.MoneyTextView;
import co.tinab.darchin.view.toolbox.MyRecyclerView;
import co.tinab.darchin.view.toolbox.TextViewLight;
import co.tinab.darchin.view.toolbox.TextViewNormal;

/**
 * Created by A.S.R on 1/7/2018.
 */

public class VerticalProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Product> productList;
    private final short Multi = 1;

    public VerticalProductListAdapter(List<Product> productList){
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

    class MultiItemViewHolder extends RecyclerView.ViewHolder {
        TextViewLight txtDescription;
        RoundedImageView imgFood;
        TextViewNormal txtName;
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
        }
        @SuppressLint("ClickableViewAccessibility")
        void bind(final Product product){
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
            adapter.setOnItemClickListener(new ProductItemClickListener() {
                @Override
                public void onAddItem(ProductItem item, int position) {
                    if (listener != null) listener.onAddItem(product,item,getAdapterPosition());
                }

                @Override
                public void onRemoveItem(ProductItem item, int position) {
                    if (listener != null) listener.onRemoveItem(product,item,getAdapterPosition());
                }
            });

            imgFood.setOnTouchListener(new ProductImageTouchDetection() {
                @Override
                protected void onLongPress() {
                    if (listener != null) listener.onProductImageTouchedDown(product);
                }

                @Override
                protected void onRelease() {
                    if (listener != null) listener.onProductImageTouchedUp();
                }
            });
        }
    }

    class SingleItemViewHolder extends RecyclerView.ViewHolder {
        private TextViewLight txtDescription;
        private RoundedImageView imgFood;
        private TextViewNormal txtName;
        private TextViewLight txtCount;
        private MoneyTextView txtPrice,txtDiscount;
        private ImageButton btnAdd,btnRemove;
        View row;

        private SingleItemViewHolder(View itemView) {
            super(itemView);
            row = itemView;
            txtDescription = itemView.findViewById(R.id.txt_desc);
            imgFood = itemView.findViewById(R.id.img);
            txtName = itemView.findViewById(R.id.txt_name);
            txtCount = itemView.findViewById(R.id.txt_count);
            txtPrice = itemView.findViewById(R.id.txt_price);
            txtDiscount = itemView.findViewById(R.id.txt_discount);
            btnAdd = itemView.findViewById(R.id.btn_add);
            btnRemove = itemView.findViewById(R.id.btn_remove);
        }
        @SuppressLint("ClickableViewAccessibility")
        void bind(final Product product){
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

                if (item.getCount() > 0) {
                    txtCount.setVisibility(View.VISIBLE);
                    btnRemove.setVisibility(View.VISIBLE);
                    txtCount.setText(String.valueOf(item.getCount()));
                }else {
                    txtCount.setVisibility(View.GONE);
                    btnRemove.setVisibility(View.GONE);
                    txtCount.setText("");
                }

                if (item.hasDiscount()) {
                    txtPrice.setBackgroundResource(R.drawable.strike_through_bk);
                    txtDiscount.setVisibility(View.VISIBLE);
                    txtDiscount.setText(item.getPriceWithDiscountStr());
                }else {
                    txtPrice.setBackground(null);
                    txtDiscount.setVisibility(View.GONE);
                    txtDiscount.setText("");
                }

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProductItem item = product.getItems().get(0);
                        if (listener != null) listener.onAddItem(product,item,getAdapterPosition());
                    }
                });
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProductItem item = product.getItems().get(0);
                        if (listener != null) listener.onAddItem(product,item,getAdapterPosition());
                    }
                });
                btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProductItem item = product.getItems().get(0);
                        if (listener != null) listener.onRemoveItem(product,item,getAdapterPosition());
                    }
                });

                imgFood.setOnTouchListener(new ProductImageTouchDetection() {
                    @Override
                    protected void onLongPress() {
                        if (listener != null) listener.onProductImageTouchedDown(product);
                    }

                    @Override
                    protected void onRelease() {
                        if (listener != null) listener.onProductImageTouchedUp();
                    }
                });
            }
        }
    }


    private ProductClickListener listener;
    public void setOnProductClickListener(ProductClickListener listener){
        this.listener = listener;
    }
}
