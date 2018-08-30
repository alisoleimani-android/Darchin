package co.tinab.darchin.controller.adapter;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import co.tinab.darchin.R;

/**
 * Created by A.S.R on 2/24/2018.
 */

public class LanguageListAdapter extends RecyclerView.Adapter<LanguageListAdapter.ViewHolder> {
    private List<String> languageList = new ArrayList<>();
    private LinkedHashMap<String,String> hashMap;
    private int mSelectedItem = -1;
    private String defaultLanguage;

    public interface ItemSelectedListener{
        void onItemSelected(String name);
    }
    private ItemSelectedListener listener;

    public void setOnItemSelectedListener(ItemSelectedListener listener){
        this.listener = listener;
    }

    public LanguageListAdapter(LinkedHashMap<String, String> hashMap){
        this.languageList.addAll(hashMap.keySet());
        this.hashMap = hashMap;
        this.defaultLanguage = Locale.getDefault().getLanguage();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_single_choice_medium,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mSelectedItem == -1) {
            holder.btnRadio.setChecked(hashMap.get(languageList.get(position)).contains(defaultLanguage));
        }else{
            holder.btnRadio.setChecked(position == mSelectedItem);
        }
        holder.btnRadio.setText(languageList.get(position));
    }

    @Override
    public int getItemCount() {
        return languageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private RadioButton btnRadio;

        private ViewHolder(View itemView) {
            super(itemView);
            btnRadio = (RadioButton) itemView;
            btnRadio.setTypeface(Typeface.createFromAsset(
                    itemView.getContext().getAssets(),"fonts/Light.ttf"));

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0, languageList.size());
                    listener.onItemSelected(languageList.get(mSelectedItem));
                }
            };

            btnRadio.setOnClickListener(onClickListener);
            itemView.setOnClickListener(onClickListener);
        }
    }
}
