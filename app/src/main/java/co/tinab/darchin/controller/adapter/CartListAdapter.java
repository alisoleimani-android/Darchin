package co.tinab.darchin.controller.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.interfaces.ProductItemClickListener;
import co.tinab.darchin.model.store.ProductItem;
import co.tinab.darchin.view.toolbox.MoneyTextView;

/**
 * Created by A.S.R on 3/2/2018.
 */

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {
    private List<ProductItem> items;

    public CartListAdapter(List<ProductItem> items){
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_cart_list,parent,false));
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
        private TextView txtName,txtCount,txtDesc;
        private ImageButton btnAdd;
        private ImageButton btnRemove;
        private MoneyTextView txtPrice,txtDiscount;

        private ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtCount = itemView.findViewById(R.id.txt_count);
            txtDesc = itemView.findViewById(R.id.txt_desc);
            txtPrice = itemView.findViewById(R.id.txt_price);
            txtDiscount = itemView.findViewById(R.id.txt_discount);
            btnAdd = itemView.findViewById(R.id.btn_add);
            btnRemove = itemView.findViewById(R.id.btn_remove);
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAddItem(items.get(getAdapterPosition()),getAdapterPosition());
                }
            });
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRemoveItem(items.get(getAdapterPosition()),getAdapterPosition());
                }
            });
        }
        private void bind(ProductItem item){
            txtPrice.setText(item.getPrice());
            txtName.setText(item.getLabel());
            txtDesc.setText(item.getDescription());

            // count
            if (item.getCount() > 0) {
                txtCount.setText(String.valueOf(item.getCount()));
            }else {
                txtCount.setText("");
            }

            // discount
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
