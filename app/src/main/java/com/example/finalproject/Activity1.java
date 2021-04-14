package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class Activity1 extends AppCompatActivity {

    private String username;
    private String password;
    private Button button_login;
    private String createUsername;
    private String createPassword;
    private String createPasswordConfirm;
    private String email;
    private Button createAcct;

    private EditText et_username;
    private EditText et_password;
    private EditText et_createUsername;
    private EditText et_createPassword;
    private EditText et_createPasswordConfirm;
    private EditText et_email;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        et_username=findViewById(R.id.editText_username);
        et_username.setHint("Email"); //change to email later

        et_password= findViewById(R.id.editText_password);
        et_password.setHint("Password");

        button_login=findViewById(R.id.button_login);

       /* et_createUsername=findViewById(R.id.editText_createUsername);
        et_createUsername.setHint("Create username");
        createUsername=et_createUsername.getText().toString(); // not really used right now
*/

    /*    et_createPasswordConfirm=findViewById(R.id.editText_createPasswordConfirm);
        et_createPasswordConfirm.setHint("Confirm Password");
        createPasswordConfirm=et_createPasswordConfirm.getText().toString(); // add into an info later in the button listener
*/

        mAuth = FirebaseAuth.getInstance();

        /*@Override
        public void onStart(){
            super.onStart();

            FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);
        } */

       /* if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Activity2.class));
            finish();
        } */

        button_login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.e("taehyung", "value");
                username = et_username.getText().toString().trim();
                password = et_password.getText().toString().trim();

                if (TextUtils.isEmpty(username)) {
                    et_username.setError("Username is required.");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    et_password.setError("Password is required");
                    return;
                }

                //if(checkCredentials(username,password)==true){
                mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Activity1.this, "Logged in Successful", Toast.LENGTH_SHORT).show();
                            launchNextActivity(v);
                        }
                        else{
                            Toast.makeText(Activity1.this, "Incorrect Credentials, try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            //}

            }
        });
//        createAcct.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                //if(addUser(createUsername, createPassword,createPasswordConfirm,email)==true){
//                    createPassword = et_createPassword.getText().toString().trim();
//                    email = et_email.getText().toString().trim();
//
//                    if(TextUtils.isEmpty(createPassword)){
//                        et_createPassword.setError("Password is required.");
//                    }
//                    if(TextUtils.isEmpty(email)){
//                        et_email.setError("Email is required"); //if it's empty it wil take you to the main screen which is not what I want
//                    }
//                    if(createPassword.length() < 6){
//                        et_createPassword.setError("Password must be >= 6 characters");
//                    }
//                    mAuth.createUserWithEmailAndPassword(email,createPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if(task.isSuccessful()){
//                                Toast.makeText(Activity1.this, "User Created", Toast.LENGTH_SHORT).show();
//                                 launchNextActivity(v);
//                            }
//                            else{
//                                Toast.makeText(Activity1.this, "Something went wrong, please try again " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });
//                    //else{
//                    //Toast.makeText(Activity1.this, "Something went wrong, please try again", Toast.LENGTH_LONG).show();
//                    //}
//                   // }
//                   // launchNextActivity(v);
//                }

        //});

    }

    public boolean checkCredentials(String username, String password){
        if(TextUtils.isEmpty(username)){
            et_username.setError("Username is required.");
        }
        if(TextUtils.isEmpty(password)){
            et_password.setError("Password is required");
        }
        return false;
    }

    public boolean addUser(String createUsername, String createPassword, String createPasswordConfirm, String email){
        return false;
        //add this user to the database
    }

    public void launchNextActivity(View v){
        Intent intent= new Intent(Activity1.this, Activity2.class);
        startActivity(intent);
    }

}
