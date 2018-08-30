package co.tinab.darchin.controller.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.interfaces.ProductItemClickListener;
import co.tinab.darchin.model.store.Product;
import co.tinab.darchin.view.toolbox.MoneyTextView;
import co.tinab.darchin.view.toolbox.TextViewLight;
import co.tinab.darchin.view.toolbox.TextViewNormal;

/**
 * Created by A.S.R on 3/10/2018.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    private List<Product> productList;

    private ProductItemClickListener listener;
    public void setOnProductClickListener(ProductItemClickListener listener){
        this.listener = listener;
    }

    public EventListAdapter(List<Product> productList){
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_event_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private KenBurnsView imgCover;
        private RoundedImageView img;
        private TextViewLight txtDesc,txtHour;
        private MoneyTextView txtPrice,txtDiscount;
        private TextViewNormal txtTitle,txtCount;
        private ImageButton btnAdd,btnRemove;
        private View row;

        private ViewHolder(View itemView) {
            super(itemView);
            row = itemView;
            imgCover = itemView.findViewById(R.id.img_cover);
            img = itemView.findViewById(R.id.img);
            txtCount = itemView.findViewById(R.id.txt_count);
            txtDesc = itemView.findViewById(R.id.txt_desc);
            txtHour = itemView.findViewById(R.id.txt_hour);
            txtPrice = itemView.findViewById(R.id.txt_price);
            txtDiscount = itemView.findViewById(R.id.txt_discount);
            txtTitle = itemView.findViewById(R.id.txt_title);
            btnAdd = itemView.findViewById(R.id.btn_add);
            btnRemove = itemView.findViewById(R.id.btn_remove);
        }

        private void bind(Product product){

        }
    }
}
