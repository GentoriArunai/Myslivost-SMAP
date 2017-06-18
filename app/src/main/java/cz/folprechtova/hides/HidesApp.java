package cz.folprechtova.hides;


import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

public class HidesApp extends Application {

    private static Context context;
    private static Resources resources;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext(); //uložíme si aplikační kontext do singletonu HidesApp (anebo ne)
        resources = getResources();
    }

    public static Context getContext(){
        return context;
    }

    public static Resources getRes(){
        return resources;
    }
}
