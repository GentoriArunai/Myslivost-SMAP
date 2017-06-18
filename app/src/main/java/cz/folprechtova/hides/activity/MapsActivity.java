package cz.folprechtova.hides.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import cz.folprechtova.hides.R;
import cz.folprechtova.hides.dto.Hide;
import cz.folprechtova.hides.utils.FakeDataBuilder;
import cz.folprechtova.hides.utils.JSONUtils;

public class MapsActivity extends FragmentActivity {

    private GoogleMap googleMap;
    private MapView mapView;
    private Hide hide;
    private List<Hide> hides;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (getIntent().hasExtra("HIDE")) {
            hide = (Hide) getIntent().getExtras().getSerializable("HIDE"); //takhle to mělo být :))
        } else {
            hides = JSONUtils.getListFromJson(FakeDataBuilder.FAKE, Hide[].class);
        }

        mapView = (MapView) findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MapsActivity.this.googleMap = googleMap;
                if (hide == null) {
                    setupMapWithHides();
                } else {
                    showJustOurOneHide();
                }
            }
        });
    }

    private void setupMapWithHides() {
        for (Hide hide : hides) {
            googleMap.addMarker(new MarkerOptions().position(new LatLng(hide.getLatitude(), hide.getLongitude()))
                    .title(hide.getName())
                    .snippet(isOccupiedAllHides(hide.isOccupied())));
        }
        LatLng location = new LatLng(50.111992, 15.062731);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12.8f));
    }

    private String isOccupiedAllHides(boolean occ) {
        if (occ)
            return "Obsazený";
        else
            return "Volný";
    }

    private void showJustOurOneHide() {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(hide.getLatitude(), hide.getLongitude()))
                .title(hide.getName())
                .snippet(isOccupied(hide.isOccupied())));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(hide.getLatitude(), hide.getLongitude()), 15.8f));
    }

    private String isOccupied(boolean occ) {
        if (occ) {
            hide.setOccupied(true);
            return "Obsazený"; }
        else {
            hide.setOccupied(false);
            return "Volný"; }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
        super.onLowMemory();
    }

}
