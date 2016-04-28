package rdxcabs.com.cabsdriverandroid;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.model.LatLng;

import java.io.FileReader;

public class LocationService extends Service {

    public LocationService() {

    }

    @Override
    public int onStartCommand(Intent intent, final int flags, int startId){
        Firebase.setAndroidContext(this);
        final Firebase firebase = new Firebase("https://resplendent-fire-1005.firebaseio.com/Cords");
        //SharedPreferences sp=getSharedPreferences("username", Context.MODE_PRIVATE);
        //final String username = sp.getString("username","");

        final Runnable r = new Runnable() {
            @Override
            public void run() {

                while (true) {
                    final GPSTracker gps = new GPSTracker(LocationService.this);
                    if(gps.canGetLocation) {
                        LatLng startPos = new LatLng(gps.getLatitude(), gps.getLongitude());
                        firebase.child("username").setValue(startPos);
                    }
                }

            }
        };

        Thread t = new Thread(r);
        t.start();

        return flags;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
}
