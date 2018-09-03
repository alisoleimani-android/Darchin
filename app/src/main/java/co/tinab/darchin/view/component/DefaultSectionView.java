package co.tinab.darchin.view.component;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.tinab.darchin.R;

public class DefaultSectionView implements View.OnClickListener {
    private ViewGroup parent;

    DefaultSectionView(ViewGroup parent){
        this.parent = parent;
    }

    public View getView(){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_default_section_view,parent,false);
        CardView containerInfo = view.findViewById(R.id.container_info);
        containerInfo.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.container_info){
            Uri uri = Uri.parse("");
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(uri);
            parent.getContext().startActivity(intent);
        }
    }
}
