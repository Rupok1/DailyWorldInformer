package com.example.dailyworldinformer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SubscribeActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button subscribe;
    private TextView loginText;

    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        email = (EditText)findViewById(R.id.emailId);
        password = (EditText)findViewById(R.id.passwordId);

        loginText = (TextView)findViewById(R.id.loginText);

        subscribe = (Button) findViewById(R.id.subscribeId);

        fAuth = FirebaseAuth.getInstance();

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SubscribeActivity.this,LoginActivity.class));
                finish();
            }
        });

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email2,password2;
                email2 = email.getText().toString();
                password2 = password.getText().toString();

                if(!Patterns.EMAIL_ADDRESS.matcher(email2).matches())
                {
                    email.setError("Invalid Email!");
                    return;
                }
                if(email2.isEmpty())
                {
                    email.setError("Required!");
                    return;
                }
                if(password2.isEmpty())
                {
                    password.setError("Required!");
                    return;
                }
                if( password2.length()<6)
                {
                    password.setError("Password length should be greater than 6 character");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email2,password2).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        Toast.makeText(SubscribeActivity.this,"Subscribed Successfully ",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SubscribeActivity.this,MainActivity.class));
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SubscribeActivity.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
}