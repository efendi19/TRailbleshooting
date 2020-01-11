package com.tehkotak.trailbleshooting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    ProgressDialog progressDialog;

    @Override
    protected void onStop() {
        super.onStop();
    }

    private ImageView icBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        dialogSetUp();

        rvReport = (RecyclerView) findViewById(R.id.rv_report);
        rvReport.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference().child(InputDataActivity.database_path);
        databaseReference.keepSynced(true);

        icBack = findViewById(R.id.ic_back);

        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList = new ArrayList<DataModel>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    DataModel model = dataSnapshot1.getValue(DataModel.class);
                    arrayList.add(model);
                }
                if (rvReport != null) {
                    progressDialog.dismiss();
                } else {
                    progressDialog.show();
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

    private void dialogSetUp() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sedang mengambil data..");
        progressDialog.setContentView(R.layout.layout_progressdialog);
        progressDialog.show();
    }
}
