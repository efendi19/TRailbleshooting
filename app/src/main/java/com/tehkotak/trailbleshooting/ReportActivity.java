package com.tehkotak.trailbleshooting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tehkotak.trailbleshooting.adapter.FirebaseViewHolderAdapter;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity {

    private RecyclerView rvReport;
    private ArrayList<DataModel> arrayList;
    private DatabaseReference databaseReference;
    FirebaseViewHolderAdapter adapter;

    /*@Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        rvReport = (RecyclerView) findViewById(R.id.rv_report);
        rvReport.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Data_kerusakan");
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList = new ArrayList<DataModel>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    DataModel model = dataSnapshot1.getValue(DataModel.class);
                    arrayList.add(model);
                }

                adapter = new FirebaseViewHolderAdapter(ReportActivity.this, arrayList);
                rvReport.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReportActivity.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
