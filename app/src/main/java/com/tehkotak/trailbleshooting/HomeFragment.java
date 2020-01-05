package com.tehkotak.trailbleshooting;


import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private TextView tv_username;
    private CardView cv_1, cv_2, cv_3, cv_4;

    /*private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;*/

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_main, container, false);

        tv_username = v.findViewById(R.id.tv_username);
        //cv_1 = v.findViewById(R.id.cv_report);
        cv_2 = v.findViewById(R.id.cv_plan);
        cv_3 = v.findViewById(R.id.cv_history);
        cv_4 = v.findViewById(R.id.cv_location);

        /*cv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Report is pressed!", Toast.LENGTH_SHORT).show();
            }
        });*/

        cv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Daily Plan is pressed!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), ReportActivity.class));
            }
        });

        cv_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "History is pressed!!", Toast.LENGTH_SHORT).show();
            }
        });

        cv_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "LocationActivity is pressed!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MapsActivity.class));
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

    /*private void onUserData() {
        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check until required data get
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    //get data
                    String username = ""+ds.child("name").getValue();
                    String email = ""+ds.child("email").getValue();
                    String photo = ""+ds.child("photo").getValue();

                    //set data
                    tv_username.setText(username);
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
        });
    }*/

}
