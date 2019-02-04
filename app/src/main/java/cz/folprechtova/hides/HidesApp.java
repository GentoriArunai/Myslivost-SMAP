package cz.folprechtova.hides;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import cz.folprechtova.hides.activity.LoginActivity;

public class HidesApp extends Application implements BootstrapNotifier {

    private static Context context;
    private static Resources resources;
    private RegionBootstrap regionBootstrap;

    @Override
    public void onCreate() {
        super.onCreate();
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.setBackgroundBetweenScanPeriod(10000L);
        beaconManager.setBackgroundBetweenScanPeriod(20000L);
        context = getApplicationContext(); //uložíme si aplikační kontext do singletonu HidesApp (anebo ne)
        resources = getResources();

        Region region = new Region("something", Identifier.parse("50765CB7-D9EA-4E21-99A4-FA879613A492"), null, null);
        regionBootstrap = new RegionBootstrap(this, region);
    }

    public static Context getContext(){
        return context;
    }

    public static Resources getRes(){
        return resources;
    }

    @Override
    public void didEnterRegion(Region region) {
        String mac = region.getBluetoothAddress();
        Log.d("MYSLIVOST_BLE", "entered region");
        //vytáhnout známe MAC a pokud ji známe tak spustit intent
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("MAC", mac);
        startActivity(intent);
    }

    @Override
    public void didExitRegion(Region region) {

    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {

    }
}
