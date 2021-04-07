package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        et_username=findViewById(R.id.editText_username);
        et_username.setHint("Username");
        username=et_username.getText().toString();

        et_password= findViewById(R.id.editText_password);
        et_password.setHint("Password");
        password = et_password.getText().toString();

        button_login=findViewById(R.id.button_login);

        et_createUsername=findViewById(R.id.editText_createUsername);
        et_createUsername.setHint("Create username");
        createUsername=et_createUsername.getText().toString();

        et_createPassword=findViewById(R.id.editText_createPassword);
        et_createPassword.setHint("Create password");
        createPassword=et_createPassword.getText().toString();

        et_createPasswordConfirm=findViewById(R.id.editText_createPasswordConfirm);
        et_createPasswordConfirm.setHint("Confirm Password");
        createPasswordConfirm=et_createPasswordConfirm.getText().toString();

        et_email=findViewById(R.id.editText_email);
        et_email.setHint("Enter email");
        email=et_email.getText().toString();

        createAcct = findViewById(R.id.button_createAcct);

        button_login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(checkCredentials(username,password)==true){
                    launchNextActivity(v);
                }
                else{
                    Toast.makeText(Activity1.this, "Incorrect Credentials, try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        createAcct.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(addUser(createUsername, createPassword,createPasswordConfirm,email)==true){
                    launchNextActivity(v);
                }
                else{
                    Toast.makeText(Activity1.this, "Something went wrong, please try again", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public boolean checkCredentials(String username, String password){
        //check that the database has this username and password
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
