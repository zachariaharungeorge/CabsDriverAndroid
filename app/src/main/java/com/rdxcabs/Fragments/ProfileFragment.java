package com.rdxcabs.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.rdxcabs.Beans.DriverBean;
import com.rdxcabs.Constants.Constants;
import com.rdxcabs.R;

/**
 * Created by arung on 17/5/16.
 */
public class ProfileFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);


        SharedPreferences sp=this.getActivity().getSharedPreferences(Constants.MYCABS_DRIVER, Context.MODE_PRIVATE);
        final String uname = sp.getString(Constants.USERNAME,"");

        final TextView fullName = (TextView) view.findViewById(R.id.ProfileText1);
        final TextView email = (TextView) view.findViewById(R.id.ProfileText2);
        final TextView phone = (TextView) view.findViewById(R.id.ProfileText3);
        final TextView username = (TextView) view.findViewById(R.id.ProfileText4);

       final ProgressDialog progressDialog=ProgressDialog.show(getContext(), "Loading", "Loading Details", false,false);
        Firebase firebaseRef = new Firebase(Constants.FIREBASE_URL + Constants.URL_SEP + Constants.DRIVER).child(uname);
        firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DriverBean u = dataSnapshot.getValue(DriverBean.class);
                    fullName.setText(u.getFullName());
                    email.setText(u.getEmail());
                    phone.setText(u.getPhone());
                    username.setText(u.getUsername());
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return view;
    }


}
