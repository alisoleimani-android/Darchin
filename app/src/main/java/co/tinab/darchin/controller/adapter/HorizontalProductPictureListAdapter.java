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
import co.tinab.darchin.controller.tools.Display;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.store.Product;

/**
 * Created by A.S.R on 2/12/2018.
 */

public class HorizontalProductPictureListAdapter extends
        RecyclerView.Adapter<HorizontalProductPictureListAdapter.ViewHolder>{
    private List<Product> productList;

    public HorizontalProductPictureListAdapter(List<Product> productList){
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_horizontal_picture_list,parent,false));
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
        private RoundedImageView imageView;
        private View row;
        private ViewHolder(View itemView) {
            super(itemView);
            row = itemView;
            imageView = itemView.findViewById(R.id.img);
        }
        private void bind(Product product){
            if (FunctionHelper.isConnected(row.getContext())) {
                Picasso.with(row.getContext())
                        .load(product.getPicture())
                        .resize(Display.dpToPx(170,row.getContext()),Display.dpToPx(130,row.getContext()))
                        .onlyScaleDown()
                        .placeholder(R.drawable.ic_banner_placeholder)
                        .error(R.drawable.ic_banner_placeholder)
                        .into(imageView);
            }else {
                Picasso.with(row.getContext())
                        .load(product.getPicture())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .resize(Display.dpToPx(170,row.getContext()),Display.dpToPx(130,row.getContext()))
                        .onlyScaleDown()
                        .placeholder(R.drawable.ic_banner_placeholder)
                        .error(R.drawable.ic_banner_placeholder)
                        .into(imageView);
            }
        }
    }
}
