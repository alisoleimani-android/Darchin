package co.tinab.darchin.view.component;

import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.section.Notification;
import co.tinab.darchin.model.section.Section;

/**
 * Created by A.S.R on 1/3/2018.
 */

class NotificationSectionView {
    private ViewGroup parent;
    private View view;

    NotificationSectionView(ViewGroup parent) {
        this.parent = parent;
    }

    public View getView(){
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_notification_section_view,parent,false);
        return view;
    }

    void requestData(Section section){
        List<Notification> notifications = getNotification(section.getContents());
        if (!notifications.isEmpty()) {
            bind(notifications.get(0));

        }else {
            view.setVisibility(View.GONE);
        }
    }

    private List<Notification> getNotification(JsonArray jsonArray){
        Gson gson = new Gson();
        List<Notification> notifications = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray){
            Notification notification = gson.fromJson(jsonElement,Notification.class);
            notifications.add(notification);
        }
        return notifications;
    }

    private void bind(final Notification notification){
        CardView containerInfo = view.findViewById(R.id.container_info);
        TextView txtDesc = view.findViewById(R.id.txt_desc);
        TextView txtName = view.findViewById(R.id.txt_name);
        containerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FunctionHelper.gotoLink(parent.getContext(),
                        notification.getLinkType(),
                        notification.getLink(),
                        notification.getName()
                );
            }
        });

        int resID;
        int textColorId;
        switch (notification.getDegree()){
            case "info":
                textColorId = android.R.color.white;
                resID = R.color.info;
                break;

            case "warning":
                textColorId = R.color.primaryText;
                resID = R.color.alert;
                break;

            case "danger":
                textColorId = android.R.color.white;
                resID = R.color.failure;
                break;

            case "success":
                textColorId = android.R.color.white;
                resID = R.color.success;
                break;

            default:
                textColorId = R.color.primaryText;
                resID = R.color.default_color;
                break;
        }
        containerInfo.setCardBackgroundColor(ContextCompat.getColor(parent.getContext(),resID));

        if (!notification.getName().trim().isEmpty()) {
            txtName.setVisibility(View.VISIBLE);
            txtName.setText(notification.getName());
            txtName.setTextColor(ResourcesCompat.getColor(parent.getResources(),textColorId,null));
        }else {
            txtName.setVisibility(View.GONE);
        }
        txtDesc.setText(notification.getDescription());
        txtDesc.setTextColor(ResourcesCompat.getColor(parent.getResources(),textColorId,null));
    }
}
