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
import co.tinab.darchin.model.store.Store;
import co.tinab.darchin.view.toolbox.TextViewLight;
import co.tinab.darchin.view.toolbox.TextViewNormal;

/**
 * Created by A.S.R on 1/2/2018.
 */

public class HorizontalStoreListAdapter extends RecyclerView.Adapter<HorizontalStoreListAdapter.ViewHolder> {
    private List<Store> storeList;

    public HorizontalStoreListAdapter(List<Store> storeList){
        this.storeList = storeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_horizontal_store_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.fetchData(storeList.get(position));
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgLogo;
        private TextViewNormal txtName,txtScore;
        private TextViewLight txtAddress,txtDiscount;
        private View row;
        private ViewHolder(View itemView) {
            super(itemView);
            row = itemView;
            imgLogo = itemView.findViewById(R.id.img);
            txtName = itemView.findViewById(R.id.txt_name);
            txtAddress = itemView.findViewById(R.id.txt_address);
            txtDiscount = itemView.findViewById(R.id.txt_discount);
            txtScore = itemView.findViewById(R.id.txt_score);
        }
        private void fetchData(Store store){
            if (FunctionHelper.isConnected(row.getContext())) {
                Picasso.with(row.getContext())
                        .load(store.getPicture())
                        .resizeDimen(R.dimen.horizontal_store_image_width,
                                R.dimen.horizontal_store_image_height)
                        .centerCrop()
                        .error(R.drawable.ic_store_placeholder)
                        .placeholder(R.drawable.ic_store_placeholder)
                        .into(imgLogo);
            }else {
                Picasso.with(row.getContext())
                        .load(store.getPicture())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .resizeDimen(R.dimen.horizontal_store_image_width,
                                R.dimen.horizontal_store_image_height)
                        .centerCrop()
                        .error(R.drawable.ic_store_placeholder)
                        .placeholder(R.drawable.ic_store_placeholder)
                        .into(imgLogo);
            }
            txtName.setText(store.getName());
            txtAddress.setText(store.getAddress());
            if (store.getVote().getAverage() > 0) {
                txtScore.setBackground(store.getVote().getScoreBackground(row.getContext()));
                txtScore.setText(store.getVote().getAverageString());
                txtScore.setVisibility(View.VISIBLE);
            }else {
                txtScore.setVisibility(View.INVISIBLE);
            }

            if (store.hasDiscount()) {
                txtDiscount.setVisibility(View.VISIBLE);
                txtDiscount.setText(store.getDiscount());
            }else {
                txtDiscount.setVisibility(View.INVISIBLE);
            }
        }
    }
}
