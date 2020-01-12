package com.tehkotak.trailbleshooting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tehkotak.trailbleshooting.adapter.FirebaseViewHolderAdapter;
import com.tehkotak.trailbleshooting.view.ReportView;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity implements ReportView {

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

    private String TAG = ReportActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        dialogSetUp();

        rvReport = (RecyclerView) findViewById(R.id.rv_report);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        rvReport.setLayoutManager(gridLayoutManager);
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

                Log.d(TAG, "Menerima data Report Kerusakan dari server, jumlah item: " + arrayList.size());
                for (DataModel modelResult : arrayList) {
                    Log.w("Kerusakan : ", modelResult.getDatetime());
                }

                adapter.setOnItemClickListener(new FirebaseViewHolderAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Toast.makeText(ReportActivity.this, "Card View : " + arrayList.get(position).getKomentar(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ReportActivity.this, MapsKerusakanActivity.class);
                        intent.putExtra("titiklat", arrayList.get(position).getLatitude());
                        intent.putExtra("titiklongi", arrayList.get(position).getLongitude());
                        startActivity(intent);
                    }
                });
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

    @Override
    public void setReportView(ArrayList<DataModel> reportView) {
        Log.d(TAG, "Menerima data Report Kerusakan dari server, jumlah item: " + reportView.size());
        for (DataModel modelResult : reportView) {
            Log.w("Kerusakan : ", modelResult.getDatetime());
        }
        setReportAdapter(reportView);
    }

    private void setReportAdapter(final ArrayList<DataModel> reportView) {
        /*adapter = new FirebaseViewHolderAdapter(this, reportView);
        rvReport.setAdapter(adapter);*/
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        //rvReport.setLayoutManager(gridLayoutManager);
        /*rvReport.setNestedScrollingEnabled(true);

        adapter.setOnItemClickListener(new FirebaseViewHolderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(ReportActivity.this, "Card View : " + reportView.get(position).getKomentar(), Toast.LENGTH_SHORT).show();
            }
        });*/
    }
}
