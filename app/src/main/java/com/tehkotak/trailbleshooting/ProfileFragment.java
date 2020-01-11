package com.tehkotak.trailbleshooting;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    //declare Fire base
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private Button btnLogout;
    private TextView tvName;


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

        getUserData();

        btnLogout = v.findViewById(R.id.btnLogout);
        tvName = v.findViewById(R.id.tv_name);

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

    private void getUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            //tvName.setText(email);
        }
    }

    private void LogOut() {
        firebaseAuth.signOut();
        Intent in = new Intent(getActivity(), LoginActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(in);
        Toast.makeText(getActivity(), "Berhasil keluar", Toast.LENGTH_SHORT).show();
    }

}
