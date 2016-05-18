package com.rdxcabs.UIActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.rdxcabs.Adapters.TripLayoutAdapter;
import com.rdxcabs.Beans.TripsBean;
import com.rdxcabs.Constants.Constants;
import com.rdxcabs.R;

import java.util.ArrayList;

import static com.firebase.client.Firebase.setAndroidContext;

public class TripListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidContext(this);
        setContentView(R.layout.activity_my_trip_list);

        final ArrayList<TripsBean> lst= new ArrayList<TripsBean>();

        final ProgressDialog progressDialog=ProgressDialog.show(this, "Loading", "Fetching Data", false,false);
        progressDialog.show();
        Firebase myFirebaseRef = new Firebase(Constants.FIREBASE_URL + Constants.URL_SEP + Constants.TRIPS);

        SharedPreferences sp=getSharedPreferences(Constants.MYCABS_DRIVER,Context.MODE_PRIVATE);
        final String username = sp.getString(Constants.USERNAME,"");

        myFirebaseRef.addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                    TripsBean tripsBean = dataSnapshot.getValue(TripsBean.class);
                                                    if(Constants.STATUS_REQUESTED.equalsIgnoreCase(tripsBean.getState())) {
                                                        lst.add(tripsBean);
                                                    }

                                                    if(!lst.isEmpty()) {
                                                        TripLayoutAdapter tripLayoutAdapter = new TripLayoutAdapter(TripListActivity.this, R.layout.activity_trip_layout, lst);
                                                        ListView listView = (ListView) findViewById(R.id.listView);
                                                        listView.setAdapter(tripLayoutAdapter);
                                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                            @Override
                                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                final TripsBean selectedtrip = lst.get(position);
                                                                AlertDialog.Builder alert = new AlertDialog.Builder(TripListActivity.this);
                                                                alert.setTitle("Accept Ride");
                                                                alert.setMessage("From : " + selectedtrip.getSourceLoc() + "\n" + "To : " + selectedtrip.getDestLoc());
                                                                alert.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        selectedtrip.setState(Constants.STATUS_ACCEPTED);
                                                                        final ProgressDialog progressDialog=ProgressDialog.show(TripListActivity.this, "Loading", "Accepting Ride", false,false);
                                                                        progressDialog.show();

                                                                        Firebase firebaseRef = new Firebase(Constants.FIREBASE_URL + Constants.URL_SEP + Constants.TRIPS + Constants.URL_SEP + selectedtrip.getTripId());
                                                                        firebaseRef.child(selectedtrip.getTripId());
                                                                        firebaseRef.setValue(selectedtrip, new Firebase.CompletionListener() {

                                                                            @Override
                                                                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                                                                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(TripListActivity.this);
                                                                                alertDialog.setTitle("Accept Ride");
                                                                                alertDialog.setCancelable(false);
                                                                                if (firebaseError == null) {
                                                                                    SharedPreferences sharedPreferences = getSharedPreferences("username", Context.MODE_PRIVATE);
                                                                                    //SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                                    //editor.putString("username", user.get("username"));
                                                                                    //editor.commit();
                                                                                    alertDialog.setMessage("Ride Accepted");
                                                                                    alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                                            Intent intent = new Intent(TripListActivity.this, TripListActivity.class);
                                                                                            startActivity(intent);
                                                                                        }
                                                                                    });
                                                                                } else {
                                                                                    alertDialog.setTitle("Error");
                                                                                    alertDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                                        }
                                                                                    });
                                                                                }
                                                                                progressDialog.dismiss();
                                                                                alertDialog.show();
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                    }
                                                                });
                                                                alert.show();
                                                            }
                                                        });
                                                    }

                                                }

                                                @Override
                                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                                }

                                                @Override
                                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                                }

                                                @Override
                                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                                }

                                                @Override
                                                public void onCancelled(FirebaseError firebaseError) {

                                                }
                                            }
        );
        if(lst.isEmpty()){
            ListView listView = (ListView) findViewById(R.id.listView);
            listView.setEmptyView(findViewById(R.id.emptyElement));
        }
        progressDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_trip_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.signOut){
            SharedPreferences sp = getSharedPreferences("username", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor= sp.edit();
            editor.putString("username",null);
            editor.commit();
            Intent intent = new Intent(TripListActivity.this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
