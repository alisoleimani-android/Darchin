package co.tinab.darchin.controller.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.store.Store;
import co.tinab.darchin.view.toolbox.TextViewLight;
import co.tinab.darchin.view.toolbox.TextViewNormal;

/**
 * Created by A.S.R on 1/7/2018.
 */

public class VerticalStoreListAdapter extends RecyclerView.Adapter<VerticalStoreListAdapter.ViewHolder> {
    private List<Store> storeList;

    public VerticalStoreListAdapter(List<Store> storeList){
        this.storeList = storeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_vertical_store_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(storeList.get(position));
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextViewLight txtDesc;
        TextViewNormal txtName,txtDiscount,txtScore;
        RoundedImageView imgRestaurant;
        View row;
        private ViewHolder(View itemView) {
            super(itemView);
            row = itemView;
            txtName = itemView.findViewById(R.id.txt_name);
            txtScore = itemView.findViewById(R.id.txt_score);
            txtDesc = itemView.findViewById(R.id.txt_desc);
            txtDiscount = itemView.findViewById(R.id.txt_discount);
            imgRestaurant = itemView.findViewById(R.id.img);
        }
        void bind(Store store){
            if (store != null) {
                if (FunctionHelper.isConnected(row.getContext())) {
                    Picasso.with(row.getContext())
                            .load(store.getPicture())
                            .resizeDimen(R.dimen.store_image_width,R.dimen.store_image_height)
                            .centerCrop()
                            .error(R.drawable.ic_store_placeholder)
                            .placeholder(R.drawable.ic_store_placeholder)
                            .into(imgRestaurant);
                }else {
                    Picasso.with(row.getContext())
                            .load(store.getPicture())
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .resizeDimen(R.dimen.store_image_width,R.dimen.store_image_height)
                            .centerCrop()
                            .error(R.drawable.ic_store_placeholder)
                            .placeholder(R.drawable.ic_store_placeholder)
                            .into(imgRestaurant);
                }

                txtName.setText(store.getName());
                txtScore.setBackground(store.getVote().getScoreBackground(row.getContext()));
                txtScore.setText(store.getVote().getAverageString());
                txtDesc.setText(store.getFullAddress(row.getContext()));

                if (store.hasDiscount()) {
                    txtDiscount.setVisibility(View.VISIBLE);
                    txtDiscount.setText(store.getDiscount());
                }else {
                    txtDiscount.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}
