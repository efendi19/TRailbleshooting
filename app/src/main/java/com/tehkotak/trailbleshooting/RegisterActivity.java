package com.tehkotak.trailbleshooting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextView tvMasuk;
    private EditText et_email, et_pass, et_name;
    private Button btnRegist;

    private ProgressDialog progressDialog;

    /*Declare an instance of FirebaseAuth*/
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tvMasuk = findViewById(R.id.tv_masuk);
        et_email = findViewById(R.id.ed_email_reg);
        et_pass = findViewById(R.id.ed_pass_reg);
        et_name = findViewById(R.id.ed_name_reg);
        btnRegist = findViewById(R.id.btn_daftar);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving new account...");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        tvMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(RegisterActivity.this, LoginActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in);
            }
        });

        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //input email, pass
                String email = et_email.getText().toString().trim();
                String pass = et_pass.getText().toString().trim();
                String name = et_name.getText().toString().trim();

                //validation
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    //set error and focus email edit text
                    et_email.setError(" Invalid Email!");
                    et_email.setFocusable(true);
                } else if (pass.length() < 8) {
                    et_pass.setError("more than 8 character!");
                    et_pass.setFocusable(true);
                /*} else if (!pass.equals(name)) {
                    et_name.setError("Tidak sama!");
                    et_name.setFocusable(true);*/
                } else {
                    registerUser(email, pass);
                }

                //Toast.makeText(RegisterActivity.this, "Masuk ke activity login", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void registerUser(String email, String pass) {
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, dismiss dialog and start activity register
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();

                            String email = user.getEmail();
                            String name = et_name.getText().toString();
                            String uid = user.getUid();

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("Address", "");
                            hashMap.put("Email", email);
                            hashMap.put("Name", name);
                            hashMap.put("Phone", "");
                            hashMap.put("Uid", uid);

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("UserOfficer");
                            //String userReg = reference.push().getKey();
                            reference.child(uid).setValue(hashMap);


                            Toast.makeText(RegisterActivity.this, "Welcome : " + user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
