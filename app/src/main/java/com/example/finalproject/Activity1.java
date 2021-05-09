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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class Activity1 extends AppCompatActivity {

    private String username;
    private String password;
    private Button button_login;
    private Button button_signUp;

    private EditText et_username;
    private EditText et_password;

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference refer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        et_username=findViewById(R.id.editText_username);
        et_username.setHint("Email"); //change to email later

        et_password= findViewById(R.id.editText_password);
        et_password.setHint("Password");

        button_login=findViewById(R.id.button_login);
        button_signUp = findViewById(R.id.button_signUp);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance();
        refer = db.getReference();// added to code

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
                mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            refer.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                                    String uid = user.getUid(); // pulls the UID
                                    refer.child(uid).child("Email").setValue(username);
                                }
                            });
                            Toast.makeText(Activity1.this, "Logged in Successful", Toast.LENGTH_SHORT).show();
                            launchNextActivity(v);
                        }
                        else{
                            Toast.makeText(Activity1.this, "Incorrect Credentials, try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        button_signUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                launchNextActivity2(v);
            }
        });

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

    public void launchNextActivity(View v){
        Intent intent= new Intent(Activity1.this, Activity2.class);
        startActivity(intent);
    }

    public void launchNextActivity2(View v){
        Intent intent= new Intent(Activity1.this, CreateAccount.class);
        startActivity(intent);
    }

}
