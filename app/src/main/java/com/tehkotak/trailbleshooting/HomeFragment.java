package com.tehkotak.trailbleshooting;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private CardView cv_1, cv_2, cv_3, cv_4;
    private ImageView icInformation;
    private TextView userNam;

    //declare Fire base
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    private ArrayList<DataModel> arrayList;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_main, container, false);

        //cv_1 = v.findViewById(R.id.cv_report);
        cv_2 = v.findViewById(R.id.cv_plan);
        //cv_3 = v.findViewById(R.id.cv_history);
        cv_4 = v.findViewById(R.id.cv_location);
        icInformation = v.findViewById(R.id.ic_information);
        userNam = v.findViewById(R.id.tv_username);

        onUserData();
        //getUsername();

        icInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
            }
        });

        //init Fire base
        firebaseAuth = firebaseAuth.getInstance();

       /* icInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });*/
        cv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "Daily Plan is pressed!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), ReportActivity.class));
            }
        });


        cv_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "LocationActivity is pressed!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MapsMyLocationActivity.class));

            }
        });

        /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser !=null) {
            onUserData();
        } else {
            //
        }*/

        return v;
    }

    private void getUsername() {
        // Read from the database
        firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("UserOfficer").child(firebaseUser.getUid());
        databaseReference = FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String ioi = dataSnapshot.child("Name").getValue().toString();
                Log.d(TAG, "Value is: " + ioi);
                userNam.setText(ioi);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void onStart() {
        checkUserLogin();
        super.onStart();
    }


    private void checkUserLogin() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            //user is signed stay here
        } else {
            //user is not sign in, go to Login Activity
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void logOut() {
        firebaseAuth.signOut();
        Intent in = new Intent(getActivity(), LoginActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(in);
        Toast.makeText(getActivity(), "Berhasil keluar", Toast.LENGTH_SHORT).show();
    }

    private void onUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(TAG, "User Login adalah : " + user.getEmail());
            String email = user.getEmail();
            //String name = user.getDisplayName();
            userNam.setText(email);
        }
        /*Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check until required data get
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    //get data
                    String username = ""+ds.child("name").getValue();
                    String email = ""+ds.child("email").getValue();
                    String photo = ""+ds.child("photo").getValue();
                    Log.d(TAG, "Value is: " + value);

                    //set data
                    userNam.setText(username);
                    //iniEmail_tv.setText(email);
                    try {
                        //Picasso.get().load(photo).into(iniPhoto_iv);
                    } catch (Exception e) {
                        //Picasso.get().load(R.drawable.ic_profile).into(iniPhoto_iv);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //
            }
        });*/
    }

}
