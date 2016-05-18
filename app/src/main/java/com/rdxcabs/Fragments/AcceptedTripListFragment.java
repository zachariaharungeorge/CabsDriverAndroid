package com.rdxcabs.Fragments;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.rdxcabs.Adapters.TripLayoutAdapter;
import com.rdxcabs.Beans.TripsBean;
import com.rdxcabs.Constants.Constants;
import com.rdxcabs.R;
import com.rdxcabs.UIActivity.HomeActivity;

import java.util.ArrayList;

import static com.firebase.client.Firebase.setAndroidContext;

/**
 * Created by arung on 18/5/16.
 */
public class AcceptedTripListFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_my_trip_list, container, false);
        {
            setAndroidContext(this.getContext());

            final ArrayList<TripsBean> lst= new ArrayList<TripsBean>();

            final ProgressDialog progressDialog=ProgressDialog.show(getContext(), "Loading", "Fetching Data", false,false);
            progressDialog.show();
            Firebase myFirebaseRef = new Firebase(Constants.FIREBASE_URL + Constants.URL_SEP + Constants.TRIPS);

            SharedPreferences sp=this.getActivity().getSharedPreferences(Constants.MYCABS_DRIVER, Context.MODE_PRIVATE);
            final String username = sp.getString(Constants.USERNAME,"");

            myFirebaseRef.addChildEventListener(new ChildEventListener() {
                                                    @Override
                                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                        TripsBean tripsBean = dataSnapshot.getValue(TripsBean.class);
                                                        if(username.equals(tripsBean.getDriverUname()) && Constants.STATUS_ACCEPTED.equalsIgnoreCase(tripsBean.getState())) {
                                                            lst.add(tripsBean);
                                                        }

                                                        if(!lst.isEmpty()) {
                                                            TripLayoutAdapter tripLayoutAdapter = new TripLayoutAdapter(getContext(), R.layout.activity_trip_layout, lst);
                                                            ListView listView = (ListView) view.findViewById(R.id.listView);
                                                            listView.setAdapter(tripLayoutAdapter);
                                                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                @Override
                                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                    final TripsBean selectedtrip = lst.get(position);
                                                                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                                                    alert.setTitle("Accept Ride");
                                                                    alert.setMessage("From : " + selectedtrip.getSourceLoc() + "\n" + "To : " + selectedtrip.getDestLoc() + "\n" + "Fare : " + selectedtrip.getFare());
                                                                    alert.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            selectedtrip.setState(Constants.STATUS_ACCEPTED);
                                                                            final ProgressDialog progressDialog=ProgressDialog.show(getContext(), "Loading", "Accepting Ride", false,false);
                                                                            progressDialog.show();

                                                                            Firebase firebaseRef = new Firebase(Constants.FIREBASE_URL + Constants.URL_SEP + Constants.TRIPS + Constants.URL_SEP + selectedtrip.getTripId());
                                                                            firebaseRef.child(selectedtrip.getTripId());
                                                                            firebaseRef.setValue(selectedtrip, new Firebase.CompletionListener() {

                                                                                @Override
                                                                                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                                                                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(getContext());
                                                                                    alertDialog.setTitle("Accept Ride");
                                                                                    alertDialog.setCancelable(false);
                                                                                    if (firebaseError == null) {
                                                                                        alertDialog.setMessage("Ride Accepted");
                                                                                        alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                                Intent intent = new Intent(getContext(), HomeActivity.class);
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
                ListView listView = (ListView) view.findViewById(R.id.listView);
                listView.setEmptyView(view.findViewById(R.id.emptyElement));
            }
            progressDialog.dismiss();
        }
        return view;
    }
}
