package ua.com.codefire.gamedev.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlowFragment extends SupportMapFragment implements OnMapReadyCallback {

    public static final float MAP_ZOOM = 17.f;

    private LatLng current;
    private GoogleMap googleMap;
    private Handler handler;
    private Timer timer;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        handler = new Handler();

        timer = new Timer();

        current = new LatLng(50.2744468,30.5731178);

        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(current, MAP_ZOOM);

        googleMap.moveCamera(cameraUpdate);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        moveCamera(new LatLng(current.latitude + 0.00005f, current.longitude));
                    }
                });
            }
        }, 500, 50);
    }

    public LatLng getCurrent() {
        return current;
    }

    public void moveCamera(LatLng target) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(target, MAP_ZOOM));
        current = target;
    }
}
