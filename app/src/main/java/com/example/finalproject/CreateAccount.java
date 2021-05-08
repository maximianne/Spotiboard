package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccount extends AppCompatActivity {


    private String createPassword;
    private String createPasswordConfirm;
    private String email;

    private Button createAcct;

    private EditText et_enterEmail;
    private EditText et_createPassword;
    private EditText et_createPasswordConfirm;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        et_enterEmail = findViewById(R.id.editText_enterEmail);
        et_createPassword = findViewById(R.id.editText_createPassword);
        et_createPasswordConfirm = findViewById(R.id.editText_confirmPassword);

        mAuth = FirebaseAuth.getInstance();

        createAcct = findViewById(R.id.button_createAccount);

        createAcct.setOnClickListener(new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            et_createPassword = findViewById(R.id.editText_createPassword);

            createPassword = et_createPassword.getText().toString().trim();
            email = et_enterEmail.getText().toString().trim();
            createPasswordConfirm = et_createPasswordConfirm.getText().toString(); // add later as a confirmation

            if(TextUtils.isEmpty(createPassword)){
                et_createPassword.setError("Password is required.");
            }
            if(TextUtils.isEmpty(email)){
                et_enterEmail.setError("Email is required"); //if it's empty it wil take you to the main screen which is not what I want
            }
            if(createPassword.length() < 6){
                et_createPassword.setError("Password must be >= 6 characters");
            }
            mAuth.createUserWithEmailAndPassword(email,createPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(CreateAccount.this, "User Created", Toast.LENGTH_SHORT).show();
                        launchNextActivity(v);
                    }
                    else{
                        Toast.makeText(CreateAccount.this, "Something went wrong, please try again " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }


    });

}

    public void launchNextActivity(View v){
        Intent intent= new Intent(CreateAccount.this, Activity1.class);
        startActivity(intent);
    }

}
