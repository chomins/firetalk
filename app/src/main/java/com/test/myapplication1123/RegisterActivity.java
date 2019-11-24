package com.test.myapplication1123;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.test.myapplication1123.Models.User;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText textEmail, textPassword, textName;
    ProgressBar progressBar;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textEmail = (TextInputEditText)findViewById(R.id.email_ed_register);
        textPassword=(TextInputEditText)findViewById(R.id.password_ed_register);
        textName=(TextInputEditText)findViewById(R.id.name_ed_register);
        progressBar = (ProgressBar)findViewById(R.id.progressBarSignup);
        auth= FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");


    }

    public void RegisterUser(View v)
    {
            progressBar.setVisibility(View.VISIBLE);
            final String email = textEmail.getText().toString();
            final String password = textPassword.getText().toString();
            final String name = textName.getText().toString();

            if(!email.equals("") && !password.equals("") && password.length()>6){
                auth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    //DB  에 넣기
                                    FirebaseUser firebaseUser= auth.getCurrentUser();
                                    User u = new User();
                                    u.setName(name);
                                    u.setEmail(email);

                                    reference.child(firebaseUser.getUid()).setValue(u)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(getApplicationContext(),"회원가입이 완료되었습니다.",Toast.LENGTH_LONG).show();
                                                        progressBar.setVisibility(View.GONE);
                                                        finish();
                                                        Intent i = new Intent(RegisterActivity.this,GroupChatActivity.class);
                                                        startActivity(i);
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(getApplicationContext(),"회원가입에 실패했습니다.",Toast.LENGTH_LONG).show();
                                                        progressBar.setVisibility(View.GONE);
                                                    }
                                                }
                                            });
                                }
                            }
                        });
            }
    }

    public void gotoLogin(View v){
        Intent i = new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(i);
    }
}
