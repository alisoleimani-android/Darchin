package co.tinab.darchin.controller.fragment.store;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.cachapa.expandablelayout.ExpandableLayout;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.fragment.other.MySupportMapFragment;
import co.tinab.darchin.controller.tools.Display;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.controller.tools.MyDateTime;
import co.tinab.darchin.model.User;
import co.tinab.darchin.model.store.Store;
import co.tinab.darchin.view.component.SectionView;
import co.tinab.darchin.view.toolbox.MoneyTextView;
import co.tinab.darchin.view.toolbox.TextViewLight;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment implements OnMapReadyCallback, ExpandableLayout.OnExpansionUpdateListener, SectionView.RequestCompleteListener {
    private NestedScrollView scrollView;
    private Store store;
    private ExpandableLayout expandableLayout;
    private TextViewLight txtServiceRange;
    private boolean isFragmentLoaded = false;
    private MySupportMapFragment mapFragment;

    public static InfoFragment newInstance(Store store){
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putParcelable("store",store);
        fragment.setArguments(args);
        return fragment;
    }

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey("store")) {
            store = getArguments().getParcelable("store");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        scrollView = view.findViewById(R.id.scroll_view);
        mapFragment = (MySupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment.getView() != null) {
            ViewGroup.LayoutParams params = mapFragment.getView().getLayoutParams();
            params.height = Display.heightOfMap(getContext());
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            mapFragment.getView().setLayoutParams(params);
        }
        mapFragment.setListener(new MySupportMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                scrollView.requestDisallowInterceptTouchEvent(true);
            }
        });

        // Info:
        // today hours
        TextViewLight txtTodayHour = view.findViewById(R.id.txt_today_hour);
        txtTodayHour.setText(store.getHoursOfDayStr(getContext(), MyDateTime.getToday()));

        // delivery cost
        TextViewLight txtDeliveryCost = view.findViewById(R.id.txt_delivery_cost);
        txtDeliveryCost.setText(getString(R.string.from_to_pattern, store.getDeliveryCost().get("min"),
                store.getDeliveryCost().get("max")).concat("\t").concat(getString(R.string.money_unit)));

        // min order
        MoneyTextView txtMinOrder = view.findViewById(R.id.txt_minimum_order);
        txtMinOrder.setText(String.valueOf(store.getMinimumOrder()));

        // service range
        txtServiceRange = view.findViewById(R.id.txt_service_range);
        txtServiceRange.setText(store.getServiceRange(getContext()));

        if (store.hasTooManyServiceRange()) {
            ViewGroup containerServiceRange = view.findViewById(R.id.container_service_range);
            expandableLayout = view.findViewById(R.id.expandable_layout);
            expandableLayout.setOnExpansionUpdateListener(this);

            TextViewLight txtRestOfServiceRange = view.findViewById(R.id.txt_service_range_all);
            txtRestOfServiceRange.setText(store.getRestOfServiceRange());

            containerServiceRange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandableLayout.toggle(true);
                }
            });
        }

        // address
        TextViewLight txtAddress = view.findViewById(R.id.txt_address);
        txtAddress.setText(store.getFullAddress(getContext()));

        // hours of week
        TextViewLight txtSaturday = view.findViewById(R.id.txt_saturday_time);
        txtSaturday.setText(store.getHoursOfDayStr(getContext(),"saturday"));

        TextViewLight txtSunday = view.findViewById(R.id.txt_sunday_time);
        txtSunday.setText(store.getHoursOfDayStr(getContext(),"sunday"));

        TextViewLight txtMonday = view.findViewById(R.id.txt_monday_time);
        txtMonday.setText(store.getHoursOfDayStr(getContext(),"monday"));

        TextViewLight txtTuesday = view.findViewById(R.id.txt_tuesday_time);
        txtTuesday.setText(store.getHoursOfDayStr(getContext(),"tuesday"));

        TextViewLight txtWednesday = view.findViewById(R.id.txt_wednesday_time);
        txtWednesday.setText(store.getHoursOfDayStr(getContext(),"wednesday"));

        TextViewLight txtThursday = view.findViewById(R.id.txt_thursday_time);
        txtThursday.setText(store.getHoursOfDayStr(getContext(),"thursday"));

        TextViewLight txtFriday = view.findViewById(R.id.txt_friday_time);
        txtFriday.setText(store.getHoursOfDayStr(getContext(),"friday"));

        SectionView sectionView = view.findViewById(R.id.container_section);
        sectionView.setOnRequestCompleteListener(this);

        if (FunctionHelper.isConnected(getContext())) {
            sectionView.requestData(store.getSlug(),"top");
        }else {
            sectionView.bind(User.getInstance(getContext())
                    .getSections(getContext(),store.getSlug(),"top"));
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isFragmentLoaded) {
            isFragmentLoaded = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // sync google map
                    mapFragment.getMapAsync(InfoFragment.this);
                }
            },50);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng position = new LatLng(store.getLatitude(), store.getLongitude());
        googleMap.addMarker(new MarkerOptions()
                .position(position)
                .title(store.getName())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_logo))
        );
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position,15.0f));
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                Context context = getContext();

                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                if (context != null)title.setTypeface(Typeface
                        .createFromAsset(context.getAssets(),"fonts/Light.ttf"));
                title.setText(marker.getTitle());
                info.addView(title);
                return info;
            }
        });
    }

    @Override
    public void onExpansionUpdate(float expansionFraction, int state) {
        switch (state){
            case ExpandableLayout.State.COLLAPSED:
                txtServiceRange.setText(store.getServiceRange(getContext()));
                break;

            case ExpandableLayout.State.EXPANDED:
                txtServiceRange.setText(store.getServiceRange(getContext()).replace("...",""));
                break;
        }
    }

    @Override
    public void onRequestCompleted() {

    }

    @Override
    public void onRequestFailed() {

    }
}
