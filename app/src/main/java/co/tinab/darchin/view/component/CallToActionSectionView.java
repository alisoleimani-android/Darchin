package co.tinab.darchin.view.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.section.Action;
import co.tinab.darchin.model.section.Section;

/**
 * Created by A.S.R on 3/8/2018.
 */

public class CallToActionSectionView implements View.OnClickListener {
    private ViewGroup parent;
    private View view;
    private Action action;

    CallToActionSectionView(ViewGroup parent){
        this.parent = parent;
    }

    public View getView(){
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_call_to_action_section_view,parent,false);
        return view;
    }

    // request to get content
    public void requestData(Section section){
        List<Action> actions = getActions(section.getContents());
        if (!actions.isEmpty()) {
            bind(actions.get(0));

        }else {
            view.setVisibility(View.GONE);
        }
    }

    private List<Action> getActions(JsonArray jsonArray){
        Gson gson = new Gson();
        List<Action> actions = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray){
            Action action = gson.fromJson(jsonElement,Action.class);
            actions.add(action);
        }
        return actions;
    }

    private void bind(Action action){
        this.action = action;

        TextView txtTitle = view.findViewById(R.id.txt_title);
        TextView txtDesc = view.findViewById(R.id.txt_desc);
        Button btnAction = view.findViewById(R.id.btn_action);

        btnAction.setText(action.getButtonName());
        btnAction.setOnClickListener(this);
        btnAction.setBackground(action.getButtonBackground(parent.getContext()));

        if (!action.getName().trim().isEmpty()) {
            txtTitle.setVisibility(View.VISIBLE);
            txtTitle.setText(action.getName());
        }else {
            txtTitle.setVisibility(View.GONE);
        }
        txtDesc.setText(action.getDescription());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_action:
                FunctionHelper.gotoLink(parent.getContext(),
                        action.getLinkType(),
                        action.getLink(),
                        action.getName()
                );
                break;
        }
    }
}
