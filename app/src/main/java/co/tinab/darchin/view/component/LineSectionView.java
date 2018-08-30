package co.tinab.darchin.view.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.tinab.darchin.model.section.Divider;
import co.tinab.darchin.R;
import co.tinab.darchin.view.toolbox.TextViewLight;
import co.tinab.darchin.view.toolbox.TextViewNormal;

/**
 * Created by A.S.R on 3/9/2018.
 */

public class LineSectionView {
    private ViewGroup parent;
    private TextViewLight txtTitle;
    private TextViewNormal txtDesc;

    LineSectionView(ViewGroup parent){
        this.parent = parent;
    }

    public View getView(){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_divider_line_view,parent,false);
        txtTitle = view.findViewById(R.id.txt_title);
        txtDesc = view.findViewById(R.id.txt_desc);
        return view;
    }

    public void bind(Divider divider){
        if (!divider.getName().isEmpty()) {
            txtTitle.setVisibility(View.VISIBLE);
            txtTitle.setText(divider.getName());
        }

        if (!divider.getDesc().isEmpty()) {
            txtDesc.setVisibility(View.VISIBLE);
            txtDesc.setText(divider.getDesc());
        }
    }
}
