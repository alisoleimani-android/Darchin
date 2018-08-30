package co.tinab.darchin.controller.adapter;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.List;
import java.util.Locale;

import co.tinab.darchin.R;
import co.tinab.darchin.model.address.City;

/**
 * Created by A.S.R on 3/5/2018.
 */

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder>{
    private int mSelectedItem = -1;
    private List<City> cityList;

    public interface ItemSelectedListener{
        void onItemSelected(City city);
    }
    private ItemSelectedListener listener;

    public void setOnItemSelectedListener(ItemSelectedListener listener){
        this.listener = listener;
    }

    public CityListAdapter(List<City> cityList){
        this.cityList = cityList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_single_choice_medium,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.btn.setChecked(position == mSelectedItem);
        holder.btn.setText(cityList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private RadioButton btn;

        private ViewHolder(View itemView) {
            super(itemView);
            btn = (RadioButton) itemView;
            if (Locale.getDefault().getLanguage().equals("fa")) {
                btn.setTypeface(Typeface.createFromAsset(
                        itemView.getContext().getAssets(),"fonts/Light.ttf"));
            }else {
                btn.setTypeface(Typeface.createFromAsset(
                        itemView.getContext().getAssets(),"fonts/LatinLight.ttf"));
            }

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0,cityList.size());
                    listener.onItemSelected(cityList.get(mSelectedItem));
                }
            };

            btn.setOnClickListener(onClickListener);
            itemView.setOnClickListener(onClickListener);
        }
    }

}
