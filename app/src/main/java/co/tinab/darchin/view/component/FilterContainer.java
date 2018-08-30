package co.tinab.darchin.view.component;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.cachapa.expandablelayout.ExpandableLayout;

import co.tinab.darchin.controller.adapter.MultiChoiceFilterListAdapter;
import co.tinab.darchin.controller.adapter.SingleChoiceFilterListAdapter;
import co.tinab.darchin.model.Filter;
import co.tinab.darchin.R;
import co.tinab.darchin.view.toolbox.TextViewLight;

/**
 * Created by A.S.R on 1/14/2018.
 */

public class FilterContainer implements ExpandableLayout.OnExpansionUpdateListener {
    private ImageView imgTitle;
    private TextViewLight txtTitle;
    private RecyclerView recyclerView;
    private ViewGroup parent;
    private ExpandableLayout expandableLayout;

    public FilterContainer(ViewGroup parent){
        this.parent = parent;
    }

    public View getView(){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_container_filter, parent ,false);

        // img title
        imgTitle = view.findViewById(R.id.img);

        txtTitle = view.findViewById(R.id.txt_title);

        // recycler view
        recyclerView = view.findViewById(R.id.recycler_view);

        // expandable:
        expandableLayout = view.findViewById(R.id.expandable_layout);
        expandableLayout.setOnExpansionUpdateListener(this);

        // btn toggle
        ViewGroup btnToggle = view.findViewById(R.id.btn_toggle);
        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableLayout.toggle(true);
            }
        });


        return view;
    }

    public void bind(Filter filter) {
        RecyclerView.Adapter adapter;
        if (filter.isSingle()) {
            adapter = new SingleChoiceFilterListAdapter(filter.getItems());
            recyclerView.setAdapter(adapter);
            ((SingleChoiceFilterListAdapter) adapter).setOnItemSelectedListener(new SingleChoiceFilterListAdapter.ItemSelectedListener() {
                @Override
                public void onItemSelected(Filter.Item item) {
                    // if item is checked add it to list else remove it
                }
            });
        }else {
            adapter = new MultiChoiceFilterListAdapter(filter.getItems());
            recyclerView.setAdapter(adapter);
            ((MultiChoiceFilterListAdapter) adapter).setOnItemSelectedListener(new MultiChoiceFilterListAdapter.ItemSelectedListener() {
                @Override
                public void onItemSelected(boolean isChecked, Filter.Item item) {
                    // if item is checked add it to list else remove it
                }
            });
        }
        txtTitle.setText(filter.getName());
    }

    @Override
    public void onExpansionUpdate(float expansionFraction, int state) {
        switch (state){
            case ExpandableLayout.State.COLLAPSED:
                imgTitle.setImageResource(R.drawable.ic_add_18dp);
                break;

            case ExpandableLayout.State.EXPANDED:
                imgTitle.setImageResource(R.drawable.ic_remove_18dp);
                break;
        }
    }
}
