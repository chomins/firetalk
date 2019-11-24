package com.test.myapplication1123;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {


    TextInputEditText textEmail, textPassword;
    ProgressBar progressBar;

    FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth= FirebaseAuth.getInstance();
        if(auth.getCurrentUser() !=null){
            Intent i = new Intent(MainActivity.this,GroupChatActivity.class);
            startActivity(i);
        }
        else {
            setContentView(R.layout.activity_main);

            textEmail = (TextInputEditText) findViewById(R.id.email_ed_login);
            textPassword = (TextInputEditText) findViewById(R.id.password_ed_login);
            progressBar = (ProgressBar) findViewById(R.id.progressBarLogin);


        }

    }

    public void LoginUser(View v){
        progressBar.setVisibility(View.VISIBLE);

        String email= textEmail.getText().toString();
        String password = textPassword.getText().toString();

        if(!email.equals("") && !password.equals("") ){
            auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Loged in",Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(MainActivity.this,GroupChatActivity.class);
                                startActivity(i);
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"잘못된 입력입니다. 다시 입력하세요",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        }
    }

    public void gotoRegister(View v){
        Intent i = new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(i);
    }

    public void forgotPassword(View v){

        AlertDialog.Builder alert= new AlertDialog.Builder(MainActivity.this);

        LinearLayout container = new LinearLayout(MainActivity.this);
        container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams ip = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        ip.setMargins(50,0,0,100);

        final EditText input = new EditText(MainActivity.this);
        input.setLayoutParams(ip);
        input.setGravity(Gravity.TOP|Gravity.START);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        input.setLines(1);
        input.setMaxLines(1);

        container.addView(input,ip);


        alert.setMessage("등록 된 이메일을 입력해주세요");
        alert.setTitle("비밀번호를 잃어버리셨나요?");
        alert.setView(container);

        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                String entered_email= input.getText().toString();

                auth.sendPasswordResetEmail(entered_email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    dialogInterface.dismiss();
                                    Toast.makeText(getApplicationContext(),"메일을 보냈습니다. 메일함을 확인해주세요",Toast.LENGTH_LONG);
                                }
                            }
                        });
            }
        });
    }
}
