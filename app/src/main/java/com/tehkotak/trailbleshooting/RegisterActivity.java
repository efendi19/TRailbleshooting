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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private TextView mas;
    private EditText et_email, et_pass, et_pass_conf;
    private Button btnRegist;

    private ProgressDialog progressDialog;

    /*Declare an instance of FirebaseAuth*/
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mas = findViewById(R.id.tv_masuk);
        et_email = findViewById(R.id.ed_email_reg);
        et_pass = findViewById(R.id.ed_pass_reg);
        et_pass_conf = findViewById(R.id.ed_pass_conf_reg);
        btnRegist = findViewById(R.id.btn_daftar);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mendaftarkan akun baru...");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //input email, pass
                String email = et_email.getText().toString().trim();
                String pass = et_pass.getText().toString().trim();
                String pass_conf = et_pass_conf.getText().toString().trim();

                //validation
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    //set error and focus email edit text
                    et_email.setError("Email tidak valid!");
                    et_email.setFocusable(true);
                } else if (pass.length() < 6) {
                    et_pass.setError("Password kurang dari 6 karakter!");
                    et_pass.setFocusable(true);
                /*} else if (!pass.equals(pass_conf)) {
                    et_pass_conf.setError("Tidak sama!");
                    et_pass_conf.setFocusable(true);*/
                } else {
                    registerUser(email, pass, pass_conf);
                }

                Toast.makeText(RegisterActivity.this, "Masuk ke activity login", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void registerUser(String email, String pass, String pass_conf) {
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, dismiss dialog and start activity register
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "Berhasil daftar" + user.getEmail(), Toast.LENGTH_SHORT).show();
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
