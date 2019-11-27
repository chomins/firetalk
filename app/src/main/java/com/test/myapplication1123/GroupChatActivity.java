package com.test.myapplication1123;

//그룹채팅- 20150465조민수 만듬
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.test.myapplication1123.Adapters.MessageAdapter;
import com.test.myapplication1123.Models.AllMethods;
import com.test.myapplication1123.Models.Message;
import com.test.myapplication1123.Models.User;

import java.util.ArrayList;
import java.util.List;

public class GroupChatActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference messagedb;
    FirebaseFirestore messagedb2;
    MessageAdapter messageAdapter;
    User u; //user클래스 u는 유저정보를 담은 클래스입니다.
    List<Message> messages;


    RecyclerView rvMessage;
    EditText etMessage;
    ImageButton imgButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        init();
    }

    private void init(){  //액티비티가 실행되었을때 뷰와 메시지를 보낼수 있는 버튼 ui를 생성합니다.
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        u=new User();

        rvMessage=(RecyclerView) findViewById(R.id.rvMessage);
        etMessage=(EditText) findViewById(R.id.etMessage);
        imgButton =(ImageButton)findViewById(R.id.btnSend);
        imgButton.setOnClickListener(this);
        messages = new ArrayList<>();


    }

    @Override
    public void onClick(View view) { //send 버튼을 클릭했을때 빈칸이라면 보낼수 없게하고 다른경우에는 디비에 값을 보낼수 있도록 합니다.
        if(!TextUtils.isEmpty(etMessage.getText().toString()))
        {
            Message message = new Message(etMessage.getText().toString(),u.getName());
            etMessage.setText("");
            messagedb.push().setValue(message);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"you cannot send blank message",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //로그아웃을 할수 있는 옵션메뉴입니다.
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.MenuLogout){
            auth.signOut();
            finish();
            startActivity(new Intent(GroupChatActivity.this,MainActivity.class));
        }

        return  super.onOptionsItemSelected(item);
    }

    protected void onStart() {
        super.onStart();
        final FirebaseUser currentUser= auth.getCurrentUser(); //로그인된 유저를 가져옵니다.
        u.setUid(currentUser.getUid());
        u.setEmail(currentUser.getEmail());

        database.getReference("Users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                u= dataSnapshot.getValue(User.class);
                u.setUid(currentUser.getUid());
                AllMethods.name=u.getName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        messagedb = database.getReference("messages");
        messagedb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { //메시지가 생성되었을때 디비로 보내주는역할을 합니다.
                Message message = dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());
                messages.add(message);
                displayMessages(messages);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());

                List<Message> newMessages = new ArrayList<Message>();

                for (Message m: messages){
                    if(m.getKey().equals(message.getKey())){
                        newMessages.add(message);
                    }
                    else {
                        newMessages.add(m);
                    }
                }
                messages = newMessages;
                displayMessages(messages);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { //메시지를 DB에서 삭제할수 있는 기능을 합니다.
                Message message = dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());
                List<Message> newMessages = new ArrayList<Message>();
                for (Message m: messages){
                    if(!m.getKey().equals(message.getKey())){
                        newMessages.add(m);
                    }
                }
                messages = newMessages;
                displayMessages(messages);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        messages = new ArrayList<>();

    }

    private void displayMessages(List<Message> messages) { //메시지를 화면에 출력하는 역할을 합니다.
        rvMessage.setLayoutManager(new LinearLayoutManager(GroupChatActivity.this)); //rvMessage는 RecycleViewMessage입니다. 그룹채팅 액티비티를 Recycleview 레이아웃으로 설정합니다.
        messageAdapter = new MessageAdapter(GroupChatActivity.this,messages,messagedb); //그룹채팅 엑티비티에서 사용하고 메시지와 메시지 DB를 MessageAdapter에 넘겨줍니다.
        rvMessage.setAdapter(messageAdapter); //rvMessage에 어댑터를 messageAdapter객체로 설정함
    }
}
