package co.tinab.darchin.controller.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.interfaces.ProductItemClickListener;
import co.tinab.darchin.model.store.ProductItem;
import co.tinab.darchin.view.toolbox.MoneyTextView;
import co.tinab.darchin.view.toolbox.TextViewLight;

/**
 * Created by A.S.R on 4/6/2018.
 */

public class ProductItemsListAdapter extends RecyclerView.Adapter<ProductItemsListAdapter.ViewHolder> {
    private List<ProductItem> items;

    ProductItemsListAdapter(List<ProductItem> items){
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_product_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextViewLight txtName;
        private TextViewLight txtCount;
        private MoneyTextView txtPrice,txtDiscount;
        private ImageButton btnAdd,btnRemove;

        private ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtCount = itemView.findViewById(R.id.txt_count);
            txtPrice = itemView.findViewById(R.id.txt_price);
            txtDiscount = itemView.findViewById(R.id.txt_discount);
            btnAdd = itemView.findViewById(R.id.btn_add);
            btnRemove = itemView.findViewById(R.id.btn_remove);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener
                            .onAddItem(items.get(getAdapterPosition()),getAdapterPosition());
                }
            });
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener
                            .onAddItem(items.get(getAdapterPosition()),getAdapterPosition());
                }
            });
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener
                            .onRemoveItem(items.get(getAdapterPosition()),getAdapterPosition());
                }
            });
        }

        private void bind(ProductItem item){
            txtName.setText(item.getName());
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
        }
    }

    private ProductItemClickListener listener;
    public void setOnItemClickListener(ProductItemClickListener listener){
        this.listener = listener;
    }
}
