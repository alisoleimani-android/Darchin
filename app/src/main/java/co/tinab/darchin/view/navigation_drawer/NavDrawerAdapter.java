package co.tinab.darchin.view.navigation_drawer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Collections;
import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.view.toolbox.TextViewLight;

/**
 * Created by ali.soleimani on 7/31/2017.
 */

class NavDrawerAdapter extends RecyclerView.Adapter<NavDrawerAdapter.NavViewHolder> {
    private List<NavDrawerModel> data = Collections.emptyList();
    private LayoutInflater inflater;

    NavDrawerAdapter(Context context, List<NavDrawerModel> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public NavViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_nav_drawer, parent, false);
        return new NavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NavViewHolder holder, int position) {
        NavDrawerModel current = data.get(position);
        holder.txtTitle.setText(current.getTitle());
        holder.imgTitle.setImageResource(current.getImgResID());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class NavViewHolder extends RecyclerView.ViewHolder {
        TextViewLight txtTitle;
        ImageView imgTitle;
        NavViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
            imgTitle = itemView.findViewById(R.id.img_title);
        }
    }
}

