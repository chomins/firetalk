package com.test.myapplication1123.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.test.myapplication1123.Models.AllMethods;
import com.test.myapplication1123.Models.Message;
import com.test.myapplication1123.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterViewHolder>
{

    Context context; //액티비티 이름
    List<Message> messages; // 메시지목록
    DatabaseReference messageDb; // 메시지목록이 저장되어잇는 DB

    public MessageAdapter(Context context, List<Message> messages, DatabaseReference messageDb) {
        this.context = context; //액티비티
        this.messages = messages; //보낸 메시지 리사이클뷰에 행을 표시함(세로로 나열되어있는 메시지들)
        this.messageDb = messageDb; //메시지가 저장되어있는 디비
    }

    @NonNull
    @Override
    public MessageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message,parent,false);
        return new MessageAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapterViewHolder holder, int position) { //디비와 결합하것

        Message message = messages.get(position);

        if(message.getName().equals(AllMethods.name)){ //내가 보낸 메시지 즉(메시지를 보낸 이름과 현재 유저의 이름이 일치할경우)
            holder.tvTitle.setText("You"+message.getMessage());
            holder.tvTitle.setGravity(Gravity.START);
            holder.l1.setBackgroundColor(Color.parseColor("#EF9E73")); //대화창색깔
        }
        else
        {
            holder.tvTitle.setText(message.getName()+ ":" +message.getMessage()); //다른 유저가 보낸 대화메시지
            holder.tvTitle.setGravity(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageAdapterViewHolder extends RecyclerView.ViewHolder //메시지가 어떻게 쌓이는지 전체적인 뷰
    {
        TextView tvTitle;
        ImageButton ibDelete;
            LinearLayout l1;

        public MessageAdapterViewHolder(@NonNull View itemView) //하나의 리사이클뷰에는 리니어레이아웃이 담기는데 이 리니어레이아웃이 하나의 메시지를 나타냄 따라서 l1은 하나의 리니어레이아웃 하나의 메시지 안에는 tvTitle=>유저의 아이디, ibDelete=>메시지창에 있는 삭제 버튼
        {
            super(itemView);
            tvTitle=(TextView)itemView.findViewById(R.id.tvTitle);
            ibDelete=(ImageButton)itemView.findViewById(R.id.ibDelete);
            l1=(LinearLayout)itemView.findViewById(R.id.l1Message);

            ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { //ib deledte버튼이 눌렸을 시 취해야 할 온클릭리스너
                    messageDb.child(messages.get(getAdapterPosition()).getKey()).removeValue();
                }
            });
        }
    }
}
