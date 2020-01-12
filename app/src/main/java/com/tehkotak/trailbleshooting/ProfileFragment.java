package com.tehkotak.trailbleshooting;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    //declare Fire base
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private Button btnLogout;
    private TextView tvNameProfile;
    private ImageView icEdit;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_profile, container, false);
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseAuth = firebaseAuth.getInstance();

        //getUserData();

        btnLogout = v.findViewById(R.id.btnLogout);
        tvNameProfile = v.findViewById(R.id.tv_nameProfile);
        icEdit = v.findViewById(R.id.ic_edit);
        final View ctxView = v.findViewById(R.id.cl_profile);

        icEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(ctxView,
                        "Coming soon..",
                        Snackbar.LENGTH_LONG).show();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogOut();
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getUserData();
    }

    private void getUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(TAG, "Nama User adalah : " + user.getDisplayName());
            String nama = user.getDisplayName();
            //String name = user.getDisplayName();
            tvNameProfile.setText(nama);
        } else {
            String email = user.getEmail();
            tvNameProfile.setText(email);
        }
    }

    private void LogOut() {
        firebaseAuth.signOut();
        Intent in = new Intent(getActivity(), LoginActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(in);
        Toast.makeText(getActivity(), "Logout successfully", Toast.LENGTH_SHORT).show();
    }

}
