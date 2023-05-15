package com.razi.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razi.chat.databinding.ActivityLoginBinding;
import com.razi.chat.databinding.ActivityMainBinding;

import java.util.Objects;

public class loginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    String name,email,password;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        binding.signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=binding.email.getText().toString();
                password= binding.password.getText().toString();
                SignIn();
            }
        });
        binding.signUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name= binding.name.getText().toString();
                email=binding.email.getText().toString();
                password= binding.password.getText().toString();
                SignUp();
            }
        });


    }
    @Override
    protected void onStart()
    {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() !=null)
        {
            Intent intent = new Intent(loginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
    private void SignIn() {
        FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(email.trim(),password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Intent intent = new Intent(loginActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(loginActivity.this, "loginnnnn", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                });
    }
    private void SignUp() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.trim(), password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser firebaseUser = authResult.getUser();
                        UserProfileChangeRequest userProfileChangeRequest =
                                new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                        firebaseUser.updateProfile(userProfileChangeRequest);
                        String userId = firebaseUser.getUid();
                        UserModel userModel = new UserModel(userId, name, email, password);

                        // Store user data in the Realtime Database
                        databaseReference.child(userId).setValue(userModel)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Intent intent = new Intent(loginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(loginActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                    }
                });
    }
}